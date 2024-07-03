package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.UserSearchEventParams;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collections;
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
    public List<EventShortDto> getAllEvents(@Valid UserSearchEventParams searchEventParams,
                                            HttpServletRequest request) {
        log.info("[GET /events] получение событий с возможностью фильтрации {}", searchEventParams);
        // TODO
        return Collections.emptyList();
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
    public EventFullDto getEventById(@PathVariable(value = "eventId") @Positive Integer eventId,
                                     HttpServletRequest request) {
        log.info("[GET /events/{}] Получение подробной информации об " +
                        "опубликованном событии по его идентификатору", eventId);
        // TODO
        return null;
    }
}
