package de.moritzerhard.libraryrestapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for creating or updating a category.
 */
@Data
public class CategoryRequest {
  @NotBlank
  private String name;

  private String description;
}
