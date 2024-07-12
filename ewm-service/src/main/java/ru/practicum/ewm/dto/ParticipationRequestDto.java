package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.model.enums.RequestStatus;

import java.time.LocalDateTime;

/**
 * Заявка на участие в событии
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRequestDto {
    /**
     * Идентификатор
     */
    private Long id;

    /**
     * Дата и время создания заявки
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    /**
     * Идентификатор события
     */
    private Long eventId;

    /**
     * Идентификатор пользователя, отправившего заявку
     */
    private Long requesterId;

    /**
     * Статус заявки
     */
    private RequestStatus status;
}