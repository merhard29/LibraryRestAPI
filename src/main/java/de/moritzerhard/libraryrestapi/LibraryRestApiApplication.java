package de.moritzerhard.libraryrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Library REST API.
 */
@SpringBootApplication
public class LibraryRestApiApplication {

  /**
   * Entry point for the Spring Boot application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(LibraryRestApiApplication.class, args);
  }

}
