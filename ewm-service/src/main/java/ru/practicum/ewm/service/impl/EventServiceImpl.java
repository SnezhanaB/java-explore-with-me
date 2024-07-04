package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final ModelMapper mapper = new ModelMapper();

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
        return List.of();
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
        return null;
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
    public List<EventShortDto> getAllEvents(Long userId, Integer from, Integer size) {
        return List.of();
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
        return null;
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
        return null;
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
        return null;
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
        return List.of();
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
        return null;
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
     * @param searchEventParams Запрос на поиск события пользователем
     * @param request           служебное инфо о запросе
     * @return список найденных событий
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     */
    @Override
    public List<EventShortDto> getAllEvents(UserSearchEventParams searchEventParams, HttpServletRequest request) {
        return List.of();
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
        return null;
    }
}
