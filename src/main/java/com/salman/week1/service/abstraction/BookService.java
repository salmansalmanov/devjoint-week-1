package com.salman.week1.service.abstraction;

import com.salman.week1.model.dto.request.BookRequest;
import com.salman.week1.model.dto.response.BookResponse;
import com.salman.week1.model.enums.BookStatus;
import org.springframework.data.domain.Page;

public interface BookService {
    BookResponse createBook(BookRequest request);

    Page<BookResponse> getAll(int page, int size, BookStatus status);
}
