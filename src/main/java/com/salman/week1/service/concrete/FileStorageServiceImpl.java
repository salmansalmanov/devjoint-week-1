package com.salman.week1.service.concrete;

import com.salman.week1.exception.custom.InvalidFileTypeException;
import com.salman.week1.model.dto.response.FileSaveResponse;
import com.salman.week1.service.abstraction.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Set<String> SUPPORTED_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    @Override
    public FileSaveResponse store(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String extension = "";
            String uploadDir = "images";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                if (!SUPPORTED_EXTENSIONS.contains(extension.substring(1))) {
                    throw new InvalidFileTypeException("Unsupported file type: " + extension);
                }
            }

            String changedFileName = UUID.randomUUID() + extension;
            Path root = Paths.get(uploadDir);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            Files.copy(file.getInputStream(), root.resolve(changedFileName));
            return new FileSaveResponse(originalFileName, changedFileName, uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while storing file", e);
        }
    }
}
