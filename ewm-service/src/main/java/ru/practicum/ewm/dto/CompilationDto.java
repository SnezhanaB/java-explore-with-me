package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

/**
 * Подборка событий
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Список событий входящих в подборку
     */
    private Set<EventShortDto> events;
    /**
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned;
    /**
     * Заголовок подборки
     * <p>
     * example: "Летние концерты"
     */
    private String title;
}
