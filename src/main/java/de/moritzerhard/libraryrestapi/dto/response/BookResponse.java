package de.moritzerhard.libraryrestapi.dto.response;

import lombok.Data;

/**
 * Data Transfer Object for Book response.
 */
@Data
public class BookResponse {
  private Long id;
  private String title;
  private String author;
  private String publisher;
  private int publishingYear;
  private String categoryName;
}
