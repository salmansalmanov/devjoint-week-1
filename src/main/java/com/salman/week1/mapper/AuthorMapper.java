package com.salman.week1.mapper;

import com.salman.week1.model.dto.request.AuthorRequest;
import com.salman.week1.model.dto.response.AuthorResponse;
import com.salman.week1.model.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author createRequestToEntity(AuthorRequest request);

    AuthorResponse toResponse(Author author);

    Author updateRequestToEntity(AuthorRequest request, @MappingTarget Author author);
}
