package com.salman.week1.model.dto.response;

import com.salman.week1.model.enums.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private UUID id;
    private String title;
    private UUID isbn;
    private Integer publicationYear;
    private Integer totalCopies;
    private Integer availableCopies;
    private BookStatus status;
    private AuthorResponse author;
}
