package com.salman.week1.service.concrete;

import com.salman.week1.mapper.MemberMapper;
import com.salman.week1.model.dto.request.MemberRequest;
import com.salman.week1.model.dto.response.MemberResponse;
import com.salman.week1.model.entity.Member;
import com.salman.week1.repository.MemberRepository;
import com.salman.week1.service.abstraction.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    @Override
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberMapper.createRequestToEntity(request);
        Member savedMember = memberRepository.save(member);
        return memberMapper.toResponse(savedMember);
    }
}
