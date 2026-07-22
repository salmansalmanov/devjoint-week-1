package com.salman.week1.service.concrete;

import com.salman.week1.mapper.MemberMapper;
import com.salman.week1.model.dto.request.MemberRequest;
import com.salman.week1.model.dto.response.MemberResponse;
import com.salman.week1.model.entity.Member;
import com.salman.week1.model.enums.MemberStatus;
import com.salman.week1.repository.MemberRepository;
import com.salman.week1.service.abstraction.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public Page<MemberResponse> getAll(int page, int size, MemberStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Member> members = memberRepository.findAllByStatus(status, pageable);
        return members.map(memberMapper::toResponse);
    }
}
