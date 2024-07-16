package ru.practicum.ewm.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.AdminCommentDto;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    /**
     * Добавление нового комментария пользователем
     * @param userId идентификатор пользователя
     * @param commentDto данные добавляемого комментария
     * @return созданный комментарий
     */
    CommentDto addCommentByUser(Long userId, NewCommentDto commentDto);

    /**
     * Изменение комментария добавленного текущим пользователем
     * @param userId идентификатор пользователя
     * @param commentId идентификатор комментария
     * @param updateCommentDto Новые текст комментария
     * @return обновленный комментарий
     */
    CommentDto updateCommentByUser(Long userId, Long commentId, UpdateCommentDto updateCommentDto);

    /**
     * Удаление комментария добавленного текущим пользователем
     * @param commentId идентификатор комментария
     */
    void deleteCommentByUser(Long userId, Long commentId);

    /**
     * Получение администратором последних опубликованных комментариев постранично
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @param eventId поиск по событию
     * @param text поиск по тексту комментария
     * @return список комментариев, сначала новые
     */
    List<AdminCommentDto> searchCommentsByAdmin(
            Integer from,
            Integer size,
            Integer eventId,
            String text
    );

    /**
     * Изменение (модерация) комментария администратором
     * @param commentId идентификатор комментария
     * @param updateCommentDto Новые текст комментария
     * @return обновленный комментарий
     */
    CommentDto moderateCommentByAdmin(Long commentId, UpdateCommentDto updateCommentDto);

    /**
     * Удаление комментария администратором
     * @param commentId идентификатор комментария
     */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCommentByAdmin(Long commentId);

}
