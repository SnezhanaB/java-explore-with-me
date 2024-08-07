package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Admin: Пользователи
 * <p>
 * API для работы с пользователями
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController {

    private final UserService service;

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
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("[GET /admin/users] Получение списка пользователей, ids={}, from={}, size={}", ids, from, size);

        return service.getUsers(ids, from, size);
    }

    /**
     * Добавление нового пользователя
     * @param newUserRequest Данные добавляемого пользователя
     * @return Данные созданного пользователя с id
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("[POST /admin/users] Добавление нового пользователя {}", newUserRequest);
        return service.addUser(newUserRequest);
    }

    /**
     * Удаление пользователя
     * @param userId идентификатор пользователя
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive Long userId) {
        log.info("[DELETE /admin/users] Удаление пользователя с id={}", userId);
        service.deleteUser(userId);
    }
}
