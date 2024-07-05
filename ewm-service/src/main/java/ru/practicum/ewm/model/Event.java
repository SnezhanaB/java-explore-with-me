package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.enums.EventStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Полная информация о событии
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "events")
public class Event {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Краткое описание
     * <p>
     * example: "Эксклюзивность нашего шоу гарантирует привлечение
     * максимальной зрительской аудитории"
     */
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;

    /**
     * Категория
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Дата и время создания события (в формате \"yyyy-MM-dd HH:mm:ss\")
     * <p>
     * example: "2022-09-06 11:00:23"
     */
    @Column(name = "create_date")
    private LocalDateTime createdOn;

    /**
     * Полное описание события
     * <p>
     * example: "Что получится, если соединить кукурузу и полёт? Создатели
     * \"Шоу летающей кукурузы\" испытали эту идею на практике
     * и воплотили в жизнь инновационный проект, предлагающий
     * свежий взгляд на развлечения..."
     */
    @Column(name = "description", length = 7000)
    private String description;

    /**
     * Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\")
     * <p>
     * example: "2022-09-06 11:00:23"
     */
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    /**
     * Инициатор
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    /**
     * Широта и долгота места проведения события
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "location_id")
    private Location location;

    /**
     * Нужно ли оплачивать участие
     */
    @Column(name = "paid")
    private Boolean paid;

    /**
     * Ограничение на количество участников.
     * Значение 0 - означает отсутствие ограничения
     */
    @Column(name = "participant_limit")
    private Integer participantLimit;

    /**
     * Дата и время публикации события (в формате \"yyyy-MM-dd HH:mm:ss\")
     * <p>
     * example: "2022-09-06 11:00:23"
     */
    @Column(name = "published_date")
    private LocalDateTime publishedOn;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    @Column(name = "request_moderation")
    private Boolean requestModeration;

    /**
     * Список состояний жизненного цикла события
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventStatus state;

    /**
     * Заголовок
     * <p>
     * example: "Знаменитое шоу 'Летающая кукуруза'"
     */
    @Column(name = "title", nullable = false, length = 120)
    private String title;

}
