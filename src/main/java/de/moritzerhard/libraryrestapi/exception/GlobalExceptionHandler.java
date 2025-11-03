package de.moritzerhard.libraryrestapi.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the REST API.
 * Handles common exceptions and converts them into appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles validation errors thrown during request body binding.
   *
   * @param ex the validation exception
   * @return a {@link ResponseEntity} containing a map of field names and error messages
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
    return ResponseEntity.badRequest().body(errors);
  }

  /**
   * Handles invalid argument errors.
   *
   * @param ex the illegal argument exception
   * @return a {@link ResponseEntity} with a BAD_REQUEST status and error message
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", ex.getMessage()));
  }

  /**
   * Handles unexpected runtime exceptions.
   *
   * @param ex the runtime exception
   * @return a {@link ResponseEntity} with a NOT_FOUND status and error message
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("error", ex.getMessage()));
  }

  /**
   * Handles access denied exceptions (e.g., missing permissions).
   *
   * @param ex the access denied exception
   * @return a {@link ResponseEntity} with a FORBIDDEN status and error details
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(Map.of("error", "forbidden", "message", ex.getMessage()));
  }
}
