package de.moritzerhard.libraryrestapi.controller;

import de.moritzerhard.libraryrestapi.api.CategoryControllerDefinition;
import de.moritzerhard.libraryrestapi.dto.request.CategoryRequest;
import de.moritzerhard.libraryrestapi.dto.response.CategoryResponse;
import de.moritzerhard.libraryrestapi.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of the category controller for managing category-related endpoints.
 */
@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDefinition {
  private final CategoryService categoryService;

  @Override
  public ResponseEntity<CategoryResponse> createCategory(CategoryRequest categoryRequest) {
    CategoryResponse response = categoryService.create(categoryRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  public ResponseEntity<List<CategoryResponse>> getAllCategories() {
    return ResponseEntity.ok(categoryService.getAll());
  }

  @Override
  public ResponseEntity<CategoryResponse> getCategoryById(Long id) {
    return ResponseEntity.ok(categoryService.getById(id));
  }

  @Override
  public ResponseEntity<CategoryResponse> updateCategory(Long id, CategoryRequest categoryRequest) {
    return ResponseEntity.ok(categoryService.update(id, categoryRequest));
  }

  @Override
  public ResponseEntity<Void> deleteCategory(Long id) {
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
