package ru.practicum.ewm.model;

import lombok.*;
import ru.practicum.ewm.model.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Заявка на участие в событии
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "requests")
public class Request {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата и время создания заявки
     */
    @Column(name = "create_date", nullable = false)
    private LocalDateTime created;

    /**
     * Событие
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    /**
     * Пользователь, отправившего заявку
     */
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    /**
     * Статус заявки
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;
}