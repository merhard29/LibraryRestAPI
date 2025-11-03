package de.moritzerhard.libraryrestapi.dto.response;

import lombok.Data;

/**
 * Data Transfer Object for Category response.
 */
@Data
public class CategoryResponse {
  private Long id;
  private String name;
  private String description;
  private int bookCount;
}
