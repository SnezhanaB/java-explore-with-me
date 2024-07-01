package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Категория
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    /**
     * Идентификатор категории
     */
    private Integer id;
    /**
     * Название категории
     * <p>
     * example: "Концерты"
     */
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
