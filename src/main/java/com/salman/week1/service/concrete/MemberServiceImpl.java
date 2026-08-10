package com.salman.week1.service.concrete;

import com.salman.week1.exception.custom.NotAvailableException;
import com.salman.week1.exception.custom.NotFoundException;
import com.salman.week1.mapper.MemberMapper;
import com.salman.week1.model.dto.request.MemberCreateRequest;
import com.salman.week1.model.dto.request.MemberUpdateRequest;
import com.salman.week1.model.dto.response.MemberResponse;
import com.salman.week1.model.entity.Book;
import com.salman.week1.model.entity.Member;
import com.salman.week1.model.enums.BookStatus;
import com.salman.week1.model.enums.MemberStatus;
import com.salman.week1.model.enums.Role;
import com.salman.week1.repository.BookRepository;
import com.salman.week1.repository.MemberRepository;
import com.salman.week1.service.abstraction.BookService;
import com.salman.week1.service.abstraction.MemberService;
import com.salman.week1.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final MailService mailService;

    @Override
    public MemberResponse createMember(MemberCreateRequest request) {
        Member member = memberMapper.createRequestToEntity(request);
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setRole(Role.MEMBER);
        Member savedMember = memberRepository.save(member);
        mailService.sendUserCreationEmail(member.getEmail());
        return memberMapper.toResponse(savedMember);
    }

    @Override
    public Page<MemberResponse> getAll(int page, int size, MemberStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Member> members = memberRepository.findAllByStatus(status, pageable);
        return members.map(memberMapper::toResponse);
    }

    @Override
    public MemberResponse getById(UUID id) {
        String key = "member:" + id;
        MemberResponse response = redisUtil.getResource(key, MemberResponse.class);
        if (response != null) {
            return response;
        }
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with ID: " + id));
        response = memberMapper.toResponse(member);
        redisUtil.save(key, response);
        return response;
    }

    @Override
    public MemberResponse updateById(UUID id, MemberUpdateRequest request) {
        String key = "member:" + id;
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with ID: " + id));
        Member updatedMember = memberMapper.updateRequestToEntity(request, existingMember);
        updatedMember.setPassword(passwordEncoder.encode(request.getPassword()));
        Member savedMember = memberRepository.save(updatedMember);
        redisUtil.delete(key);
        mailService.sendUserUpdateEmail(savedMember.getEmail());
        return memberMapper.toResponse(savedMember);
    }

    @Override
    public String deleteById(UUID id) {
        String key = "member:" + id;
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with ID: " + id));
        memberRepository.delete(member);
        redisUtil.delete(key);
        mailService.sendUserDeletionEmail(member.getEmail());
        return "Member deleted successfully";
    }

    @Override
    public MemberResponse changeStatusById(UUID id, MemberStatus status) {
        String key = "member:" + id;
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with ID: " + id));
        member.setStatus(status);
        Member savedMember = memberRepository.save(member);
        redisUtil.delete(key);
        mailService.sendUserUpdateEmail(savedMember.getEmail());
        return memberMapper.toResponse(savedMember);
    }

    @Override
    @Transactional
    public String borrowBook(UUID memberId, UUID bookId) {
        Member member = memberRepository.findByIdAndStatus(memberId, MemberStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Member not found with ID: " + memberId));
        Book book = bookRepository.findByIdAndStatus(bookId, BookStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + bookId));
        if (book.getAvailableCopies() <= 0) {
            throw new NotAvailableException("Book is not available");
        }

        member.getBooks().add(book);
        book.getMembers().add(member);
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        memberRepository.save(member);
        return "Book borrowed successfully";
    }
}
