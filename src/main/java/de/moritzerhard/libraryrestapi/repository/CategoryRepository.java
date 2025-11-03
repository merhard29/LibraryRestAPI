package de.moritzerhard.libraryrestapi.repository;

import de.moritzerhard.libraryrestapi.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link CategoryEntity} persistence operations.
 */
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
