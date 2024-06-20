package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.ViewsStatsRequest;

import java.util.List;

public interface StatsService {

    void addHit(EndpointHitDto hit);

    List<ViewStatsDto> viewStats(ViewsStatsRequest request);

}
