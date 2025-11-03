package de.moritzerhard.libraryrestapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for user login requests.
 */
@Data
public class LoginRequest {
  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String password;
}
