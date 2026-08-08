package com.salman.week1.mapper;

import com.salman.week1.model.dto.response.UploadResponse;
import com.salman.week1.model.entity.Image;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FileMapper {
    UploadResponse toResponse(Image image);

    @AfterMapping
    default void setFileUrl(@MappingTarget UploadResponse response, Image image) {
        response.setUrl("images/" + image.getId());
    }
}
