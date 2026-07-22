package com.salman.week1.service.abstraction;

import com.salman.week1.model.dto.request.AuthorRequest;
import com.salman.week1.model.dto.response.AuthorResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AuthorService {
    AuthorResponse createAuthor(AuthorRequest request);

    Page<AuthorResponse> getAll(int page, int size);

    AuthorResponse getById(UUID id);
}
