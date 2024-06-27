package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Пользователь
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequest {
    /**
     * Имя
     * <p>
     * example: "Петров Иван"
     */
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;
    /**
     * Адрес электронной почты
     * <p>
     * example: "petrov.i@practicummail.ru"
     */
    @NotBlank
    @Email
    @Size(min = 2, max = 255)
    private String email;
}
