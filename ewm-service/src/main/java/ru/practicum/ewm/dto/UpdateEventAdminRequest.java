package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.model.enums.EventAdminState;
import ru.practicum.ewm.model.enums.EventUserState;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 *
 * Данные для изменения информации о событии.
 * Если поле в запросе не указано (равно null) -
 * значит изменение этих данных не требуется
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventAdminRequest {

    /**
     * Новая аннотация
     */
    @Length(min = 20, max = 2000)
    private String annotation;

    /**
     * Новая категория
     */
    private Integer category;

    /**
     * Новое описание
     */
    @Length(min = 20, max = 7000)
    private String description;

    /**
     * Новые дата и время на которые намечено событие.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     *
     * Широта и долгота места проведения события
     */
    @Valid
    private LocationDto location;

    /**
     * Новое значение флага о платности мероприятия
     */
    private Boolean paid;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    private Boolean requestModeration;

    /**
     * Новый лимит пользователей
     */
    @PositiveOrZero
    private Integer participantLimit;

    /**
     * Новое состояние события
     */
    private EventAdminState stateAction;

    /**
     * Новый заголовок
     */
    @Size(min = 3, max = 120)
    private String title;
}
