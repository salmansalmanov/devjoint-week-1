package com.salman.week1.service.abstraction;

import com.salman.week1.model.dto.request.MemberRequest;
import com.salman.week1.model.dto.response.MemberResponse;
import com.salman.week1.model.enums.MemberStatus;
import org.springframework.data.domain.Page;

public interface MemberService {
    MemberResponse createMember(MemberRequest request);

    Page<MemberResponse> getAll(int page, int size, MemberStatus status);
}
