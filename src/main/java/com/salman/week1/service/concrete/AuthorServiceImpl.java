package com.salman.week1.service.concrete;

import com.salman.week1.mapper.AuthorMapper;
import com.salman.week1.model.dto.request.AuthorRequest;
import com.salman.week1.model.dto.response.AuthorResponse;
import com.salman.week1.model.entity.Author;
import com.salman.week1.repository.AuthorRepository;
import com.salman.week1.service.abstraction.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponse createAuthor(AuthorRequest request) {
        Author author = authorMapper.toEntity(request);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(savedAuthor);
    }
}
