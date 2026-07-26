package com.salman.week1.service.concrete;

import com.salman.week1.exception.custom.NotFoundException;
import com.salman.week1.mapper.AuthorMapper;
import com.salman.week1.model.dto.request.AuthorRequest;
import com.salman.week1.model.dto.response.AuthorResponse;
import com.salman.week1.model.entity.Author;
import com.salman.week1.model.enums.Role;
import com.salman.week1.repository.AuthorRepository;
import com.salman.week1.service.abstraction.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthorResponse createAuthor(AuthorRequest request) {
        Author author = authorMapper.createRequestToEntity(request);
        author.setPassword(passwordEncoder.encode(request.getPassword()));
        author.setRole(Role.AUTHOR);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(savedAuthor);
    }

    @Override
    public Page<AuthorResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Author> authors = authorRepository.findAll(pageable);
        return authors.map(authorMapper::toResponse);
    }

    @Override
    public AuthorResponse getById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with ID: " + id));
        return authorMapper.toResponse(author);
    }

    @Override
    public AuthorResponse updateById(UUID id, AuthorRequest request) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with ID: " + id));
        Author updatedAuthor = authorMapper.updateRequestToEntity(request, existingAuthor);
        updatedAuthor.setPassword(passwordEncoder.encode(request.getPassword()));
        Author savedAuthor = authorRepository.save(updatedAuthor);
        return authorMapper.toResponse(savedAuthor);
    }

    @Override
    public String deleteById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with ID: " + id));
        authorRepository.delete(author);
        return "Author deleted successfully";
    }
}
