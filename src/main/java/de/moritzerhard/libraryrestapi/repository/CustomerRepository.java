package de.moritzerhard.libraryrestapi.repository;

import de.moritzerhard.libraryrestapi.entity.CustomerEntity;
import jakarta.validation.constraints.Email;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link CustomerEntity} persistence operations.
 */
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
  /**
   * Finds a customer by their email address.
   *
   * @param email the email address to search for
   * @return an Optional containing the found CustomerEntity, or empty if not found
   */
  Optional<CustomerEntity> findByEmail(@Email String email);
}
