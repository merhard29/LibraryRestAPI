package de.moritzerhard.libraryrestapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for creating or updating a book.
 */
@Data
public class BookRequest {
  @NotBlank
  private String title;

  @NotBlank
  private String author;

  private String publisher;

  private int publishingYear;

  private Long categoryId;
}
