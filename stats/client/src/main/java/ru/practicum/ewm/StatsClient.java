package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Service
public class StatsClient extends BaseClient {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    /**
     * Сохранение захода в базу.
     */
    public void addHit(EndpointHitDto endpointHitDto) {
        post("/hit", endpointHitDto);
    }

    /**
     * Получение статистики заходов за определенный период.
     */
    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris,
                                           Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(dateTimeFormatter),
                "end", end.format(dateTimeFormatter),
                "uris", String.join(",", uris),
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
