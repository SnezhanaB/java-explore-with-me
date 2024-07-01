package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.enums.RequestStatus;

import java.util.Set;

/**
 * Изменение статуса запроса на участие в событии текущего пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {
    /**
     * Идентификаторы запросов на участие в событии текущего пользователя
     */
    private Set<Integer> requestIds;
    /**
     * Новый статус запроса на участие в событии текущего пользователя
     */
    private RequestStatus status;
}
