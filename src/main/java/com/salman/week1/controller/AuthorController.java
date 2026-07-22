package com.salman.week1.controller;

import com.salman.week1.model.dto.request.AuthorRequest;
import com.salman.week1.model.dto.response.AuthorResponse;
import com.salman.week1.service.abstraction.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/authors")
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@RequestBody AuthorRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authorService.createAuthor(request));
    }

    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthorById(@PathVariable UUID id, @RequestBody AuthorRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authorService.deleteById(id));
    }
}
