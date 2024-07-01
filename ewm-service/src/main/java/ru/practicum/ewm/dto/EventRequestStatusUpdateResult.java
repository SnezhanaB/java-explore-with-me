package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Результат подтверждения/отклонения заявок на участие в событии
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestStatusUpdateResult {
    /**
     * Подтвержденные заявки
     */
    private List<ParticipationRequestDto> confirmedRequests;

    /**
     * Отклоненные заявки
     */
    private List<ParticipationRequestDto> rejectedRequests;
}
