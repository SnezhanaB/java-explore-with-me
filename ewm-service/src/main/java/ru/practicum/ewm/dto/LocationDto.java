package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Широта и долгота места проведения события
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    /**
     * Широта
     * <p>
     * example: 55.754167
     */
    @Min(-90)
    @Max(90)
    @NotNull
    private Float lat;

    /**
     * Долгота
     * <p>
     * example: 37.62
     */
    @Min(-180)
    @Max(180)
    @NotNull
    private Float lon;
}