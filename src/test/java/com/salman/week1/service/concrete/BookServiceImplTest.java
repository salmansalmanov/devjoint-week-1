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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void createBook_ShouldReturnBookResponse() {
        UUID authorId = UUID.randomUUID();

        BookRequest request = new BookRequest(
                "Clean Code",
                2008,
                5,
                authorId
        );

        Author author = new Author();
        author.setId(authorId);

        Book book = new Book();
        Book savedBook = new Book();

        BookResponse response = new BookResponse();
        response.setTitle("Clean Code");

        when(bookMapper.createRequestToEntity(request)).thenReturn(book);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toResponse(savedBook)).thenReturn(response);

        BookResponse result = bookService.createBook(request);

        assertEquals("Clean Code", result.getTitle());

        verify(bookRepository).save(book);
    }

    @Test
    void createBook_ShouldThrowNotFoundException_WhenAuthorNotFound() {
        UUID authorId = UUID.randomUUID();

        BookRequest request = new BookRequest(
                "Clean Code",
                2008,
                5,
                authorId
        );

        Book book = new Book();

        when(bookMapper.createRequestToEntity(request)).thenReturn(book);
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookService.createBook(request));

        verify(bookRepository, never()).save(any());
    }

    @Test
    void getAll_ShouldReturnPage() {
        Book book = new Book();

        BookResponse response = new BookResponse();
        response.setTitle("Clean Code");

        Page<Book> page = new PageImpl<>(Collections.singletonList(book));

        when(bookRepository.findAllByStatus(eq(BookStatus.ACTIVE), any(Pageable.class)))
                .thenReturn(page);

        when(bookMapper.toResponse(book)).thenReturn(response);

        Page<BookResponse> result =
                bookService.getAll(0, 10, BookStatus.ACTIVE);

        assertEquals(1, result.getTotalElements());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
    }

    @Test
    void getById_ShouldReturnBook() {
        UUID id = UUID.randomUUID();

        Book book = new Book();
        book.setId(id);

        BookResponse response = new BookResponse();
        response.setId(id);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponse result = bookService.getById(id);

        assertEquals(id, result.getId());
    }

    @Test
    void getById_ShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookService.getById(id));
    }

    @Test
    void updateById_ShouldUpdateBook_WhenAuthorChanged() {
        UUID bookId = UUID.randomUUID();
        UUID oldAuthorId = UUID.randomUUID();
        UUID newAuthorId = UUID.randomUUID();

        BookRequest request = new BookRequest(
                "Updated",
                2024,
                10,
                newAuthorId
        );

        Author oldAuthor = new Author();
        oldAuthor.setId(oldAuthorId);

        Author newAuthor = new Author();
        newAuthor.setId(newAuthorId);

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setAuthor(oldAuthor);

        Book updatedBook = new Book();
        updatedBook.setId(bookId);

        BookResponse response = new BookResponse();
        response.setTitle("Updated");

        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(existingBook));

        when(bookMapper.updateRequestToEntity(request, existingBook))
                .thenReturn(updatedBook);

        when(authorRepository.findById(newAuthorId))
                .thenReturn(Optional.of(newAuthor));

        when(bookRepository.save(updatedBook))
                .thenReturn(updatedBook);

        when(bookMapper.toResponse(updatedBook))
                .thenReturn(response);

        BookResponse result = bookService.updateById(bookId, request);

        assertEquals("Updated", result.getTitle());

        verify(authorRepository).findById(newAuthorId);
    }

    @Test
    void updateById_ShouldThrowNotFoundException_WhenBookNotFound() {
        UUID id = UUID.randomUUID();

        BookRequest request =
                new BookRequest("Book", 2020, 5, UUID.randomUUID());

        when(bookRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookService.updateById(id, request));
    }

    @Test
    void deleteById_ShouldDeleteBook() {
        UUID id = UUID.randomUUID();

        Book book = new Book();

        when(bookRepository.findById(id))
                .thenReturn(Optional.of(book));

        String result = bookService.deleteById(id);

        assertEquals("Book deleted successfully", result);

        verify(bookRepository).delete(book);
    }

    @Test
    void deleteById_ShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(bookRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookService.deleteById(id));

        verify(bookRepository, never()).delete(any());
    }
}