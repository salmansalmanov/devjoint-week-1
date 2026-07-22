package com.salman.week1.mapper;

import com.salman.week1.model.dto.request.BookRequest;
import com.salman.week1.model.dto.response.BookResponse;
import com.salman.week1.model.entity.Book;
import com.salman.week1.model.enums.BookStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public interface BookMapper {
    Book createRequestToEntity(BookRequest request);

    @AfterMapping
    default void initializeData(BookRequest request, @MappingTarget Book book) {
        book.setAvailableCopies(request.getTotalCopies());
        book.setStatus(BookStatus.ACTIVE);
        book.setIsbn(UUID.randomUUID());
    }

    BookResponse toResponse(Book book);

    Book updateRequestToEntity(BookRequest request, @MappingTarget Book book);
}
