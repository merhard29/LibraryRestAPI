package de.moritzerhard.libraryrestapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.moritzerhard.libraryrestapi.dto.request.LoginRequest;
import de.moritzerhard.libraryrestapi.entity.CustomerEntity;
import de.moritzerhard.libraryrestapi.repository.CustomerRepository;
import de.moritzerhard.libraryrestapi.utils.RestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

  private static final String LOGIN_URL = RestConstants.AUTH + "/login";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    CustomerEntity user = new CustomerEntity();
    user.setName("Test User");
    user.setEmail("test@example.com");
    user.setPassword(passwordEncoder.encode("password123"));
    customerRepository.save(user);
  }

  @Test
  @DisplayName("Successful login returns JWT token")
  void successfulLoginReturnsToken() throws Exception {
    LoginRequest request = new LoginRequest();
    request.setEmail("test@example.com");
    request.setPassword("password123");

    mockMvc.perform(post(LOGIN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.token").exists());
  }

  @Test
  @DisplayName("Invalid password returns 401 Unauthorized")
  void invalidPasswordReturnsUnauthorized() throws Exception {
    LoginRequest request = new LoginRequest();
    request.setEmail("test@example.com");
    request.setPassword("wrongPassword");

    mockMvc.perform(post(LOGIN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Invalid email returns 401 Unauthorized")
  void unknownEmailReturnsUnauthorized() throws Exception {
    LoginRequest request = new LoginRequest();
    request.setEmail("unknown@example.com");
    request.setPassword("password123");

    mockMvc.perform(post(LOGIN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }
}
