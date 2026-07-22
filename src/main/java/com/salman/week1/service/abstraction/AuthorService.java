package com.salman.week1.service.abstraction;

import com.salman.week1.model.dto.request.AuthorRequest;
import com.salman.week1.model.dto.response.AuthorResponse;

public interface AuthorService {
    AuthorResponse createAuthor(AuthorRequest request);
}
