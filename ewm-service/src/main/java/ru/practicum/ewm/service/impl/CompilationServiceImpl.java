package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.utils.ChunkRequest;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final ModelMapper mapper;

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
        Pageable page = new ChunkRequest(from, size);
        if (pinned != null) {
            return repository.findAllByPinned(pinned, page).map(this::toDto).toList();
        }
        return repository.findAll(page).map(this::toDto).toList();
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
        return toDto(getCompilationById(compId));
    }

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     *
     * @param compilationDto данные новой подборки
     * @return созданная подборка с id
     */
    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
        Compilation compilation = Compilation.builder()
                .events(new HashSet<>(events))
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();

        return toDto(repository.save(compilation));
    }

    /**
     * Обновить информацию о подборке
     *
     * @param updateDto данные для обновления подборки
     * @param compId    id подборки
     * @return обновленная подборка
     */
    @Override
    public CompilationDto updateCompilation(UpdateCompilationDto updateDto, Long compId) {
        Compilation compilation = getCompilationById(compId);

        if (updateDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateDto.getEvents());
            compilation.setEvents(new HashSet<>(events));
        }

        if (updateDto.getPinned() != null) {
            compilation.setPinned(updateDto.getPinned());
        }

        if (updateDto.getTitle() != null && !updateDto.getTitle().isBlank()) {
            compilation.setTitle(updateDto.getTitle());
        }

        return toDto(repository.save(compilation));
    }

    /**
     * Удаление подборки
     *
     * @param compId id подборки
     */
    @Override
    public void deleteCompilation(Long compId) {
        getCompilationById(compId);
        repository.deleteById(compId);
    }

    private Compilation getCompilationById(Long compilationId) {
        return repository.findById(compilationId).orElseThrow(
                () -> new NotFoundException("Compilation with id=" + compilationId + " was not found"));
    }


    private CompilationDto toDto(Compilation compilation) {
        return mapper.map(compilation, CompilationDto.class);
    }
}
