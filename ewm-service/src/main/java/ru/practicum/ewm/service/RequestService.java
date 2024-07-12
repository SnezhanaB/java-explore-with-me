package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
     * @param userId идентификатор пользователя
     * @return список заявок на участие в событии
     */
    List<ParticipationRequestDto> getAllRequests(Long userId);

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     * @param userId идентификатор пользователя
     * @param eventId идентификатор события
     * @return заявка на участие в событии
     */
    ParticipationRequestDto addRequest(Long userId, Long eventId);

    /**
     * Отмена своего запроса на участие в событии
     * @param userId идентификатор пользователя
     * @param requestId идентификатор запроса на участие
     * @return Заявка на участие в событии
     */
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
