package ru.practicum.ewm.service.impl;

import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

public class CompilationServiceImpl implements CompilationService {
    /**
     * Получение подборок событий
     *
     * @param pinned искать только закрепленные/не закрепленные подборки
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   количество элементов в наборе
     * @return список подборок событий
     * <p>
     * В случае, если по заданным фильтрам не найдено ни одной подборки, возвращает пустой список
     */
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        return List.of();
    }

    /**
     * Получение подборки событий по его id
     *
     * @param compId id подборки
     * @return подборка событий
     * <p>
     * В случае, если подборки с заданным id не найдено, возвращает статус код 404
     */
    @Override
    public CompilationDto findByIdCompilation(Long compId) {
        return null;
    }

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     *
     * @param compilationDto данные новой подборки
     * @return созданная подборка с id
     */
    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        return null;
    }

    /**
     * Обновить информацию о подборке
     *
     * @param updateDto данные для обновления подборки
     * @param compId    id подборки
     * @return обновленная подборка
     */
    @Override
    public CompilationDto updateCompilation(NewCompilationDto updateDto, Long compId) {
        return null;
    }

    /**
     * Удаление подборки
     *
     * @param compId id подборки
     */
    @Override
    public void deleteCompilation(Long compId) {

    }
}
