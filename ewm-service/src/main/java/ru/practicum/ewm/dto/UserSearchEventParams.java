package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Запрос на поиск события пользователем
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchEventParams {

    /**
     * Текст для поиска в содержимом аннотации и подробном описании события
     */
    private String text;

    /**
     * Список идентификаторов категорий в которых будет вестись поиск
     */
    private List<Long> categories;

    /**
     * Поиск только платных/бесплатных событий
     */
    private Boolean paid;

    /**
     * Дата и время не раньше которых должно произойти событие
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;

    /**
     * Дата и время не позже которых должно произойти событие
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;

    /**
     * Только события у которых не исчерпан лимит запросов на участие
     */
    private Boolean onlyAvailable = false;

    /**
     * Вариант сортировки: по дате события или по количеству просмотров
     * <p>
     * Available values : EVENT_DATE, VIEWS
     */
    private String sort;

    /**
     * Количество событий, которые нужно пропустить для формирования текущего набора
     */
    @PositiveOrZero
    private Integer from = 0;

    /**
     * Количество событий в наборе
     */
    @Positive
    private Integer size = 10;
}