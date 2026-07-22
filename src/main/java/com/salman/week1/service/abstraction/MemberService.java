package com.salman.week1.service.abstraction;

import com.salman.week1.model.dto.request.MemberRequest;
import com.salman.week1.model.dto.response.MemberResponse;

public interface MemberService {
    MemberResponse createMember(MemberRequest request);
}
