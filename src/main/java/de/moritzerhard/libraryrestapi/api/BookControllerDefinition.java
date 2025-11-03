package de.moritzerhard.libraryrestapi.api;

import de.moritzerhard.libraryrestapi.dto.request.BookRequest;
import de.moritzerhard.libraryrestapi.dto.response.BookResponse;
import de.moritzerhard.libraryrestapi.utils.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Defines the REST API endpoints for managing books.
 */
@Tag(name = "Books", description = "Endpoints for managing books")
@RequestMapping(RestConstants.BOOKS)
public interface BookControllerDefinition {
  /**
   * Creates a new book and assigns it to a category.
   *
   * @param bookRequest the book details to create
   * @return a {@link ResponseEntity} containing the created {@link BookResponse}
   */
  @Operation(
      summary = "Create a new book",
      description = "Creates a new book and assigns it to a category. Requires authentication.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "Book data to be created"
      )
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Book created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid book data provided"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access")
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookRequest);

  /**
   * Retrieves all available books.
   *
   * @return a {@link ResponseEntity} containing a list of {@link BookResponse} objects
   */
  @Operation(summary = "Get all books", description = "Returns a list of all books. Accessible to anonymous users.")
  @ApiResponse(responseCode = "200", description = "List of books returned successfully")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<List<BookResponse>> getAllBooks();

  /**
   * Retrieves a specific book by its ID.
   *
   * @param id the ID of the book to retrieve
   * @return a {@link ResponseEntity} containing the {@link BookResponse} if found
   */
  @Operation(summary = "Get book by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Book found"),
      @ApiResponse(responseCode = "404", description = "Book not found")
  })
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<BookResponse> getBookById(
      @Parameter(description = "ID of the book to retrieve", required = true)
      @PathVariable Long id);

  /**
   * Updates the information of an existing book.
   *
   * @param id          the ID of the book to update
   * @param bookRequest the updated book information
   * @return a {@link ResponseEntity} containing the updated {@link BookResponse}
   */
  @Operation(
      summary = "Update an existing book",
      description = "Updates the information of an existing book. Requires authentication.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "Updated book information"
      )
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Book updated successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Book not found")
  })
  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<BookResponse> updateBook(
      @Parameter(description = "ID of the book to update", required = true)
      @PathVariable Long id,
      @RequestBody BookRequest bookRequest);

  /**
   * Deletes a book by its ID.
   *
   * @param id the ID of the book to delete
   * @return an empty {@link ResponseEntity} with status 204 if successful
   */
  @Operation(summary = "Delete a book by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Book not found")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteBook(
      @Parameter(description = "ID of the book to delete", required = true)
      @PathVariable Long id);
}
