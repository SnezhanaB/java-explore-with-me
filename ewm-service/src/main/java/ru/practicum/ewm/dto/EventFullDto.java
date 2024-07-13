package ru.practicum.ewm.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.model.enums.EventStatus;

import java.time.LocalDateTime;

/**
 * Полная информация о событии
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDto extends EventShortDto {

    /**
     * Дата и время создания события (в формате \"yyyy-MM-dd HH:mm:ss\")
     * <p>
     * example: "2022-09-06 11:00:23"
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * Полное описание события
     * <p>
     * example: "Что получится, если соединить кукурузу и полёт? Создатели
     * \"Шоу летающей кукурузы\" испытали эту идею на практике
     * и воплотили в жизнь инновационный проект, предлагающий
     * свежий взгляд на развлечения..."
     */
    private String description;

    /**
     * Широта и долгота места проведения события
     */
    private LocationDto location;

    /**
     * Ограничение на количество участников.
     * Значение 0 - означает отсутствие ограничения
     */
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
     * Список состояний жизненного цикла события
     */
    private EventStatus state;

}
