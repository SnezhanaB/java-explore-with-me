package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {
    /**
     * Идентификатор записи
     * <p>
     * example: 1
     */
    private Integer id;

    /**
     * Идентификатор сервиса для которого записывается информация
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
     * IP-адрес пользователя, осуществившего запрос
     * <p>
     * example: "192.163.0.1"
     */
    private String ip;

    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
     * <p>
     * example: ""2022-09-06 11:00:23"
     */
    private LocalDateTime timestamp;
}
