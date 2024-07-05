package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.UserService;
import ru.practicum.ewm.utils.ChunkRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ModelMapper mapper = new ModelMapper();

    /**
     * Получение списка пользователей
     *
     * @param ids  пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return Возвращает информацию обо всех пользователях (учитываются параметры ограничения
     * выборки), либо о конкретных (учитываются указанные идентификаторы)
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одного пользователя,
     * возвращает пустой список
     */
    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable page = new ChunkRequest(from, size);
        if (ids != null) return repository.findByIdIn(ids, page).map(this::toDto).toList();

        return repository.findAll(page).map(this::toDto).toList();
    }

    /**
     * Добавление нового пользователя
     *
     * @param newUserRequest Данные добавляемого пользователя
     * @return Данные созданного пользователя с id
     */
    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        return toDto(repository.save(toModel(newUserRequest)));
    }

    /**
     * Удаление пользователя
     *
     * @param userId идентификатор пользователя
     */
    @Override
    public void deleteUser(Long userId) {
        checkById(userId);
        repository.deleteById(userId);

    }

    private void checkById(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("User with id=" + id + " was not found");
        }
    }

    private UserDto toDto(User user) {
        return mapper.map(user, UserDto.class);
    }

    private User toModel(NewUserRequest dto) {
        return mapper.map(dto, User.class);
    }

}
