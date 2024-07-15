package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * Private: Запросы на участие
 * <p>
 * Закрытый API для работы с запросами текущего пользователя на участие в событиях
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateRequestController {
    private final RequestService service;

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
     * @param userId идентификатор пользователя
     * @return список заявок на участие в событии
     */
    @GetMapping
    public List<ParticipationRequestDto> getAllRequests(@PathVariable(value = "userId") @Positive Long userId) {
        log.info("[GET /users/{}/requests] получение информации о заявках текущего пользователя на участие в чужих " +
                "событиях", userId);
        return service.getAllRequests(userId);
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     * @param userId идентификатор пользователя
     * @param eventId идентификатор события
     * @return заявка на участие в событии
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable(value = "userId") @Positive Long userId,
                                              @RequestParam(name = "eventId") @Positive Long eventId) {
        log.info("[POST /users/{}/requests] запрос на участие в событии c id={}", eventId, userId);
        return service.addRequest(userId, eventId);
    }

    /**
     * Отмена своего запроса на участие в событии
     * @param userId идентификатор пользователя
     * @param requestId идентификатор запроса на участие
     * @return Заявка на участие в событии
     */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable(value = "userId") @Positive Long userId,
                                                   @PathVariable(value = "requestId") @Positive Long requestId) {
        log.info("[PATCH /users/{}/requests/{}/cancel] отмена запроса пользователем", userId, requestId);
        return service.cancelRequest(userId, requestId);
    }
}
