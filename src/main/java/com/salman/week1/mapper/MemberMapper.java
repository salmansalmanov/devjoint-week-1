package com.salman.week1.mapper;

import com.salman.week1.model.dto.request.MemberCreateRequest;
import com.salman.week1.model.dto.request.MemberUpdateRequest;
import com.salman.week1.model.dto.response.MemberResponse;
import com.salman.week1.model.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "status", constant = "ACTIVE")
    Member createRequestToEntity(MemberCreateRequest request);

    MemberResponse toResponse(Member member);

    Member updateRequestToEntity(MemberUpdateRequest request, @MappingTarget Member member);
}
