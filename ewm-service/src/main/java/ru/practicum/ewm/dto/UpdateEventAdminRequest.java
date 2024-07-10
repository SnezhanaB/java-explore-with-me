package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.enums.EventAdminState;

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
public class UpdateEventAdminRequest extends UpdateEventRequest {

    /**
     * Новое состояние события
     */
    private EventAdminState stateAction;

}
