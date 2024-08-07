package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

/**
 * Подборка событий
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {
    /**
     * Список идентификаторов событий входящих в подборку
     */
    private List<Long> events = Collections.emptyList();
    /**
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned = false;
    /**
     * Заголовок подборки
     * <p>
     * example: "Летние концерты"
     */
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
