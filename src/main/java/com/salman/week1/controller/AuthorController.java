package com.salman.week1.controller;

import com.salman.week1.model.dto.request.AuthorRequest;
import com.salman.week1.model.dto.response.AuthorResponse;
import com.salman.week1.service.abstraction.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
