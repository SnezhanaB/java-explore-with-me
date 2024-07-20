package ru.practicum.ewm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class NewCommentDto {
    /**
     * Идентификатор события
     */
    @Positive
    private Long eventId;
    /**
     * Текст комментария
     */
    @NotBlank
    @Length(min = 2, max = 2000)
    private String text;
}
