package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.AdminCommentDto;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.EventStatus;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.CommentService;

import java.time.LocalDateTime;
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
        User author = getUserById(userId);
        Event event = getEventById(commentDto.getEventId());

        if (event.getEventStatus() != EventStatus.PUBLISHED) {
            throw new ConflictException("You can add comment only to published events");
        }

        Comment comment = Comment.builder()
                .author(author)
                .event(event)
                .created(LocalDateTime.now())
                .lastUpdatedOn(LocalDateTime.now())
                .text(commentDto.getText())
                .build();
        return toDto(repository.save(comment));
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
        Comment comment = getCommentById(commentId);
        User user = getUserById(userId);

        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new ConflictException("Only author can change comment");
        }

        if (updateCommentDto.getText() != null) {
            comment.setText(updateCommentDto.getText());
            comment = repository.save(comment);
        }

        return toDto(comment);
    }

    /**
     * Удаление комментария добавленного текущим пользователем
     *
     * @param userId идентификатор пользователя
     * @param commentId идентификатор комментария
     */
    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        Comment comment = getCommentById(commentId);
        User user = getUserById(userId);

        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new ConflictException("Only author can delete comment");
        }

        repository.deleteById(commentId);
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
        Comment comment = getCommentById(commentId);

        if (updateCommentDto.getText() != null) {
            comment.setText(updateCommentDto.getText());
            comment = repository.save(comment);
        }

        return toDto(comment);
    }

    /**
     * Удаление комментария администратором
     *
     * @param commentId идентификатор комментария
     */
    @Override
    public void deleteCommentByAdmin(Long commentId) {
        getCommentById(commentId);
        repository.deleteById(commentId);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    private Comment getCommentById(Long userId) {
        return repository.findById(userId).orElseThrow(
                () -> new NotFoundException("Comment with id=" + userId + " was not found"));
    }

    CommentDto toDto(Comment comment) {
        return mapper.map(comment, CommentDto.class);
    }

    AdminCommentDto toAdminDto(Comment comment) {
        return mapper.map(comment, AdminCommentDto.class);
    }
}
