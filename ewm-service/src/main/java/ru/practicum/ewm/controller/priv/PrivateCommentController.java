package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService service;

    /**
     * Добавление нового комментария пользователем
     * @param userId идентификатор пользователя
     * @param commentDto данные добавляемого комментария
     * @return созданный комментарий
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable(value = "userId") @Positive Long userId,
                               @RequestBody @Valid NewCommentDto commentDto) {
        log.info("[POST /users/{}/comments] запрос на создание комментария {}", userId, commentDto);
        return service.addCommentByUser(userId, commentDto);
    }

    /**
     * Изменение комментария добавленного текущим пользователем
     * @param userId идентификатор пользователя
     * @param commentId идентификатор комментария
     * @param updateCommentDto Новые текст комментария
     * @return обновленный комментарий
     */
    @PatchMapping("/{commentId}")
    public CommentDto updateComment(
            @PathVariable(value = "userId") @Positive Long userId,
            @PathVariable(value = "commentId") @Positive Long commentId,
            @RequestBody @Valid UpdateCommentDto updateCommentDto
    ) {
        log.info("[PATCH /users/{}/comments/{}] обновление комментария {}", userId, commentId, updateCommentDto);
        return service.updateCommentByUser(userId, commentId, updateCommentDto);
    }

    /**
     * Удаление комментария добавленного текущим пользователем
     * @param commentId идентификатор комментария
     */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable(value = "userId") @Positive Long userId,
            @PathVariable @Positive Long commentId
    ) {
        log.info("[DELETE /users/{}/comments/{}] Удаление комментария пользователем", userId, commentId);
        service.deleteCommentByUser(userId, commentId);
    }
}
