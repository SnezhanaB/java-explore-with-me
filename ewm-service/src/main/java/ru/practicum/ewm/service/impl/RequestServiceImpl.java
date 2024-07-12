package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.InvalidParametersException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.EventStatus;
import ru.practicum.ewm.model.enums.RequestStatus;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ModelMapper mapper = new ModelMapper();

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
     *
     * @param userId идентификатор пользователя
     * @return список заявок на участие в событии
     */
    @Override
    public List<ParticipationRequestDto> getAllRequests(Long userId) {
        checkUserById(userId);

        return repository.findAllByRequesterId(userId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return заявка на участие в событии
     */
    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        LocalDateTime now = LocalDateTime.now();

        // инициатор события не может добавить запрос на участие в своём событии
        // (Ожидается код ошибки 409)
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("User with id= " + userId + " is initiator of event");
        }

        // если у события достигнут лимит запросов на участие -
        // необходимо вернуть ошибку (Ожидается код ошибки 409)
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= repository.countByEventIdAndStatus(eventId,
                RequestStatus.CONFIRMED)) {
            throw new ConflictException("Participation request limit reached");
        }

        // нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
        if (!event.getEventStatus().equals(EventStatus.PUBLISHED)) {
            throw new ConflictException("Event not published");
        }

        // нельзя добавить повторный запрос (Ожидается код ошибки 409)
        if (repository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException("Request already exists");
        }

        Request request = new Request();
        request.setCreated(now);
        request.setEvent(event);
        request.setRequester(user);

        // если для события отключена пре-модерация запросов на участие,
        // то запрос должен автоматически перейти в состояние подтвержденного
        if (event.getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        // При добавлении запроса на участие при participantLimit == 0
        // должен быть статус CONFIRMED
        if (event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        return toDto(repository.save(request));
    }

    /**
     * Отмена своего запроса на участие в событии
     *
     * @param userId    идентификатор пользователя
     * @param requestId идентификатор запроса на участие
     * @return Заявка на участие в событии
     */
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        checkUserById(userId);
        Request request = repository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));

        if (request.getStatus().equals(RequestStatus.CANCELED) || request.getStatus().equals(RequestStatus.REJECTED)) {
            throw new InvalidParametersException("Request already canceled or rejected");
        }

        request.setStatus(RequestStatus.CANCELED);

        return toDto(repository.save(request));
    }

    private void checkUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    ParticipationRequestDto toDto(Request request) {
        return mapper.map(request, ParticipationRequestDto.class);
    }

}
