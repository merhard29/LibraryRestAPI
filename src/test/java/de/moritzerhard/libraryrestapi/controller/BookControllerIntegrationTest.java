package de.moritzerhard.libraryrestapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.moritzerhard.libraryrestapi.dto.request.BookRequest;
import de.moritzerhard.libraryrestapi.entity.BookEntity;
import de.moritzerhard.libraryrestapi.entity.CategoryEntity;
import de.moritzerhard.libraryrestapi.entity.CustomerEntity;
import de.moritzerhard.libraryrestapi.repository.BookRepository;
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
class BookControllerIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtEncoder jwtEncoder;

  private String validToken;
  private CategoryEntity category;
  private BookEntity book;

  @BeforeEach
  void setUp() {
    // Create test user
    CustomerEntity user = new CustomerEntity();
    user.setName("Book Tester");
    user.setEmail("booktester@example.com");
    user.setPassword(passwordEncoder.encode("password123"));
    customerRepository.save(user);

    validToken = JwtTestUtils.generateBearerToken(jwtEncoder, user.getEmail());

    // Create category
    category = new CategoryEntity();
    category.setName("Fiction");
    category.setDescription("Fictional stories");
    categoryRepository.save(category);

    // Create book
    book = new BookEntity();
    book.setTitle("Test Book");
    book.setAuthor("John Writer");
    book.setPublisher("Test Publisher");
    book.setPublishingYear(2023);
    book.setCategory(category);
    bookRepository.save(book);
  }

  @Test
  @DisplayName("Get all books without authentication returns 200 OK")
  void getAllBooksWithoutAuthReturnsOk() throws Exception {
    mockMvc.perform(get(RestConstants.BOOKS)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].title").value("Test Book"));
  }

  @Test
  @DisplayName("Get book by ID without authentication returns 200 OK")
  void getBookByIdWithoutAuthReturnsOk() throws Exception {
    mockMvc.perform(get(RestConstants.BOOKS + "/" + book.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Test Book"))
        .andExpect(jsonPath("$.categoryName").value("Fiction"));
  }

  @Test
  @DisplayName("Create book without token returns 401 Unauthorized")
  void createBookWithoutAuthReturnsUnauthorized() throws Exception {
    BookRequest request = new BookRequest();
    request.setTitle("Unauthorized Book");
    request.setAuthor("Anonymous");
    request.setPublisher("NoPub");
    request.setPublishingYear(2024);
    request.setCategoryId(category.getId());

    mockMvc.perform(post(RestConstants.BOOKS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Create book with valid JWT returns 201 Created")
  void createBookWithAuthReturnsCreated() throws Exception {
    BookRequest request = new BookRequest();
    request.setTitle("Authorized Book");
    request.setAuthor("Secure Author");
    request.setPublisher("Secure Pub");
    request.setPublishingYear(2025);
    request.setCategoryId(category.getId());

    mockMvc.perform(post(RestConstants.BOOKS)
            .header("Authorization", validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Authorized Book"))
        .andExpect(jsonPath("$.author").value("Secure Author"));
  }

  @Test
  @DisplayName("Update book with valid JWT returns 200 OK")
  void updateBookWithAuthReturnsOk() throws Exception {
    BookRequest update = new BookRequest();
    update.setTitle("Updated Book Title");
    update.setAuthor(book.getAuthor());
    update.setPublisher(book.getPublisher());
    update.setPublishingYear(book.getPublishingYear());
    update.setCategoryId(category.getId());

    mockMvc.perform(put(RestConstants.BOOKS + "/" + book.getId())
            .header("Authorization", validToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(update)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated Book Title"));
  }

  @Test
  @DisplayName("Delete book with valid JWT returns 204 No Content")
  void deleteBookWithAuthReturnsNoContent() throws Exception {
    mockMvc.perform(delete(RestConstants.BOOKS + "/" + book.getId())
            .header("Authorization", validToken))
        .andExpect(status().isNoContent());
  }
}
