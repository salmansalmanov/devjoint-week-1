package com.salman.week1.controller;

import com.salman.week1.model.dto.response.UploadResponse;
import com.salman.week1.service.abstraction.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File Management", description = "REST APIs for managing file uploads")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class FileController {
    private final FileService fileService;

    @Operation(summary = "Upload a file", description = "Uploads a file to the system and returns information about the uploaded file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File successfully uploaded"),
            @ApiResponse(responseCode = "400", description = "Invalid file or request"),
            @ApiResponse(responseCode = "500", description = "Internal server error while uploading the file")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> upload(
            @Parameter(
                    description = "File to be uploaded",
                    required = true
            )
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fileService.upload(file));
    }
}
