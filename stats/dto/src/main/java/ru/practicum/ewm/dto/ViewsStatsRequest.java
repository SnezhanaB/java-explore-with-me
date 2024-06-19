package ru.practicum.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
public class ViewsStatsRequest {
    /**
     * Дата и время начала диапазона за который нужно выгрузить статистику (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @Builder.Default
    private LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
    /**
     * Дата и время конца диапазона за который нужно выгрузить статистику (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @Builder.Default
    private LocalDateTime end = LocalDateTime.now();
    /**
     * Список uri для которых нужно выгрузить статистику
     */
    private List<String> uris;
    /**
     * Нужно ли учитывать только уникальные посещения (только с уникальным ip)
     */
    private boolean unique;
    /**
     * Название сервиса
     */
    private String application;
}
