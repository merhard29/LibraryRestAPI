package de.moritzerhard.libraryrestapi.service;

import de.moritzerhard.libraryrestapi.dto.request.CustomerRequest;
import de.moritzerhard.libraryrestapi.dto.response.CustomerResponse;
import de.moritzerhard.libraryrestapi.entity.CustomerEntity;
import de.moritzerhard.libraryrestapi.mapper.CustomerMapper;
import de.moritzerhard.libraryrestapi.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for managing customers, including registration, retrieval, update, and deletion.
 * Provides authorization checks to ensure that users can only access or modify their own data.
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;
  private final PasswordEncoder passwordEncoder;

  /**
   * Creates a new customer account.
   *
   * @param request the request containing customer details and password
   * @return the created {@link CustomerResponse}
   * @throws IllegalArgumentException if an account with the same email already exists
   */
  public CustomerResponse register(CustomerRequest request) {
    if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already registered: " + request.getEmail());
    }

    CustomerEntity entity = customerMapper.toEntity(request);
    entity.setPassword(passwordEncoder.encode(request.getPassword()));
    return customerMapper.toResponse(customerRepository.save(entity));
  }

  /**
   * Retrieves a customer by ID.
   *
   * @param id the ID of the customer
   * @return the corresponding {@link CustomerResponse}
   * @throws EntityNotFoundException if the customer does not exist
   */
  public CustomerResponse getById(Long id) {
    CustomerEntity entity = customerRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
    return customerMapper.toResponse(entity);
  }

  /**
   * Updates a customer's information.
   *
   * @param id      the ID of the customer to update
   * @param request the updated customer details
   * @return the updated {@link CustomerResponse}
   * @throws EntityNotFoundException if the customer does not exist
   */
  public CustomerResponse update(Long id, CustomerRequest request) {
    CustomerEntity entity = customerRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

    entity.setName(request.getName());
    entity.setEmail(request.getEmail());
    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      entity.setPassword(passwordEncoder.encode(request.getPassword()));
    }
    return customerMapper.toResponse(customerRepository.save(entity));
  }

  /**
   * Deletes a customer by ID.
   *
   * @param id the ID of the customer to delete
   * @throws EntityNotFoundException if the customer does not exist
   */
  public void delete(Long id) {
    if (!customerRepository.existsById(id)) {
      throw new EntityNotFoundException("Customer not found with id: " + id);
    }
    customerRepository.deleteById(id);
  }
}
