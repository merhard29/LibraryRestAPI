package de.moritzerhard.libraryrestapi.service;

import de.moritzerhard.libraryrestapi.dto.request.BookRequest;
import de.moritzerhard.libraryrestapi.dto.response.BookResponse;
import de.moritzerhard.libraryrestapi.entity.BookEntity;
import de.moritzerhard.libraryrestapi.entity.CategoryEntity;
import de.moritzerhard.libraryrestapi.mapper.BookMapper;
import de.moritzerhard.libraryrestapi.repository.BookRepository;
import de.moritzerhard.libraryrestapi.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for managing books, providing CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;
  private final BookMapper bookMapper;

  /**
   * Creates a new book and assigns it to a category.
   *
   * @param request the book creation request containing book details and category ID
   * @return the created {@link BookResponse}
   * @throws EntityNotFoundException if the category does not exist
   */
  public BookResponse create(BookRequest request) {
    CategoryEntity category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + request.getCategoryId()));

    BookEntity entity = bookMapper.toEntity(request);
    entity.setCategory(category);

    bookRepository.save(entity);
    return bookMapper.toResponse(entity);
  }

  /**
   * Retrieves all books.
   *
   * @return a list of all {@link BookResponse} objects
   */
  public List<BookResponse> getAll() {
    return bookRepository.findAll()
        .stream()
        .map(bookMapper::toResponse)
        .toList();
  }

  /**
   * Retrieves a book by its ID.
   *
   * @param id the ID of the book
   * @return the corresponding {@link BookResponse}
   * @throws EntityNotFoundException if no book with the given ID exists
   */
  public BookResponse getById(Long id) {
    BookEntity entity = bookRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
    return bookMapper.toResponse(entity);
  }

  /**
   * Updates an existing book with new data.
   *
   * @param id      the ID of the book to update
   * @param request the updated book details
   * @return the updated {@link BookResponse}
   * @throws EntityNotFoundException if the book or the specified category does not exist
   */
  public BookResponse update(Long id, BookRequest request) {
    BookEntity entity = bookRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

    entity.setTitle(request.getTitle());
    entity.setAuthor(request.getAuthor());
    entity.setPublisher(request.getPublisher());
    entity.setPublishingYear(request.getPublishingYear());

    if (request.getCategoryId() != null) {
      CategoryEntity category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + request.getCategoryId()));
      entity.setCategory(category);
    }

    bookRepository.save(entity);
    return bookMapper.toResponse(entity);
  }

  /**
   * Deletes a book by its ID.
   *
   * @param id the ID of the book to delete
   * @throws EntityNotFoundException if the book does not exist
   */
  public void delete(Long id) {
    if (!bookRepository.existsById(id)) {
      throw new EntityNotFoundException("Book not found with id: " + id);
    }
    bookRepository.deleteById(id);
  }
}