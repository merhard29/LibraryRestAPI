package de.moritzerhard.libraryrestapi.controller;

import de.moritzerhard.libraryrestapi.api.CustomerControllerDefinition;
import de.moritzerhard.libraryrestapi.dto.request.CustomerRequest;
import de.moritzerhard.libraryrestapi.dto.response.CustomerResponse;
import de.moritzerhard.libraryrestapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of the customer controller for managing customer-related endpoints.
 */
@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerControllerDefinition {
  private final CustomerService customerService;

  @Override
  public ResponseEntity<CustomerResponse> registerCustomer(CustomerRequest request) {
    CustomerResponse response = customerService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<CustomerResponse> getCustomerById(Long id) {
    return ResponseEntity.ok(customerService.getById(id));
  }

  @Override
  public ResponseEntity<CustomerResponse> updateCustomer(Long id, CustomerRequest request) {
    return ResponseEntity.ok(customerService.update(id, request));
  }

  @Override
  public ResponseEntity<Void> deleteCustomer(Long id) {
    customerService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
