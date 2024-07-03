package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.utils.ChunkRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final ModelMapper mapper = new ModelMapper();

    /**
     * Добавление новой категории
     * <p>
     * Обратите внимание: имя категории должно быть уникальным
     *
     * @param newCategoryDto данные добавляемой категории
     * @return добавленная категория с id
     */
    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        checkForUniqueName(newCategoryDto.getName());
        return toDto(repository.save(toModel(newCategoryDto)));
    }

    /**
     * Удаление категории
     *
     * @param catId id категории
     */
    @Override
    public void deleteCategory(Long catId) {
        // TODO добавить проверку "с категорией не должно быть связано ни одного события"
        repository.deleteById(catId);
    }

    /**
     * Изменение категории
     * <p>
     * Обратите внимание: имя категории должно быть уникальным
     *
     * @param catId       id категории
     * @param categoryDto данные категории для изменения
     * @return обновленная категория
     */
    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category saved = findById(catId);
        if (!categoryDto.getName().equals(saved.getName())) {
            checkForUniqueName(categoryDto.getName());
        }
        categoryDto.setId(catId);
        return toDto(repository.save(toModel(categoryDto)));
    }

    /**
     * Получение категорий
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return список категорий
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список
     */
    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable page = new ChunkRequest(from, size);
        return repository.findAll(page).map(this::toDto).toList();
    }

    /**
     * Получение информации о категории по её идентификатору
     *
     * @param catId идентификатор категории
     * @return категория
     */
    @Override
    public CategoryDto getCategory(Long catId) {
        return toDto(findById(catId));
    }

    private void checkForUniqueName(String name) {
        if (repository.existsByNameIgnoreCase(name)) {
            throw new ConflictException("Category with name=" + name + " already exists.");
        }
    }

    private Category findById(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }

    private CategoryDto toDto(Category category) {
        return mapper.map(category, CategoryDto.class);
    }

    private Category toModel(CategoryDto dto) {
        return mapper.map(dto, Category.class);
    }

    private Category toModel(NewCategoryDto dto) {
        return mapper.map(dto, Category.class);
    }
}
