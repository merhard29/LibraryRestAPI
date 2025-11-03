package de.moritzerhard.libraryrestapi.service;

import de.moritzerhard.libraryrestapi.dto.request.LoginRequest;
import de.moritzerhard.libraryrestapi.dto.response.LoginResponse;
import de.moritzerhard.libraryrestapi.entity.CustomerEntity;
import de.moritzerhard.libraryrestapi.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/**
 * Service responsible for authenticating users and generating JWT tokens.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtEncoder jwtEncoder;

  /**
   * Authenticates a user using the provided credentials and returns a JWT token.
   *
   * @param request the login credentials containing email and password
   * @return a {@link LoginResponse} containing the generated JWT token
   * @throws EntityNotFoundException  if the user is not found or credentials are invalid
   * @throws IllegalArgumentException if the password does not match
   */
  public LoginResponse login(LoginRequest request) {
    CustomerEntity customer = customerRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new EntityNotFoundException("Invalid credentials"));

    if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    Instant now = Instant.now();
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .subject(customer.getEmail())
        .issuedAt(now)
        .expiresAt(now.plusSeconds(3600))
        .build();

    JwsHeader header = JwsHeader.with(() -> "HS256").build();

    String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    return new LoginResponse(token);
  }

}
