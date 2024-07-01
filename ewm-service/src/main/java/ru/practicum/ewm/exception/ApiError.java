package ru.practicum.ewm.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сведения об ошибке
 */
@Getter
@Builder
public class ApiError {

    /**
     * Список стектрейсов или описания ошибок
     */
    private final List<String> errors;

    /**
     * Сообщение об ошибке
     * <p>
     * example: "Only pending or canceled events can be changed"
     */
    private final String message;

    /**
     * Общее описание причины ошибки
     * <p>
     * example: "For the requested operation the conditions are not met"
     */
    private final String reason;

    /**
     * Код статуса HTTP-ответа
     * <p>
     * example: "FORBIDDEN"
     */
    private final String status;

    /**
     * Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
     * <p>
     * example: "2022-06-09 06:27:23"
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
}
