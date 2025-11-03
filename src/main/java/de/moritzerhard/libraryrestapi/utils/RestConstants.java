package de.moritzerhard.libraryrestapi.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants for REST API endpoints.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestConstants {
  public static final String API_V1 = "/api/v1";

  public static final String BOOKS = API_V1 + "/books";
  public static final String CATEGORIES = API_V1 + "/categories";
  public static final String CUSTOMERS = API_V1 + "/customers";
  public static final String AUTH = API_V1 + "/auth";
}
