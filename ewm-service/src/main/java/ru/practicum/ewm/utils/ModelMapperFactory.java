package ru.practicum.ewm.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;

public class ModelMapperFactory {
    public static ModelMapper create() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<EventFullDto, Event> eventTypeMap = modelMapper.createTypeMap(EventFullDto.class, Event.class);
        eventTypeMap.addMapping(EventFullDto::getState, Event::setEventStatus);

        TypeMap<Event, EventFullDto> eventDtoTypeMap = modelMapper.createTypeMap(Event.class, EventFullDto.class);
        eventDtoTypeMap.addMapping(Event::getEventStatus, EventFullDto::setState);

        TypeMap<Request, ParticipationRequestDto> requestDtoTypeMap = modelMapper.createTypeMap(Request.class,
                ParticipationRequestDto.class);
        requestDtoTypeMap.addMappings(
                mapper -> mapper.map(request -> request.getRequester().getId(),
                ParticipationRequestDto::setRequester)
        );

        return modelMapper;
    }
}
