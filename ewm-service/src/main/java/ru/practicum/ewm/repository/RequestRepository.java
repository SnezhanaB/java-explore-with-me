package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.enums.RequestStatus;

import java.util.List;
import java.util.Optional;


public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long userId);

    int countByEventIdAndStatus(Long eventId, RequestStatus status);

    Boolean existsByEventIdAndRequesterId(Long eventId, Long userId);

    Optional<Request> findByIdAndRequesterId(Long id, Long requesterId);
}
