package com.salman.week1.mapper;

import com.salman.week1.model.dto.request.MemberRequest;
import com.salman.week1.model.dto.response.MemberResponse;
import com.salman.week1.model.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "status", constant = "ACTIVE")
    Member createRequestToEntity(MemberRequest request);

    MemberResponse toResponse(Member member);
}
