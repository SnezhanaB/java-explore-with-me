package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Private: События
 * <p>
 * Закрытый API для работы с событиями
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {
    private final EventService service;

    /**
     * Получение событий, добавленных текущим пользователем
     * @param userId идентификатор пользователя
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return Список событий. В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой
     * список
     */
    @GetMapping
    public List<EventShortDto> getAllEvents(@PathVariable(value = "userId") @Positive Long userId,
                                            @RequestParam(value = "from", defaultValue = "0")
                                            @PositiveOrZero Integer from,
                                            @RequestParam(value = "size", defaultValue = "10")
                                            @Positive Integer size) {
        log.info("[GET /users/{}/events] Получение событий, добавленных текущим пользователем", userId);
        return service.getAllEventsByUser(userId, from, size);
    }

    /**
     * Добавление нового события пользователем
     * <p>
     * Обратите внимание: дата и время на которые намечено событие не может быть раньше,
     * чем через два часа от текущего момента
     * @param userId идентификатор пользователя
     * @param eventDto данные добавляемого события
     * @return созданное событие
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable(value = "userId") @Positive Long userId,
                                 @RequestBody @Valid NewEventDto eventDto) {
        log.info("[POST /users/{}/events] запрос на создание события {}", userId, eventDto);
        return service.addEvent(userId, eventDto);
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     * <p>
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     * @param userId идентификатор пользователя
     * @param eventId идентификатор события
     * @return найденное событие
     */
    @GetMapping("/{eventId}")
    public EventFullDto getFullEventByOwner(@PathVariable(value = "userId") @Positive Long userId,
                                            @PathVariable(value = "eventId") @Positive Long eventId) {
        log.info("[GET /users/{}/events/{}] получение полной информации о событии " +
                        "добавленном текущим пользователем", userId, eventId);
        return service.getFullEventByOwner(userId, eventId);
    }

    /**
     * Изменение события добавленного текущим пользователем
     * @param userId идентификатор пользователя
     * @param eventId идентификатор события
     * @param userRequest Новые данные события
     * @return обновленное событие
     */
    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByOwner(@PathVariable(value = "userId") @Positive Long userId,
                                           @PathVariable(value = "eventId") @Positive Long eventId,
                                           @RequestBody @Valid UpdateEventUserRequest userRequest) {
        log.info("[PATCH /users/{}/events/{}] обновление события {}", userId, eventId, userRequest);
        return service.updateEventByOwner(userId, eventId, userRequest);
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     * @param userId идентификатор пользователя
     * @param eventId идентификатор события
     * @return Заявка на участие в событии
     */
    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getAllRequestByEventFromOwner(@PathVariable(value = "userId") @Positive Long userId,
                                                                       @PathVariable(value = "eventId") @Positive Long eventId) {
        log.info("[GET /users/{}/events/{}/requests] " +
                "запрос на получение информации о всех запросах об участии " +
                "в событии для пользователя", userId, eventId);
        return service.getAllRequestByEventFromOwner(userId, eventId);
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
     * @param userId идентификатор пользователя
     * @param eventId идентификатор события
     * @param updateRequest Новый статус для заявок на участие в событии текущего пользователя
     * @return Результат подтверждения/отклонения заявок на участие в событии
     */
    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequestFromOwner(
            @PathVariable(value = "userId") @Positive Long userId,
            @PathVariable(value = "eventId") @Positive Long eventId,
            @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        log.info("[PATCH /users/{}/events/{}/requests] " +
                "обновление статуса события от пользователя {}", userId, eventId, updateRequest);
        return service.updateStatusRequestFromOwner(userId, eventId, updateRequest);
    }
}
