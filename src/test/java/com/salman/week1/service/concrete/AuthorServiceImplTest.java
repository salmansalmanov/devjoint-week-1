package com.salman.week1.service.concrete;

import com.salman.week1.exception.custom.NotFoundException;
import com.salman.week1.mapper.AuthorMapper;
import com.salman.week1.model.dto.request.AuthorRequest;
import com.salman.week1.model.dto.response.AuthorResponse;
import com.salman.week1.model.entity.Author;
import com.salman.week1.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void createAuthor_ShouldReturnAuthorResponse() {
        AuthorRequest request = new AuthorRequest("John", "Doe");

        Author author = new Author();
        author.setName("John");
        author.setSurname("Doe");

        UUID id = UUID.randomUUID();

        Author savedAuthor = new Author();
        savedAuthor.setId(id);
        savedAuthor.setName("John");
        savedAuthor.setSurname("Doe");

        AuthorResponse response = new AuthorResponse(id, "John", "Doe");

        when(authorMapper.createRequestToEntity(request)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(savedAuthor);
        when(authorMapper.toResponse(savedAuthor)).thenReturn(response);

        AuthorResponse result = authorService.createAuthor(request);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());

        verify(authorRepository).save(author);
    }

    @Test
    void getAll_ShouldReturnPageOfAuthorResponse() {
        UUID id = UUID.randomUUID();

        Author author = new Author();
        author.setId(id);
        author.setName("John");
        author.setSurname("Doe");

        AuthorResponse response = new AuthorResponse(id, "John", "Doe");

        Page<Author> page = new PageImpl<>(Collections.singletonList(author));

        when(authorRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(authorMapper.toResponse(author)).thenReturn(response);

        Page<AuthorResponse> result = authorService.getAll(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().getFirst().getName());

        verify(authorRepository).findAll(any(Pageable.class));
    }

    @Test
    void getById_ShouldReturnAuthorResponse() {
        UUID id = UUID.randomUUID();

        Author author = new Author();
        author.setId(id);

        AuthorResponse response = new AuthorResponse(id, "John", "Doe");

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorMapper.toResponse(author)).thenReturn(response);

        AuthorResponse result = authorService.getById(id);

        assertEquals(id, result.getId());

        verify(authorRepository).findById(id);
    }

    @Test
    void getById_ShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> authorService.getById(id));

        verify(authorRepository).findById(id);
    }

    @Test
    void updateById_ShouldReturnUpdatedAuthor() {
        UUID id = UUID.randomUUID();

        AuthorRequest request = new AuthorRequest("Updated", "Author");

        Author existing = new Author();
        existing.setId(id);

        Author updated = new Author();
        updated.setId(id);
        updated.setName("Updated");
        updated.setSurname("Author");

        AuthorResponse response = new AuthorResponse(id, "Updated", "Author");

        when(authorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(authorMapper.updateRequestToEntity(request, existing)).thenReturn(updated);
        when(authorRepository.save(updated)).thenReturn(updated);
        when(authorMapper.toResponse(updated)).thenReturn(response);

        AuthorResponse result = authorService.updateById(id, request);

        assertEquals("Updated", result.getName());

        verify(authorRepository).save(updated);
    }

    @Test
    void updateById_ShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        AuthorRequest request = new AuthorRequest("John", "Doe");

        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> authorService.updateById(id, request));

        verify(authorRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldDeleteAuthor() {
        UUID id = UUID.randomUUID();

        Author author = new Author();
        author.setId(id);

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        String result = authorService.deleteById(id);

        assertEquals("Author deleted successfully", result);

        verify(authorRepository).delete(author);
    }

    @Test
    void deleteById_ShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> authorService.deleteById(id));

        verify(authorRepository, never()).delete(any());
    }
}