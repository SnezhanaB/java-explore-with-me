package ru.practicum.ewm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UpdateCommentDto {
    /**
     * Текст комментария
     */
    @NotBlank
    @Length(min = 2, max = 2000)
    private String text;
}
