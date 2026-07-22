package com.salman.week1.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Publication year cannot be null")
    private Integer publicationYear;

    @NotNull(message = "Total copies cannot be null")
    private Integer totalCopies;

    @NotNull(message = "Author ID cannot be null")
    private UUID authorId;
}
