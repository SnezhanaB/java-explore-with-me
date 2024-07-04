package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Пользователь
 */
@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Имя
     * <p>
     * example: "Петров Иван"
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * Адрес электронной почты
     * <p>
     * example: "petrov.i@practicummail.ru"
     */
    @Column(name = "email", nullable = false)
    private String email;
}
