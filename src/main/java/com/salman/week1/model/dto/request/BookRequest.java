package com.salman.week1.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String title;
    private Integer publicationYear;
    private Integer totalCopies;
    private UUID authorId;
}
