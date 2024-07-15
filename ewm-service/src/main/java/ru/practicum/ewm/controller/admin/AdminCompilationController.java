package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationDto;
import ru.practicum.ewm.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * Admin: Подборки событий
 * <p>
 * API для работы с подборками событий
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService service;

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     * @param compilationDto данные новой подборки
     * @return созданная подборка с id
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {
        log.info("[POST /admin/compilations] создание подборки событий {}", compilationDto);
        return service.addCompilation(compilationDto);
    }

    /**
     * Обновить информацию о подборке
     * @param updateDto данные для обновления подборки
     * @param compId id подборки
     * @return обновленная подборка
     */
    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@RequestBody @Valid UpdateCompilationDto updateDto,
                                            @PathVariable Long compId) {
        log.info("[PATCH /admin/compilations/{}] обновление подборки событий {}", compId, updateDto);
        return service.updateCompilation(updateDto, compId);
    }

    /**
     * Удаление подборки
     * @param compId id подборки
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Long compId) {
        log.info("[DELETE /admin/compilations/{}] удаление подборки событий", compId);
        service.deleteCompilation(compId);
    }

}
