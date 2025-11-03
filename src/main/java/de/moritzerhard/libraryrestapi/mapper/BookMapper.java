package de.moritzerhard.libraryrestapi.mapper;

import de.moritzerhard.libraryrestapi.dto.request.BookRequest;
import de.moritzerhard.libraryrestapi.dto.response.BookResponse;
import de.moritzerhard.libraryrestapi.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Book entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface BookMapper {
  /**
   * Maps a BookRequest DTO to a BookEntity.
   */
  BookEntity toEntity(BookRequest bookRequest);

  /**
   * Maps a BookEntity to a BookResponse DTO.
   */
  @Mapping(target = "categoryName", expression = "java(bookEntity.getCategory() != null ? bookEntity.getCategory().getName() : null)")
  BookResponse toResponse(BookEntity bookEntity);
}