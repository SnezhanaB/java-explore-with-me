package ru.practicum.ewm.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.InvalidParametersException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.model.enums.EventStatus;
import ru.practicum.ewm.model.enums.RequestStatus;
import ru.practicum.ewm.repository.*;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.utils.ChunkRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final ModelMapper mapper;
    private final StatsClient statsClient;
    private final ObjectMapper objectMapper;

    @Value("${server.application.name}")
    private String applicationName;

    /**
     * Поиск событий админом
     *
     * @param searchEventParams Запрос на поиск события админом
     * @return Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     */
    @Override
    public List<EventFullDto> searchEventsByAdmin(AdminSearchEventParams searchEventParams) {
        Pageable page = new ChunkRequest(searchEventParams.getFrom(), searchEventParams.getSize());
        Specification<Event> specification = Specification.where(null);

        List<Long> users = searchEventParams.getUsers();
        List<String> states = searchEventParams.getStates();
        List<Long> categories = searchEventParams.getCategories();
        LocalDateTime rangeEnd = searchEventParams.getRangeEnd();
        LocalDateTime rangeStart = searchEventParams.getRangeStart();

        if (isNotEmpty(users)) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("initiator").get("id").in(users));
        }
        if (isNotEmpty(states)) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("eventStatus").as(String.class).in(states));
        }
        if (isNotEmpty(categories)) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }
        if (rangeEnd != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }
        if (rangeStart != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }

        List<EventFullDto> result = repository.findAll(specification, page).map(this::toFullDto).toList();
        // Расчёт event.setConfirmedRequests
        List<Long> eventIds = result.stream().map(EventFullDto::getId).collect(Collectors.toList());
        List<Request> requests = requestRepository
                .findAllByEventIdInAndStatus(eventIds, RequestStatus.CONFIRMED);
        Map<Long, List<Request>> groupedRequests = requests.stream()
                .collect(Collectors.groupingBy(r -> r.getEvent().getId()));
        for (EventFullDto event : result) {
            event.setConfirmedRequests(groupedRequests.getOrDefault(event.getId(), List.of()).size());
        }
        return result;
    }

    /**
     * Редактирование данных события админом и его статуса (отклонение/публикация).
     * Обратите внимание:
     * <p>
     * * дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
     * <p>
     * * событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
     * <p>
     * * событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
     *
     * @param eventId идентификатор события
     * @param request данные для изменения информации о событии
     * @return обновленное событие
     */
    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest request) {
        Event event = getEventById(eventId);
        if (event.getEventStatus().equals(EventStatus.PUBLISHED) || event.getEventStatus().equals(EventStatus.CANCELED)) {
            throw new ConflictException("Only pending events can be changed");
        }
        boolean hasChanges = updateEventByDto(event, request);
        if (isNotNull(request.getStateAction())) {
            switch (request.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setEventStatus(EventStatus.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    event.setEventStatus(EventStatus.CANCELED);
                    break;
            }
            hasChanges = true;
        }
        if (hasChanges) {
            return toFullDto(repository.save(event));
        }
        return toFullDto(event);
    }

    /**
     * Получение событий, добавленных текущим пользователем
     *
     * @param userId идентификатор пользователя
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   количество элементов в наборе
     * @return Список событий. В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой
     * список
     */
    @Override
    public List<EventShortDto> getAllEventsByUser(Long userId, Integer from, Integer size) {
        checkUserById(userId);
        Pageable page = new ChunkRequest(from, size);

        return repository.findAll(page).map(this::toShortDto).toList();
    }

    /**
     * Добавление нового события пользователем
     * <p>
     * Обратите внимание: дата и время на которые намечено событие не может быть раньше,
     * чем через два часа от текущего момента
     *
     * @param userId   идентификатор пользователя
     * @param eventDto данные добавляемого события
     * @return созданное событие
     */
    @Override
    public EventFullDto addEvent(Long userId, NewEventDto eventDto) {
        LocalDateTime createdOn = LocalDateTime.now();
        User user = getUserById(userId);
        checkEventDate(eventDto.getEventDate());
        Category category = getCategoryById(eventDto.getCategory());

        Event event = toModel(eventDto);
        event.setCreatedOn(createdOn);
        event.setInitiator(user);
        event.setCategory(category);
        event.setEventStatus(EventStatus.PENDING);
        if (eventDto.getLocation() != null) {
            Location location = locationRepository.save(toModel(eventDto.getLocation()));
            event.setLocation(location);
        }

        Event saved = repository.save(event);
        EventFullDto fullDto = toFullDto(saved);
        fullDto.setViews(0L);
        fullDto.setConfirmedRequests(0);

        return fullDto;
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     * <p>
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return найденное событие
     */
    @Override
    public EventFullDto getFullEventByOwner(Long userId, Long eventId) {
        checkUserById(userId);
        Event event = repository.findByInitiatorIdAndId(userId, eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " and created by user with id = " + userId +
                        " not found"));
        return toFullDto(event);
    }

    /**
     * Изменение события добавленного текущим пользователем
     *
     * @param userId      идентификатор пользователя
     * @param eventId     идентификатор события
     * @param userRequest Новые данные события
     * @return обновленное событие
     */
    @Override
    public EventFullDto updateEventByOwner(Long userId, Long eventId, UpdateEventUserRequest userRequest) {
        checkUserById(userId);
        Event event = getEventById(eventId);
        if (event.getEventStatus().equals(EventStatus.PUBLISHED)) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Only creator can change event");
        }
        boolean hasChanges = updateEventByDto(event, userRequest);
        if (isNotNull(userRequest.getStateAction())) {
            switch (userRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setEventStatus(EventStatus.PENDING);
                    hasChanges = true;
                    break;
                case CANCEL_REVIEW:
                    event.setEventStatus(EventStatus.CANCELED);
                    hasChanges = true;
                    break;
            }
        }
        if (hasChanges) {
            return toFullDto(repository.save(event));
        }
        return toFullDto(event);
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return Заявка на участие в событии
     */
    @Override
    public List<ParticipationRequestDto> getAllRequestByEventFromOwner(Long userId, Long eventId) {
        checkUserById(userId);
        getFullEventByOwner(userId, eventId);

        return requestRepository.findAllByEventId(eventId).stream()
                .map(this::requestToDto).collect(Collectors.toList());
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     * <p>
     * Обратите внимание:
     * <p>
     * * если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
     * <p>
     * * нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
     * <p>
     * * статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
     * <p>
     * * если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки
     * необходимо отклонить
     *
     * @param userId        идентификатор пользователя
     * @param eventId       идентификатор события
     * @param updateRequest Новый статус для заявок на участие в событии текущего пользователя
     * @return Результат подтверждения/отклонения заявок на участие в событии
     */
    @Override
    public EventRequestStatusUpdateResult updateStatusRequestFromOwner(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        checkUserById(userId);
        Event event = getEventById(eventId);

        // если для события лимит заявок равен 0 или отключена пре-модерация заявок,
        // то подтверждение заявок не требуется
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new ConflictException("Confirmation of requests is not required");
        }

        // статус можно изменить только у заявок, находящихся в состоянии ожидания
        // (Ожидается код ошибки 409)
        List<Request> requests = requestRepository.findAllById(updateRequest.getRequestIds());
        boolean includesNotPending =
                requests.stream().anyMatch((request) -> request.getStatus() != RequestStatus.PENDING);
        if (includesNotPending) {
            throw new ConflictException("Requests must have status PENDING");
        }
        // Количество уже подтвержденных запросов на участие в событии
        int confirmedRequestsCount = requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED);
        // Количество доступных мест
        int availableLimit = event.getParticipantLimit() - confirmedRequestsCount;
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();
        RequestStatus status = updateRequest.getStatus();
        switch (status) {
            case CONFIRMED:
                // нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
                // (Ожидается код ошибки 409)
                if (availableLimit <= 0) {
                    throw new ConflictException("The participant limit has been reached");
                }

                for (Request request : requests) {
                    if (availableLimit > 0) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        confirmed.add(request);
                        availableLimit--;
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                        rejected.add(request);
                    }
                }
                break;

            case REJECTED:
                for (Request request : requests) {
                    request.setStatus(RequestStatus.REJECTED);
                    rejected.add(request);
                }
                break;

            default:
                throw new InvalidParametersException("Status should be CONFIRMED or REJECTED");
        }
        requestRepository.saveAll(requests);

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed.stream().map(this::requestToDto).collect(Collectors.toList()))
                .rejectedRequests(rejected.stream().map(this::requestToDto).collect(Collectors.toList()))
                .build();
    }

    /**
     * Получение событий с возможностью фильтрации
     * <p>
     * Обратите внимание:
     * <p>
     * * это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
     * <p>
     * * текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
     * <p>
     * * если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут
     * позже текущей даты и времени
     * <p>
     * * информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок
     * на участие
     * <p>
     * * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе
     * статистики
     *
     * @param filterParams Параметры фильтрации событий
     * @param request           служебное инфо о запросе
     * @return список найденных событий
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     */
    @Override
    public List<EventShortDto> getAllEventsByFilter(EventFilterParams filterParams, HttpServletRequest request) {
        if (filterParams.getRangeEnd() != null && filterParams.getRangeStart() != null) {
            if (filterParams.getRangeEnd().isBefore(filterParams.getRangeStart())) {
                throw new InvalidParametersException("RangeStart should be before RangeEnd");
            }
        }

        Pageable page = new ChunkRequest(filterParams.getFrom(), filterParams.getSize());

        Specification<Event> specification = Specification.where(null);
        LocalDateTime now = LocalDateTime.now();

        // Список идентификаторов категорий в которых будет вестись поиск
        if (isNotEmpty(filterParams.getCategories())) {
            specification = specification.and((root, query, criteriaBuilder) ->
                root.get("category").get("id").in(filterParams.getCategories()));
        }

        // текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
        if (filterParams.getText() != null) {
            String searchText = filterParams.getText().toLowerCase();
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + searchText + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + searchText + "%")
                ));
        }

        // если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут
        // позже текущей даты и времени
        LocalDateTime startDateTime = filterParams.getRangeStart() == null ? now : filterParams.getRangeStart();
        specification = specification.and((root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThan(root.get("eventDate"), startDateTime));
        if (filterParams.getRangeEnd() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("eventDate"), filterParams.getRangeEnd()));
        }

        // это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
        specification = specification.and((root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("eventStatus"), EventStatus.PUBLISHED));

        // Только события у которых не исчерпан лимит запросов на участие
        if (filterParams.getOnlyAvailable() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0));
        }

        List<Event> events = repository.findAll(specification, page).toList();

        // информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
        // нужно сохранить в сервисе статистики
        addRequestToStats(request);

        // информация о каждом событии должна включать в себя количество
        // просмотров и количество уже одобренных заявок на участие
        Map<Long, Long> views = getViewsForEvents(events);
        return events.stream().map(e -> {
            EventShortDto dto = toShortDto(e);
            dto.setViews(views.getOrDefault(e.getId(), 0L));
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     * <p>
     * Обратите внимание:
     * <p>
     * * событие должно быть опубликовано
     * <p>
     * * информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
     * <p>
     * * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе
     * статистики
     *
     * @param eventId идентификатор события
     * @param request служебное инфо о запросе
     * @return Инфо о событии
     * <p>
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     */
    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = getEventById(eventId);
        if (!event.getEventStatus().equals(EventStatus.PUBLISHED)) {
            throw new NotFoundException("Event with id = " + eventId + " not published");
        }
        // информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе
        addRequestToStats(request);
        // информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
        //  статистики
        Map<Long, Long> views = getViewsForEvents(List.of(event));

        EventFullDto dto = toFullDto(event);
        dto.setViews(views.getOrDefault(event.getId(), 0L));
        return dto;
    }

    private void addRequestToStats(HttpServletRequest request) {
        statsClient.addHit(EndpointHitDto.builder()
                .app(applicationName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    private Map<Long, Long> getViewsForEvents(List<Event> events) {
        List<String> uris = events.stream()
            .map(event -> String.format("/events/%s", event.getId()))
            .collect(Collectors.toList());
        List<LocalDateTime> startDates = events.stream()
                .map(Event::getCreatedOn)
                .collect(Collectors.toList());
        LocalDateTime startDate = startDates.stream()
                .min(LocalDateTime::compareTo)
                .orElse(null);
        Map<Long, Long> statsMap = new HashMap<>();
        // Получаем статистику
        if (startDate != null) {
            ResponseEntity<Object> response = statsClient.getStats(startDate, LocalDateTime.now(), uris, true);
            List<ViewStatsDto> stats = objectMapper.convertValue(response.getBody(), new TypeReference<>() {});
            statsMap = stats.stream().collect(Collectors.toMap(
                    statsDto -> parseEventIdFromUrl(statsDto.getUri()),
                    ViewStatsDto::getHits
            ));
        }
        return statsMap;
    }

    private Long parseEventIdFromUrl(String url) {
        String[] parts = url.split("/events/");
        if (parts.length == 2) {
            return Long.parseLong(parts[1]);
        }
        return -1L;
    }

    private boolean updateEventByDto(Event model, UpdateEventRequest dto) {
        boolean hasChanges = false;
        if (isNotBlank(dto.getAnnotation())) {
            model.setAnnotation(dto.getAnnotation());
            hasChanges = true;
        }
        if (isNotBlank(dto.getDescription())) {
            model.setDescription(dto.getDescription());
            hasChanges = true;
        }
        if (isNotNull(dto.getEventDate())) {
            checkEventDate(dto.getEventDate());
            model.setEventDate(dto.getEventDate());
            hasChanges = true;
        }
        if (isNotBlank(dto.getTitle())) {
            model.setTitle(dto.getTitle());
            hasChanges = true;
        }
        if (isNotNull(dto.getCategory())) {
            Category category = getCategoryById(dto.getCategory());
            model.setCategory(category);
            hasChanges = true;
        }
        if (isNotNull(dto.getLocation())) {
            Location location = toModel(dto.getLocation());
            model.setLocation(location);
            hasChanges = true;
        }
        if (isNotNull(dto.getParticipantLimit())) {
            model.setParticipantLimit(dto.getParticipantLimit());
            hasChanges = true;
        }
        if (isNotNull(dto.getRequestModeration())) {
            model.setRequestModeration(dto.getRequestModeration());
            hasChanges = true;
        }
        if (isNotNull(dto.getPaid())) {
            model.setPaid(dto.getPaid());
            hasChanges = true;
        }
        return hasChanges;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("Category with id=" + categoryId + " was not found"));
    }

    private Event getEventById(Long eventId) {
        return repository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    private void checkUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }

    /**
     * Дата и время на которые намечено событие не может быть раньше,
     * чем через два часа от текущего момента
      */
    private void checkEventDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidParametersException("дата и время на которые намечено событие " +
                    "не может быть раньше, чем через два часа от текущего момента");
        }

    }

    EventFullDto toFullDto(Event event) {
        return mapper.map(event, EventFullDto.class);
    }

    EventShortDto toShortDto(Event event) {
        return mapper.map(event, EventShortDto.class);
    }

    ParticipationRequestDto requestToDto(Request request) {
        return mapper.map(request, ParticipationRequestDto.class);
    }

    Event toModel(NewEventDto dto) {
        return mapper.map(dto, Event.class);
    }

    Location toModel(LocationDto dto) {
        return mapper.map(dto, Location.class);
    }

    boolean isNotNull(Object o) {
        return o != null;
    }

    boolean isNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }

    boolean isNotBlank(String s) {
        return s != null && !s.isBlank();
    }

}
