package com.salman.week1.service.concrete;

import com.salman.week1.exception.custom.NotFoundException;
import com.salman.week1.mapper.BookMapper;
import com.salman.week1.model.dto.request.BookRequest;
import com.salman.week1.model.dto.response.BookResponse;
import com.salman.week1.model.entity.Author;
import com.salman.week1.model.entity.Book;
import com.salman.week1.model.enums.BookStatus;
import com.salman.week1.repository.AuthorRepository;
import com.salman.week1.repository.BookRepository;
import com.salman.week1.service.abstraction.BookService;
import com.salman.week1.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final RedisUtil redisUtil;

    @Override
    public BookResponse createBook(BookRequest request) {
        Book book = bookMapper.createRequestToEntity(request);
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author not found with ID: " + request.getAuthorId()));
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }

    @Override
    public Page<BookResponse> getAll(int page, int size, BookStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> books = bookRepository.findAllByStatus(status, pageable);
        return books.map(bookMapper::toResponse);
    }

    @Override
    public BookResponse getById(UUID id) {
        String key = "book:" + id;
        BookResponse response = redisUtil.getResource(key, BookResponse.class);
        if (response != null) {
            return response;
        }
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + id));
        response = bookMapper.toResponse(book);
        redisUtil.save(key, response);
        return response;
    }

    @Override
    public BookResponse updateById(UUID id, BookRequest request) {
        String key = "book:" + id;
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + id));
        Book updatedBook = bookMapper.updateRequestToEntity(request, existingBook);
        if (request.getAuthorId() != null) {
            if (!existingBook.getAuthor().getId().equals(request.getAuthorId())) {
                Author author = authorRepository.findById(request.getAuthorId())
                        .orElseThrow(() -> new NotFoundException("Author not found with ID: " + request.getAuthorId()));
                updatedBook.setAuthor(author);
            }
        }
        Book savedBook = bookRepository.save(updatedBook);
        redisUtil.delete(key);
        return bookMapper.toResponse(savedBook);
    }

    @Override
    public String deleteById(UUID id) {
        String key = "book:" + id;
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + id));
        bookRepository.delete(book);
        redisUtil.delete(key);
        return "Book deleted successfully";
    }
}
