package com.salman.week1.model.entity;

import com.salman.week1.model.enums.BookStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private UUID isbn;

    @Column(nullable = false)
    private Integer publicationYear;

    @Column(nullable = false)
    private Integer totalCopies;

    @Column(nullable = false)
    private Integer availableCopies;

    @Enumerated(EnumType.STRING)
    private BookStatus status;
}
