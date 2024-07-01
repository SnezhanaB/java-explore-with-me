package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

/**
 * Новое событие
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {
    /**
     * Краткое описание
     * <p>
     * example: "Эксклюзивность нашего шоу гарантирует привлечение
     * максимальной зрительской аудитории"
     */
    @NotBlank
    @Length(max = 2000, min = 20)
    private String annotation;

    /**
     * Идентификатор категории к которой относится событие
     */
    @NotNull
    private Integer category;

    /**
     * Полное описание события
     * <p>
     * example: "Что получится, если соединить кукурузу и полёт? Создатели
     * \"Шоу летающей кукурузы\" испытали эту идею на практике
     * и воплотили в жизнь инновационный проект, предлагающий
     * свежий взгляд на развлечения..."
     */
    @Length(max = 7000, min = 20)
    private String description;

    /**
     * Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\")
     * <p>
     * example: "2022-09-06 11:00:23"
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * Широта и долгота места проведения события
     */
    @NotNull
    @Valid
    private LocationDto location;

    /**
     * Нужно ли оплачивать участие
     */
    private Boolean paid;

    /**
     * Ограничение на количество участников.
     * Значение 0 - означает отсутствие ограничения
     */
    @PositiveOrZero
    private Integer participantLimit;

    /**
     * Дата и время публикации события (в формате \"yyyy-MM-dd HH:mm:ss\")
     * <p>
     * example: "2022-09-06 11:00:23"
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    private Boolean requestModeration;

    /**
     * Заголовок
     * <p>
     * example: "Знаменитое шоу 'Летающая кукуруза'"
     */
    @NotNull
    @Length(min = 3, max = 120)
    private String title;
}
