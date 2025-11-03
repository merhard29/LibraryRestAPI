package de.moritzerhard.libraryrestapi.utils;

import java.time.Instant;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

public final class JwtTestUtils {

  public static String generateBearerToken(JwtEncoder encoder, String subject) {
    try {
      Instant now = Instant.now();
      JwtClaimsSet claims = JwtClaimsSet.builder()
          .subject(subject)
          .issuedAt(now)
          .expiresAt(now.plusSeconds(3600))
          .build();

      JwsHeader header = JwsHeader.with(() -> "HS256").build();

      String tokenValue = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
      return "Bearer " + tokenValue;
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate JWT: " + e.getMessage(), e);
    }
  }
}
