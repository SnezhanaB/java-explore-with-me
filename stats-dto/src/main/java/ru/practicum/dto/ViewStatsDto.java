package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {
    /**
     * Название сервиса
     * <p>
     * example: "ewm-main-service"
     */
    private String app;

    /**
     * URI сервиса
     * <p>
     * example: "/events/1"
     */
    private String uri;

    /**
     * Количество просмотров
     * <p>
     * example: 6
     */
    private Integer hits;
}
