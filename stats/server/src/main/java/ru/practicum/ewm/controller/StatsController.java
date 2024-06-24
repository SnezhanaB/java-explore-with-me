package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.ViewsStatsRequest;
import ru.practicum.ewm.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void hit(@RequestBody EndpointHitDto hit) {
        log.info("POST /hit request=({})", hit);
        statsService.addHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(defaultValue = "") List<String> uris,
                                       @RequestParam(defaultValue = "false") boolean unique) {

        ViewsStatsRequest request = ViewsStatsRequest.builder()
                .start(start)
                .end(end)
                .uris(uris)
                .unique(unique)
                .build();

        log.info("GET /stats request=({})", request);

        return statsService.viewStats(request);
    }

}
