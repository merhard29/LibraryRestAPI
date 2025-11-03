package de.moritzerhard.libraryrestapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for Login response.
 */
@Data
@AllArgsConstructor
public class LoginResponse {
  private String token;
}
