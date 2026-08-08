package com.salman.week1.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileSaveResponse {
    private String originalFileName;
    private String changedFileName;
    private String path;
}
