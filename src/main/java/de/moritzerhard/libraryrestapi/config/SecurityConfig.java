package de.moritzerhard.libraryrestapi.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Main Spring Security configuration for the Library REST API.
 * This class defines access rules for public and secured endpoints,
 * configures stateless JWT-based authentication, and provides
 * beans for encoding/decoding JWT tokens and passwords.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  /**
   * Base64-encoded secret key used for signing and verifying JWTs.
   */
  @Value("${jwt.secret}")
  private String secret;

  /**
   * Defines the main HTTP security filter chain.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Disable CSRF because we use JWT (no session state)
        .csrf(AbstractHttpConfigurer::disable)

        // Stateless session policy: no HTTP sessions are created or used
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // Return HTTP 401 (Unauthorized) for unauthenticated access attempts
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        )

        // Define access rules for endpoints
        .authorizeHttpRequests(auth -> auth
            // Allow Swagger UI and API documentation (this should be restricted in production)
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

            // Allow authentication endpoints (login)
            .requestMatchers("/api/v1/auth/**").permitAll()

            // Allow anonymous GET access to books and categories
            .requestMatchers(HttpMethod.GET, "/api/v1/books/**", "/api/v1/categories/**").permitAll()

            // Require authentication for write operations on books and categories
            .requestMatchers("/api/v1/books/**", "/api/v1/categories/**").authenticated()

            // Require authentication for all customer-related operations
            .requestMatchers("/api/v1/customers/**").authenticated()

            // Any other request must be authenticated
            .anyRequest().authenticated()
        )

        // Enable JWT validation using Spring's OAuth2 resource server support
        .oauth2ResourceServer(oauth2 ->
            oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()))
        );

    return http.build();
  }

  /**
   * Configures a JWT encoder for signing tokens using the symmetric secret key.
   * Uses Nimbus JOSE implementation to produce HS256-signed tokens.
   */
  @Bean
  public JwtEncoder jwtEncoder() {
    byte[] keyBytes = Base64.getDecoder().decode(secret);
    OctetSequenceKey jwk = new OctetSequenceKey.Builder(keyBytes)
        .algorithm(JWSAlgorithm.HS256)
        .keyUse(com.nimbusds.jose.jwk.KeyUse.SIGNATURE)
        .build();
    ImmutableJWKSet<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwkSource);
  }

  /**
   * Configures a JWT decoder for verifying tokens using the same symmetric secret key.
   * Ensures that all incoming tokens are signed with the correct key and algorithm.
   */
  @Bean
  public JwtDecoder jwtDecoder() {
    byte[] keyBytes = Base64.getDecoder().decode(secret);
    return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(keyBytes, "HmacSHA256"))
        .build();
  }

  /**
   * Provides a BCrypt password encoder for securely hashing user passwords.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
