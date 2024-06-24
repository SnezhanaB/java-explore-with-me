package ru.practicum.ewm.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "hits", schema = "public")
@Getter
@Setter
@ToString
public class EndpointHit {
    /**
     * Идентификатор записи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Идентификатор сервиса для которого записывается информация
     * <p>
     * example: "ewm-main-service"
     */
    @Column(name = "app", nullable = false)
    private String app;

    /**
     * URI сервиса
     * <p>
     * example: "/events/1"
     */
    @Column(name = "uri", nullable = false)
    private String uri;

    /**
     * IP-адрес пользователя, осуществившего запрос
     * <p>
     * example: "192.163.0.1"
     */
    @Column(name = "ip", nullable = false)
    private String ip;

    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
     * <p>
     * example: "2022-09-06 11:00:23"
     */
    @Column(name = "created", nullable = false)
    private LocalDateTime timestamp;
}
