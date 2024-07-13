package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    /**
     * Получение подборок событий
     * @param pinned искать только закрепленные/не закрепленные подборки
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return список подборок событий
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одной подборки, возвращает пустой список
     */
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    /**
     * Получение подборки событий по его id
     * @param compId id подборки
     * @return подборка событий
     * <p>
     * В случае, если подборки с заданным id не найдено, возвращает статус код 404
     */
    CompilationDto findByIdCompilation(Long compId);

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     * @param compilationDto данные новой подборки
     * @return созданная подборка с id
     */
    CompilationDto addCompilation(NewCompilationDto compilationDto);

    /**
     * Обновить информацию о подборке
     * @param updateDto данные для обновления подборки
     * @param compId id подборки
     * @return обновленная подборка
     */
    CompilationDto updateCompilation(NewCompilationDto updateDto, Long compId);

    /**
     * Удаление подборки
     * @param compId id подборки
     */
    void deleteCompilation(Long compId);
}
