package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.AdminCommentDto;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;
import ru.practicum.ewm.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {
    private final CommentService service;

    /**
     * Получение администратором последних опубликованных комментариев постранично
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @param eventId поиск по событию
     * @param text поиск по тексту комментария
     * @return список комментариев, сначала новые
     */
    @GetMapping
    public List<AdminCommentDto> searchComments(
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size,
            @Positive Integer eventId,
            @RequestParam String text
    ) {
        log.info("[POST /admin/comments] Получение администратором последних опубликованных комментариев");
        return service.searchCommentsByAdmin(from, size, eventId, text);
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
        return service.moderateCommentByAdmin(commentId, updateCommentDto);
    }

    /**
     * Удаление комментария администратором
     * @param commentId идентификатор комментария
     */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable @Positive Long commentId
    ) {
        log.info("[DELETE /admin/comments/{}] Удаление комментария пользователем", commentId);
        service.deleteCommentByAdmin(commentId);
    }
}
