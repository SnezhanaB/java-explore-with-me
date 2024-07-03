package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * Admin: Категории
 * <p>
 * API для работы с категориями
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {

    private CategoryService service;

    /**
     * Добавление новой категории
     * <p>
     * Обратите внимание: имя категории должно быть уникальным
     * @param newCategoryDto данные добавляемой категории
     * @return добавленная категория с id
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("[POST /admin/categories] создание новой категории: {}", newCategoryDto);
        return service.addCategory(newCategoryDto);
    }

    /**
     * Удаление категории
     * <p>
     * Обратите внимание: с категорией не должно быть связано ни одного события
     * @param catId id категории
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive Long catId) {
        log.info("[DELETE /admin/categories/{}] удалении категории", catId);
        service.deleteCategory(catId);
    }

    /**
     * Изменение категории
     * <p>
     * Обратите внимание: имя категории должно быть уникальным
     * @param catId id категории
     * @param categoryDto данные категории для изменения
     * @return обновленная категория
     */
    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable(value = "catId") @Positive Long catId,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        log.info("[PATCH /admin/categories/{}] обновление категории {}", catId, categoryDto);
        return service.updateCategory(catId, categoryDto);
    }
}
