package de.moritzerhard.libraryrestapi.api;

import de.moritzerhard.libraryrestapi.dto.request.CustomerRequest;
import de.moritzerhard.libraryrestapi.dto.response.CustomerResponse;
import de.moritzerhard.libraryrestapi.utils.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Defines the REST API endpoints for managing customer accounts.
 */
@Tag(name = "Customers", description = "Endpoints for managing customers (authentication required)")
@RequestMapping(RestConstants.CUSTOMERS)
public interface CustomerControllerDefinition {
  /**
   * Registers a new customer (requires authentication).
   *
   * @param request the customer registration details
   * @return a {@link ResponseEntity} containing the created {@link CustomerResponse}
   */
  @Operation(
      summary = "Register a new customer",
      description = "Requires JWT authentication.",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid customer data"),
      @ApiResponse(responseCode = "409", description = "Email already exists")
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<CustomerResponse> registerCustomer(@RequestBody CustomerRequest request);

  /**
   * Retrieves a customer's information by ID (Requires authentication).
   *
   * @param id the ID of the customer to retrieve
   * @return a {@link ResponseEntity} containing the {@link CustomerResponse}
   */
  @Operation(
      summary = "Get customer by ID",
      description = "Requires JWT authentication.",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized – JWT token missing or invalid"),
      @ApiResponse(responseCode = "404", description = "Customer not found")
  })
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<CustomerResponse> getCustomerById(
      @Parameter(description = "ID of the customer", required = true)
      @PathVariable Long id);

  /**
   * Updates a customer's information (Requires authentication).
   *
   * @param id      the ID of the customer to update
   * @param request the updated customer data
   * @return a {@link ResponseEntity} containing the updated {@link CustomerResponse}
   */
  @Operation(
      summary = "Update customer information",
      description = "Requires JWT authentication.",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized – JWT token missing or invalid"),
      @ApiResponse(responseCode = "404", description = "Customer not found")
  })
  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<CustomerResponse> updateCustomer(
      @Parameter(description = "ID of the customer to update", required = true)
      @PathVariable Long id,
      @RequestBody CustomerRequest request);

  /**
   * Deletes a customer by ID (Requires authentication).
   *
   * @param id the ID of the customer to delete
   * @return an empty {@link ResponseEntity} with status 204 if successful
   */
  @Operation(
      summary = "Delete a customer",
      description = "Requires JWT authentication.",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized – JWT token missing or invalid"),
      @ApiResponse(responseCode = "404", description = "Customer not found")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteCustomer(
      @Parameter(description = "ID of the customer to delete", required = true)
      @PathVariable Long id);
}
