package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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
    private Integer id;
    /**
     * Список событий входящих в подборку
     */
    private List<EventShortDto> events;
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
