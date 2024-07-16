package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.EventFilterParams;
import ru.practicum.ewm.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Public: Подборки событий
 * <p>
 * Публичный API для работы с подборками событий
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {
    private final EventService service;

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
     * @param searchEventParams Запрос на поиск события пользователем
     * @param request служебное инфо о запросе
     * @return список найденных событий
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     */
    @GetMapping
    public List<EventShortDto> getAllEvents(@Valid EventFilterParams searchEventParams,
                                            HttpServletRequest request) {
        log.info("[GET /events] получение событий с возможностью фильтрации {}", searchEventParams);
        return service.getAllEventsByFilter(searchEventParams, request);
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
     * @param eventId идентификатор события
     * @param request служебное инфо о запросе
     * @return Инфо о событии
     * <p>
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     */
    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable(value = "eventId") @Positive Long eventId,
                                     HttpServletRequest request) {
        log.info("[GET /events/{}] Получение подробной информации об " +
                        "опубликованном событии по его идентификатору", eventId);
        return service.getEventById(eventId, request);
    }

    /**
     * Получить комментарии к событию
     * @param eventId идентификатор события
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return отсортированный по дате добавления список комментариев, сначала новые
     */
    @GetMapping("/{eventId}/comments")
    public List<CommentDto> getEventComments(
            @PathVariable(value = "eventId")
            @Positive Long eventId,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        log.info("[GET /events/{}/comments] Получение комментариев к событию", eventId);
        return service.getEventComments(eventId, from, size);
    }
}
