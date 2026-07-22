package com.salman.week1.repository;

import com.salman.week1.model.entity.Book;
import com.salman.week1.model.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query("""
            SELECT b
            FROM Book b
            WHERE (:status IS NULL OR b.status = :status)
            """)
    Page<Book> findAllByStatus(@Param("status") BookStatus status, Pageable pageable);

    Optional<Book> findByIdAndStatus(UUID id, BookStatus status);
}
