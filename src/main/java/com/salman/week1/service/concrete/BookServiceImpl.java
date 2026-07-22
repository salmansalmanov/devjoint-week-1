package com.salman.week1.service.concrete;

import com.salman.week1.exception.custom.NotFoundException;
import com.salman.week1.mapper.BookMapper;
import com.salman.week1.model.dto.request.BookRequest;
import com.salman.week1.model.dto.response.BookResponse;
import com.salman.week1.model.entity.Author;
import com.salman.week1.model.entity.Book;
import com.salman.week1.repository.AuthorRepository;
import com.salman.week1.repository.BookRepository;
import com.salman.week1.service.abstraction.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public BookResponse createBook(BookRequest request) {
        Book book = bookMapper.createRequestToEntity(request);
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author not found with ID: " + request.getAuthorId()));
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }
}
