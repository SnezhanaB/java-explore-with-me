package ru.practicum.ewm.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Изменение информации о подборке событий.
 * Если поле в запросе не указано (равно null) -
 * значит изменение этих данных не требуется
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequest {

    /**
     * Идентификатор
     */
    private Long id;

    /**
     * Список id событий подборки для полной замены текущего списка
     */
    private Set<Long> events;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned;

    /**
     * Заголовок подборки
     */
    @Size(max = 50)
    private String title;
}
