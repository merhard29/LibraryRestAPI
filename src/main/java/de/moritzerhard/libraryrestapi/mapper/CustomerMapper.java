package de.moritzerhard.libraryrestapi.mapper;

import de.moritzerhard.libraryrestapi.dto.request.CustomerRequest;
import de.moritzerhard.libraryrestapi.dto.response.CustomerResponse;
import de.moritzerhard.libraryrestapi.entity.CustomerEntity;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Customer entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {
  /**
   * Maps a CustomerRequest DTO to a CustomerEntity.
   */
  CustomerEntity toEntity(CustomerRequest customer);

  /**
   * Maps a CustomerEntity to a CustomerResponse DTO.
   */
  CustomerResponse toResponse(CustomerEntity customer);
}
