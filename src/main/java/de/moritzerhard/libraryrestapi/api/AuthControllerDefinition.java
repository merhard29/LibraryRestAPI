package de.moritzerhard.libraryrestapi.api;

import de.moritzerhard.libraryrestapi.dto.request.LoginRequest;
import de.moritzerhard.libraryrestapi.dto.response.LoginResponse;
import de.moritzerhard.libraryrestapi.utils.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Defines the authentication API endpoints for user login and JWT token generation.
 */
@Tag(name = "Authentication", description = "Endpoints for user authentication and jwt generation")
@RequestMapping(RestConstants.AUTH)
public interface AuthControllerDefinition {
  /**
   * Authenticates a user and returns a JWT access token.
   *
   * @param loginRequest the login credentials (email and password)
   * @return a {@link ResponseEntity} containing the JWT token if authentication succeeds
   */
  @Operation(
      summary = "Login endpoint",
      description = "Authenticates a user by email and password, returning a JWT token if successful.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "Login credentials (email and password)"
      )
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Successful authentication, returns JWT token"),
      @ApiResponse(responseCode = "401", description = "Unauthorized, invalid email or password")
  })
  @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest);

}
