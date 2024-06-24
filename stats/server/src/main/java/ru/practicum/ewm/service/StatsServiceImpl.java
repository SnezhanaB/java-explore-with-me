package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.ViewsStatsRequest;
import ru.practicum.ewm.exception.DataValidationException;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.repository.StatsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public void addHit(EndpointHitDto hit) {
        repository.save(mapper.map(hit, EndpointHit.class));
    }

    @Override
    public List<ViewStatsDto> viewStats(ViewsStatsRequest request) {
        if (request.getEnd().isBefore(request.getStart())) {
            throw new DataValidationException("Дата окончания должна быть позже даты начала");
        }

        if (request.isUnique()) {
            return repository.findAllUnique(
                    request.getStart(),
                    request.getEnd(),
                    request.getUris());
        }
        return repository.findAll(
                request.getStart(),
                request.getEnd(),
                request.getUris());
    }
}
