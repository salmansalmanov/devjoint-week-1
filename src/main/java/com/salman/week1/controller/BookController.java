package com.salman.week1.controller;

import com.salman.week1.model.dto.request.BookRequest;
import com.salman.week1.model.dto.response.BookResponse;
import com.salman.week1.model.enums.BookStatus;
import com.salman.week1.service.abstraction.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.createBook(request));
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) BookStatus status
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getAll(page, size, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> updateBookById(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getById(id));
    }
}
