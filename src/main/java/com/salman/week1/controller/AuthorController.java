package com.salman.week1.controller;

import com.salman.week1.model.dto.request.AuthorRequest;
import com.salman.week1.model.dto.response.AuthorResponse;
import com.salman.week1.service.abstraction.AuthorService;
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

@Tag(name = "Author Management", description = "REST APIs for managing authors")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Create a new author", description = "Creates a new author based on the provided request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (Validation error)")
    })
    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@RequestBody @Valid AuthorRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authorService.createAuthor(request));
    }

    @Operation(summary = "Get all authors", description = "Returns a paginated list of authors.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> getAllAuthors(
            @Parameter(description = "Page index (zero-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.getAll(page, size));
    }

    @Operation(summary = "Get author by ID", description = "Returns details of a specific author based on the provided UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
            @Parameter(description = "Unique ID of the author (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.getById(id));
    }

    @Operation(summary = "Update author by ID", description = "Updates details of an existing author based on the ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (Validation error)"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthorById(
            @Parameter(description = "Unique ID of the author (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @RequestBody @Valid AuthorRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.updateById(id, request));
    }

    @Operation(summary = "Delete author by ID", description = "Deletes an author from the system based on the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthorById(
            @Parameter(description = "Unique ID of the author (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.deleteById(id));
    }
}