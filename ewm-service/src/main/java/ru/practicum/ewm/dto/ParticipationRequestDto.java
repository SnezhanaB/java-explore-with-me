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
    private Integer id;

    /**
     * Дата и время создания заявки
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    /**
     * Идентификатор события
     */
    private Integer event;

    /**
     * Идентификатор пользователя, отправившего заявку
     */
    private Integer requester;

    /**
     * Статус заявки
     */
    private RequestStatus status;
}