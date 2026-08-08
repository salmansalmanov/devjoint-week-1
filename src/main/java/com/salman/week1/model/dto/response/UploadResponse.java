package com.salman.week1.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    private UUID id;
    private String originalFileName;
    private String changedFileName;
    private String url;
}
