package com.salman.week1.service.abstraction;

import com.salman.week1.model.dto.request.MemberCreateRequest;
import com.salman.week1.model.dto.request.MemberUpdateRequest;
import com.salman.week1.model.dto.response.MemberResponse;
import com.salman.week1.model.enums.MemberStatus;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MemberService {
    MemberResponse createMember(MemberCreateRequest request);

    Page<MemberResponse> getAll(int page, int size, MemberStatus status);

    MemberResponse getById(UUID id);

    MemberResponse updateById(UUID id, MemberUpdateRequest request);

    String deleteById(UUID id);

    MemberResponse changeStatusById(UUID id, MemberStatus status);

    String borrowBook(UUID memberId, UUID bookId);
}
