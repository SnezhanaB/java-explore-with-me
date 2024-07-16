package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c " +
            "from comments as c " +
            "where lower(c.text) like lower(concat('%', ?1, '%') ) and c.event.id in (?2)")
    Page<Comment> search(String text, List<Long> eventIds, Pageable pageable);

    Page<Comment> findAllByEventId(Long eventId, Pageable pageable);
}
