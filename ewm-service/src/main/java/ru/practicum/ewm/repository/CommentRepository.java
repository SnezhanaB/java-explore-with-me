package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c " +
            "from comments as c " +
            "where lower(c.text) like lower(concat('%', ?1, '%')) and c.event.id=?2")
    Page<Comment> searchByTextAndEventId(String text, Long eventId, Pageable pageable);

    @Query("select c " +
            "from comments as c " +
            "where lower(c.text) like lower(concat('%', ?1, '%'))")
    Page<Comment> searchByText(String text, Pageable pageable);

    Page<Comment> findAllByEventId(Long eventId, Pageable pageable);
}
