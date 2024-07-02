package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.utils.ChunkRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {

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
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("[GET /admin/users] Получение списка пользователей, ids={}, from={}, size={}", ids, from, size);
        Pageable page = new ChunkRequest(from, size, null);

        // TODO
        return Collections.emptyList();
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
        // TODO
        return null;
    }

    /**
     * Удаление пользователя
     * @param userId идентификатор пользователя
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        log.info("[DELETE /admin/users] Удаление пользователя с id={}", userId);
        // TODO
    }
}
