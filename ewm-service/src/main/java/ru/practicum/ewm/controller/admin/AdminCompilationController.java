package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;

import javax.validation.Valid;

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

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     * @param compilationDto данные новой подборки
     * @return созданная подборка с id
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto compilationDto) {
        log.info("[POST /admin/compilations] создание подборки событий {}", compilationDto);
        // TODO
        return null;
    }

    /**
     * Обновить информацию о подборке
     * @param updateDto данные для обновления подборки
     * @param compId id подборки
     * @return обновленная подборка
     */
    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@RequestBody @Valid NewCompilationDto updateDto,
                                            @PathVariable Integer compId) {
        log.info("[PATCH /admin/compilations/{}] обновление подборки событий {}", compId, updateDto);
        // TODO
        return null;
    }

    /**
     * Удаление подборки
     * @param compId id подборки
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        log.info("[DELETE /admin/compilations/{}] удаление подборки событий", compId);
        // TODO
    }

}
