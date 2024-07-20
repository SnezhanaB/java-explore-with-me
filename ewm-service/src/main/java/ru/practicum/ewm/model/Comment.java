package ru.practicum.ewm.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "comments")
public class Comment {
    /**
     * Идентификатор
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Текст комментария
     */
    @Column(name = "text", nullable = false)
    private String text;
    /**
     * Событие
     */
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    /**
     * Автор
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    /**
     * Дата создания
     */
    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;
    /**
     * Дата последнего редактирования
     */
    @UpdateTimestamp
    @Column(name = "last_updated_on")
    private LocalDateTime lastUpdatedOn;
}