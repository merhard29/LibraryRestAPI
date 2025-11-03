package de.moritzerhard.libraryrestapi.repository;

import de.moritzerhard.libraryrestapi.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link BookEntity} persistence operations.
 */
public interface BookRepository extends JpaRepository<BookEntity, Long> {
}
