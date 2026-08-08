package com.salman.week1.model.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {
    private UUID id;
    private String name;
    private String surname;
}
