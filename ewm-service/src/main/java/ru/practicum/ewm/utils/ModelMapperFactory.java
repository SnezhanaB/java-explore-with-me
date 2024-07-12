package ru.practicum.ewm.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.model.Event;

public class ModelMapperFactory {
    public static ModelMapper create() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<EventFullDto, Event> eventTypeMap = mapper.createTypeMap(EventFullDto.class, Event.class);
        eventTypeMap.addMapping(EventFullDto::getState, Event::setEventStatus);
        TypeMap<Event, EventFullDto> eventDtoTypeMap = mapper.createTypeMap(Event.class, EventFullDto.class);
        eventDtoTypeMap.addMapping(Event::getEventStatus, EventFullDto::setState);
        return mapper;
    }
}
