package de.moritzerhard.libraryrestapi.api;

import de.moritzerhard.libraryrestapi.dto.request.CategoryRequest;
import de.moritzerhard.libraryrestapi.dto.response.CategoryResponse;
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
 * Defines the REST API endpoints for managing book categories.
 */
@Tag(name = "Categories", description = "Endpoints for managing book categories")
@RequestMapping(RestConstants.CATEGORIES)
public interface CategoryControllerDefinition {
  /**
   * Creates a new book category.
   *
   * @param categoryRequest the category details to create
   * @return a {@link ResponseEntity} containing the created {@link CategoryResponse}
   */
  @Operation(
      summary = "Create a new category",
      description = "Creates a new book category in the library system. Requires authentication.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "Category data to be created"
      ))
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Category created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid category data provided"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access")
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest);

  /**
   * Retrieves all available categories.
   *
   * @return a {@link ResponseEntity} containing a list of {@link CategoryResponse} objects
   */
  @Operation(summary = "Get all categories", description = "Returns a list of all categories.")
  @ApiResponse(responseCode = "200", description = "List of categories returned successfully")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<List<CategoryResponse>> getAllCategories();

  /**
   * Retrieves a category by its ID.
   *
   * @param id the ID of the category to retrieve
   * @return a {@link ResponseEntity} containing the {@link CategoryResponse} if found
   */
  @Operation(summary = "Get category by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Category found"),
      @ApiResponse(responseCode = "404", description = "Category not found")
  })
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<CategoryResponse> getCategoryById(@Parameter(description = "ID of the category to retrieve", required = true)
                                                   @PathVariable Long id);

  /**
   * Updates an existing category.
   *
   * @param id              the ID of the category to update
   * @param categoryRequest the updated category information
   * @return a {@link ResponseEntity} containing the updated {@link CategoryResponse}
   */
  @Operation(
      summary = "Update an existing category",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "Updated category information"
      )
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Category updated successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Category not found")
  })
  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<CategoryResponse> updateCategory(@Parameter(description = "ID of the category to update", required = true)
                                                  @PathVariable Long id,
                                                  @RequestBody CategoryRequest categoryRequest);

  /**
   * Deletes a category by its ID.
   *
   * @param id the ID of the category to delete
   * @return an empty {@link ResponseEntity} with status 204 if successful
   */
  @Operation(summary = "Delete a category by ID")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Category not found")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteCategory(@Parameter(description = "ID of the category to delete", required = true) @PathVariable Long id);
}
