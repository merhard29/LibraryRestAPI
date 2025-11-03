package de.moritzerhard.libraryrestapi.controller;

import de.moritzerhard.libraryrestapi.api.BookControllerDefinition;
import de.moritzerhard.libraryrestapi.dto.request.BookRequest;
import de.moritzerhard.libraryrestapi.dto.response.BookResponse;
import de.moritzerhard.libraryrestapi.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of the book controller for managing book-related endpoints.
 */
@RestController
@RequiredArgsConstructor
public class BookController implements BookControllerDefinition {
  private final BookService bookService;

  @Override
  public ResponseEntity<BookResponse> createBook(BookRequest bookRequest) {
    BookResponse response = bookService.create(bookRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<List<BookResponse>> getAllBooks() {
    return ResponseEntity.ok(bookService.getAll());
  }

  @Override
  public ResponseEntity<BookResponse> getBookById(Long id) {
    return ResponseEntity.ok(bookService.getById(id));
  }

  @Override
  public ResponseEntity<BookResponse> updateBook(Long id, BookRequest bookRequest) {
    return ResponseEntity.ok(bookService.update(id, bookRequest));
  }

  @Override
  public ResponseEntity<Void> deleteBook(Long id) {
    bookService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
