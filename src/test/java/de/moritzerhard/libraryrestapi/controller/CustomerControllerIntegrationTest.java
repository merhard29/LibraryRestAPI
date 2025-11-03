package de.moritzerhard.libraryrestapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.moritzerhard.libraryrestapi.dto.request.CustomerRequest;
import de.moritzerhard.libraryrestapi.entity.CustomerEntity;
import de.moritzerhard.libraryrestapi.repository.CustomerRepository;
import de.moritzerhard.libraryrestapi.utils.JwtTestUtils;
import de.moritzerhard.libraryrestapi.utils.RestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CustomerControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtEncoder jwtEncoder;

  private CustomerEntity existingCustomer;
  private String validToken;

  @BeforeEach
  void setUp() {
    existingCustomer = new CustomerEntity();
    existingCustomer.setName("John Doe");
    existingCustomer.setEmail("john@example.com");
    existingCustomer.setPassword(passwordEncoder.encode("password123"));
    customerRepository.save(existingCustomer);

    validToken = JwtTestUtils.generateBearerToken(jwtEncoder, existingCustomer.getEmail());
  }

  @Test
  @DisplayName("Authenticated user can register a new customer (201 Created)")
  void authenticatedUserCanRegisterCustomer() throws Exception {
    CustomerRequest request = new CustomerRequest();
    request.setName("New User");
    request.setEmail("newuser@example.com");
    request.setPassword("newpassword123");

    mockMvc.perform(post(RestConstants.CUSTOMERS)
            .header("Authorization", validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.email").value("newuser@example.com"))
        .andExpect(jsonPath("$.name").value("New User"));
  }

  @Test
  @DisplayName("Unauthenticated request to create customer returns 401 Unauthorized")
  void unauthenticatedCreateCustomerReturnsUnauthorized() throws Exception {
    CustomerRequest request = new CustomerRequest();
    request.setName("Unauth User");
    request.setEmail("unauth@example.com");
    request.setPassword("test123");

    mockMvc.perform(post(RestConstants.CUSTOMERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Authenticated user can retrieve any customer (200 OK)")
  void authenticatedUserCanGetAnyCustomer() throws Exception {
    CustomerEntity another = new CustomerEntity();
    another.setName("Jane Doe");
    another.setEmail("jane@example.com");
    another.setPassword(passwordEncoder.encode("password123"));
    customerRepository.save(another);

    mockMvc.perform(get(RestConstants.CUSTOMERS + "/" + another.getId())
            .header("Authorization", validToken)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(another.getId()))
        .andExpect(jsonPath("$.email").value("jane@example.com"))
        .andExpect(jsonPath("$.name").value("Jane Doe"));
  }

  @Test
  @DisplayName("Unauthenticated user retrieving customer returns 401 Unauthorized")
  void unauthenticatedUserCannotGetCustomer() throws Exception {
    mockMvc.perform(get(RestConstants.CUSTOMERS + "/" + existingCustomer.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Authenticated user can update any customer (200 OK)")
  void authenticatedUserCanUpdateCustomer() throws Exception {
    CustomerRequest update = new CustomerRequest();
    update.setName("Updated Name");
    update.setEmail(existingCustomer.getEmail());
    update.setPassword("password123");

    mockMvc.perform(put(RestConstants.CUSTOMERS + "/" + existingCustomer.getId())
            .header("Authorization", validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Name"));
  }

  @Test
  @DisplayName("Authenticated user can delete any customer (204 No Content)")
  void authenticatedUserCanDeleteCustomer() throws Exception {
    CustomerEntity another = new CustomerEntity();
    another.setName("Delete Me");
    another.setEmail("deleteme@example.com");
    another.setPassword(passwordEncoder.encode("password123"));
    customerRepository.save(another);

    mockMvc.perform(delete(RestConstants.CUSTOMERS + "/" + another.getId())
            .header("Authorization", validToken))
        .andExpect(status().isNoContent());
  }
}
