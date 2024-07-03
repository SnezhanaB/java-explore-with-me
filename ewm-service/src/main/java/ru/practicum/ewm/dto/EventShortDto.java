package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * Краткая информация о событии
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventShortDto {
    /**
     * Идентификатор
     */
    private Long id;

    /**
     * Краткое описание
     * <p>
     * example: "Эксклюзивность нашего шоу гарантирует привлечение
     * максимальной зрительской аудитории"
     */
    private String annotation;

    /**
     * Категория
     */
    private CategoryDto category;

    /**
     * Количество одобренных заявок на участие в данном событии
     */
    private Integer confirmedRequests;

    /**
     * Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\")
     * <p>
     * example: "2022-09-06 11:00:23"
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * Инициатор
     */
    private UserShortDto initiator;

    /**
     * Нужно ли оплачивать участие
     */
    private Boolean paid;

    /**
     * Заголовок
     * <p>
     * example: "Знаменитое шоу 'Летающая кукуруза'"
     */
    private String title;

    /**
     * Количество просмотрев события
     */
    private Integer views;
}
