package com.salman.week1.controller;

import com.salman.week1.model.dto.request.BookRequest;
import com.salman.week1.model.dto.response.BookResponse;
import com.salman.week1.model.enums.BookStatus;
import com.salman.week1.service.abstraction.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Book Management", description = "REST APIs for managing books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Create a new book", description = "Creates a new book record with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (Validation error)")
    })
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.createBook(request));
    }

    @Operation(summary = "Get all books", description = "Returns a paginated list of books, optionally filtered by status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @Parameter(description = "Page index (zero-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Filter books by status (optional)", example = "AVAILABLE")
            @RequestParam(required = false) BookStatus status
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getAll(page, size, status));
    }

    @Operation(summary = "Get book by ID", description = "Returns details of a specific book based on the provided UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(description = "Unique ID of the book (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getById(id));
    }

    @Operation(summary = "Update book by ID", description = "Updates details of an existing book based on the ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (Validation error)"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBookById(
            @Parameter(description = "Unique ID of the book (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @RequestBody @Valid BookRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.updateById(id, request));
    }

    @Operation(summary = "Delete book by ID", description = "Deletes a book record from the system based on the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(
            @Parameter(description = "Unique ID of the book (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.deleteById(id));
    }
}