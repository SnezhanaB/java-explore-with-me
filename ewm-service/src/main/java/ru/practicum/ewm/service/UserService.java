package ru.practicum.ewm.service;

import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;

import java.util.List;

public interface UserService {

    /**
     * Получение списка пользователей
     * @param ids пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return Возвращает информацию обо всех пользователях (учитываются параметры ограничения
     *  выборки), либо о конкретных (учитываются указанные идентификаторы)
     *  <p>
     *  В случае, если по заданным фильтрам не найдено ни одного пользователя,
     *  возвращает пустой список
     */
    @GetMapping
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    /**
     * Добавление нового пользователя
     * @param newUserRequest Данные добавляемого пользователя
     * @return Данные созданного пользователя с id
     */
    UserDto addUser(NewUserRequest newUserRequest);

    /**
     * Удаление пользователя
     * @param userId идентификатор пользователя
     */
    void deleteUser(Long userId);
}
