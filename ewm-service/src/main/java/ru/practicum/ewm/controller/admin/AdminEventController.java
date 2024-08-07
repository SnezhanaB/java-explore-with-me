package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.AdminSearchEventParams;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * Admin: События
 * <p>
 * API для работы с событиями
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {

    private final EventService service;

    /**
     * Поиск событий админом
     * @param searchEventParams Запрос на поиск события админом
     * @return Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     */
    @GetMapping
    public List<EventFullDto> searchEventsByAdmin(@Valid AdminSearchEventParams searchEventParams) {
        log.info("[GET /admin/events] поиск события админом {}", searchEventParams);
        return service.searchEventsByAdmin(searchEventParams);
    }

    /**
     * Редактирование данных события и его статуса (отклонение/публикация).
     * Обратите внимание:
     * <p>
     * * дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
     * <p>
     * * событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
     * <p>
     * * событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
     * @param eventId идентификатор события
     * @param request данные для изменения информации о событии
     * @return обновленное событие
     */
    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable(value = "eventId") @Positive Long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequest request) {
        log.info("[PATCH /admin/events/{}] редактирование данных события {}", eventId, request);
        return service.updateEventByAdmin(eventId, request);
    }
}
