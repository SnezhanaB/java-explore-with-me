package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.AdminCommentDto;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    /**
     * Добавление нового комментария пользователем
     *
     * @param userId     идентификатор пользователя
     * @param commentDto данные добавляемого комментария
     * @return созданный комментарий
     */
    @Override
    public CommentDto addCommentByUser(Long userId, NewCommentDto commentDto) {
        return null;
    }

    /**
     * Изменение комментария добавленного текущим пользователем
     *
     * @param userId           идентификатор пользователя
     * @param commentId        идентификатор комментария
     * @param updateCommentDto Новые текст комментария
     * @return обновленный комментарий
     */
    @Override
    public CommentDto updateCommentByUser(Long userId, Long commentId, UpdateCommentDto updateCommentDto) {
        return null;
    }

    /**
     * Удаление комментария добавленного текущим пользователем
     *
     * @param userId
     * @param commentId идентификатор комментария
     */
    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {

    }

    /**
     * Получение администратором последних опубликованных комментариев постранично
     *
     * @param from    количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size    количество элементов в наборе
     * @param eventId поиск по событию
     * @param text    поиск по тексту комментария
     * @return список комментариев, сначала новые
     */
    @Override
    public List<AdminCommentDto> searchCommentsByAdmin(Integer from, Integer size, Integer eventId, String text) {
        return List.of();
    }

    /**
     * Изменение (модерация) комментария администратором
     *
     * @param commentId        идентификатор комментария
     * @param updateCommentDto Новые текст комментария
     * @return обновленный комментарий
     */
    @Override
    public CommentDto moderateCommentByAdmin(Long commentId, UpdateCommentDto updateCommentDto) {
        return null;
    }

    /**
     * Удаление комментария администратором
     *
     * @param commentId идентификатор комментария
     */
    @Override
    public void deleteCommentByAdmin(Long commentId) {

    }
}
