package com.salman.week1.controller;

import com.salman.week1.model.dto.request.BookRequest;
import com.salman.week1.model.dto.response.BookResponse;
import com.salman.week1.service.abstraction.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
