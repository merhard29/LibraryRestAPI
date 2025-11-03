package de.moritzerhard.libraryrestapi.mapper;

import de.moritzerhard.libraryrestapi.dto.request.CategoryRequest;
import de.moritzerhard.libraryrestapi.dto.response.CategoryResponse;
import de.moritzerhard.libraryrestapi.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Category entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {
  /**
   * Maps a CategoryRequest DTO to a CategoryEntity.
   */
  CategoryEntity toEntity(CategoryRequest categoryRequest);

  /**
   * Maps a CategoryEntity to a CategoryResponse DTO.
   */
  @Mapping(target = "bookCount", expression = "java(categoryEntity.getBooks() != null ? categoryEntity.getBooks().size() : 0)")
  CategoryResponse toResponse(CategoryEntity categoryEntity);
}
