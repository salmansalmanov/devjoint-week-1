package com.salman.week1.service.abstraction;

import com.salman.week1.model.dto.response.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    UploadResponse upload(MultipartFile file);
}
