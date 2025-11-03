package de.moritzerhard.libraryrestapi.dto.response;

import lombok.Data;

/**
 * Data Transfer Object for Customer response.
 */
@Data
public class CustomerResponse {
  private Long id;
  private String name;
  private String email;
}
