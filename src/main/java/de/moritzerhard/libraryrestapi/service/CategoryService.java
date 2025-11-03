package de.moritzerhard.libraryrestapi.service;

import de.moritzerhard.libraryrestapi.dto.request.CategoryRequest;
import de.moritzerhard.libraryrestapi.dto.response.CategoryResponse;
import de.moritzerhard.libraryrestapi.entity.CategoryEntity;
import de.moritzerhard.libraryrestapi.mapper.CategoryMapper;
import de.moritzerhard.libraryrestapi.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for managing book categories, providing CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  /**
   * Creates a new category.
   *
   * @param request the category creation request containing name and description
   * @return the created {@link CategoryResponse}
   */
  public CategoryResponse create(CategoryRequest request) {
    CategoryEntity entity = categoryMapper.toEntity(request);
    return categoryMapper.toResponse(categoryRepository.save(entity));
  }

  /**
   * Retrieves all categories.
   *
   * @return a list of {@link CategoryResponse} objects
   */
  public List<CategoryResponse> getAll() {
    return categoryRepository.findAll()
        .stream()
        .map(categoryMapper::toResponse)
        .toList();
  }

  /**
   * Retrieves a category by its ID.
   *
   * @param id the ID of the category
   * @return the corresponding {@link CategoryResponse}
   * @throws EntityNotFoundException if no category with the given ID exists
   */
  public CategoryResponse getById(Long id) {
    CategoryEntity entity = categoryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    return categoryMapper.toResponse(entity);
  }

  /**
   * Updates an existing category with new data.
   *
   * @param id      the ID of the category to update
   * @param request the updated category details
   * @return the updated {@link CategoryResponse}
   * @throws EntityNotFoundException if the category does not exist
   */
  public CategoryResponse update(Long id, CategoryRequest request) {
    CategoryEntity existing = categoryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

    existing.setName(request.getName());
    existing.setDescription(request.getDescription());

    return categoryMapper.toResponse(categoryRepository.save(existing));
  }

  /**
   * Deletes a category by its ID.
   *
   * @param id the ID of the category to delete
   * @throws EntityNotFoundException if the category does not exist
   */
  public void delete(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new EntityNotFoundException("Category not found with id: " + id);
    }
    categoryRepository.deleteById(id);
  }

}
