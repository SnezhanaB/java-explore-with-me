package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

import java.util.List;

/**
 * Сервис для работы с категориями
 */
public interface CategoryService {

    /**
     * Добавление новой категории
     * <p>
     * Обратите внимание: имя категории должно быть уникальным
     * @param newCategoryDto данные добавляемой категории
     * @return добавленная категория с id
     */
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    /**
     * Удаление категории
     * @param catId id категории
     */
    void deleteCategory(Long catId);

    /**
     * Изменение категории
     * <p>
     * Обратите внимание: имя категории должно быть уникальным
     * @param catId id категории
     * @param categoryDto данные категории для изменения
     * @return обновленная категория
     */
    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

    /**
     * Получение категорий
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return список категорий
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список
     */
    List<CategoryDto> getCategories(Integer from, Integer size);

    /**
     * Получение информации о категории по её идентификатору
     * @param catId идентификатор категории
     * @return категория
     */
    CategoryDto getCategory(Long catId);

}
