package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Пользователь
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDto {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Имя
     * <p>
     * example: "Петров Иван"
     */
    private String name;
}
