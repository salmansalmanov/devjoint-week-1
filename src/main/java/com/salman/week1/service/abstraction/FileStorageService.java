package com.salman.week1.service.abstraction;

import com.salman.week1.model.dto.response.FileSaveResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileSaveResponse store(MultipartFile file);
}
