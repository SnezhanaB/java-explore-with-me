package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Запрос на поиск события админом
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSearchEventParams {

    /**
     * Список id пользователей, чьи события нужно найти
     */
    private List<Long> users;

    /**
     * Список состояний в которых находятся искомые события
     */
    private List<String> states;

    /**
     * Список id категорий в которых будет вестись поиск
     */
    private List<Long> categories;

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