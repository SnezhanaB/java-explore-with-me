package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Текст комментария
     */
    private String text;
    /**
     * Автор
     */
    private UserShortDto author;
    /**
     * Дата создания
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    /**
     * Дата последнего редактирования
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdatedOn;
}
