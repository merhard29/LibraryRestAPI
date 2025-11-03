package de.moritzerhard.libraryrestapi.controller;

import de.moritzerhard.libraryrestapi.api.AuthControllerDefinition;
import de.moritzerhard.libraryrestapi.dto.request.LoginRequest;
import de.moritzerhard.libraryrestapi.dto.response.LoginResponse;
import de.moritzerhard.libraryrestapi.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of the authentication controller for handling login requests.
 */
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerDefinition {
  private final AuthService authService;

  @Override
  public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
    try {
      LoginResponse response = authService.login(loginRequest);
      return ResponseEntity.ok(response);
    } catch (EntityNotFoundException | IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
