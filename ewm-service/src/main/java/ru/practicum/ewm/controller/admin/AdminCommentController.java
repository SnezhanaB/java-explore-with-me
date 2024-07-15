package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.AdminCommentDto;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {
    /**
     * Получение администратором последних опубликованных комментариев постранично
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return список комментариев, сначала новые
     */
    @GetMapping
    public AdminCommentDto getComments(
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size
    ) {
        log.info("[POST /admin/comments] Получение администратором последних опубликованных комментариев");
        // TODO
        return null;
    }

    /**
     * Изменение (модерация) комментария администратором
     * @param commentId идентификатор комментария
     * @param updateCommentDto Новые текст комментария
     * @return обновленный комментарий
     */
    @PatchMapping("/{commentId}")
    public CommentDto moderateComment(
            @PathVariable(value = "commentId") @Positive Long commentId,
            @RequestBody @Valid UpdateCommentDto updateCommentDto
    ) {
        log.info("[PATCH /admin/comments/{}] модерация комментария администратором {}", commentId, updateCommentDto);
        // TODO
        return null;
    }

    /**
     * Удаление комментария администратором
     * @param commentId идентификатор комментария
     */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable @Positive Long commentId
    ) {
        log.info("[DELETE /admin/comments/{}] Удаление комментария пользователем", commentId);
        // TODO
    }
}
