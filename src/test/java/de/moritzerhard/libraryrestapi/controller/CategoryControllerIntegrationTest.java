package de.moritzerhard.libraryrestapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.moritzerhard.libraryrestapi.dto.request.CategoryRequest;
import de.moritzerhard.libraryrestapi.entity.CategoryEntity;
import de.moritzerhard.libraryrestapi.entity.CustomerEntity;
import de.moritzerhard.libraryrestapi.repository.CategoryRepository;
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
class CategoryControllerIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtEncoder jwtEncoder;

  private String validToken;
  private CategoryEntity existingCategory;

  @BeforeEach
  void setUp() {
    // Create test customer
    CustomerEntity user = new CustomerEntity();
    user.setName("Category Tester");
    user.setEmail("cat@example.com");
    user.setPassword(passwordEncoder.encode("password123"));
    customerRepository.save(user);

    validToken = JwtTestUtils.generateBearerToken(jwtEncoder, user.getEmail());

    // Create test category
    existingCategory = new CategoryEntity();
    existingCategory.setName("History");
    existingCategory.setDescription("Books about history");
    categoryRepository.save(existingCategory);
  }

  @Test
  @DisplayName("Get all categories without authentication returns 200 OK")
  void getAllCategoriesWithoutAuthReturnsOk() throws Exception {
    mockMvc.perform(get(RestConstants.CATEGORIES)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name").value("History"));
  }

  @Test
  @DisplayName("Get category by ID without authentication returns 200 OK")
  void getCategoryByIdWithoutAuthReturnsOk() throws Exception {
    mockMvc.perform(get(RestConstants.CATEGORIES + "/" + existingCategory.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("History"))
        .andExpect(jsonPath("$.description").value("Books about history"));
  }

  @Test
  @DisplayName("Create category without token returns 401 Unauthorized")
  void createCategoryWithoutAuthReturnsUnauthorized() throws Exception {
    CategoryRequest request = new CategoryRequest();
    request.setName("Unauthorized Category");
    request.setDescription("Should not be created");

    mockMvc.perform(post(RestConstants.CATEGORIES)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Create category with valid JWT returns 201 Created")
  void createCategoryWithAuthReturnsCreated() throws Exception {
    CategoryRequest request = new CategoryRequest();
    request.setName("New Category");
    request.setDescription("Created via integration test");

    mockMvc.perform(post(RestConstants.CATEGORIES)
            .header("Authorization", validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("New Category"))
        .andExpect(jsonPath("$.description").value("Created via integration test"));
  }

  @Test
  @DisplayName("Update category with valid JWT returns 200 OK")
  void updateCategoryWithAuthReturnsOk() throws Exception {
    CategoryRequest update = new CategoryRequest();
    update.setName("Updated Category");
    update.setDescription("Updated description");

    mockMvc.perform(put(RestConstants.CATEGORIES + "/" + existingCategory.getId())
            .header("Authorization", validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Category"))
        .andExpect(jsonPath("$.description").value("Updated description"));
  }

  @Test
  @DisplayName("Delete category with valid JWT returns 204 No Content")
  void deleteCategoryWithAuthReturnsNoContent() throws Exception {
    mockMvc.perform(delete(RestConstants.CATEGORIES + "/" + existingCategory.getId())
            .header("Authorization", validToken))
        .andExpect(status().isNoContent());
  }
}
