package com.salman.week1.service.abstraction;

import com.salman.week1.model.dto.request.BookRequest;
import com.salman.week1.model.dto.response.BookResponse;

public interface BookService {
    BookResponse createBook(BookRequest request);
}
