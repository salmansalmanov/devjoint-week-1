package com.salman.week1.service.concrete;

import com.salman.week1.mapper.FileMapper;
import com.salman.week1.model.dto.response.FileSaveResponse;
import com.salman.week1.model.dto.response.UploadResponse;
import com.salman.week1.model.entity.Image;
import com.salman.week1.repository.ImageRepository;
import com.salman.week1.service.abstraction.FileService;
import com.salman.week1.service.abstraction.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileStorageService fileStorageService;
    private final ImageRepository imageRepository;
    private final FileMapper fileMapper;

    @Override
    public UploadResponse upload(MultipartFile file) {
        FileSaveResponse fileSaveResponse = fileStorageService.store(file);
        Image image = saveImage(fileSaveResponse);
        return fileMapper.toResponse(image);
    }

    private Image saveImage(FileSaveResponse fileSaveResponse) {
        return imageRepository.save(
                new Image(
                        fileSaveResponse.getOriginalFileName(),
                        fileSaveResponse.getChangedFileName()
                )
        );
    }
}
