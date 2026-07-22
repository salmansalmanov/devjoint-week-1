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
import com.salman.week1.repository.BookRepository;
import com.salman.week1.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private UUID memberId;
    private UUID bookId;
    private Member member;
    private Book book;
    private MemberResponse memberResponse;

    @BeforeEach
    void setUp() {
        memberId = UUID.randomUUID();
        bookId = UUID.randomUUID();

        member = new Member();
        member.setName("John");
        member.setSurname("Doe");
        member.setStatus(MemberStatus.ACTIVE);

        book = new Book();
        book.setAvailableCopies(2);
        book.setStatus(BookStatus.ACTIVE);

        memberResponse = new MemberResponse();
        memberResponse.setId(memberId);
        memberResponse.setName("John");
        memberResponse.setSurname("Doe");
        memberResponse.setStatus(MemberStatus.ACTIVE);
    }

    // ---------------- createMember ----------------

    @Test
    void createMember_shouldReturnMemberResponse() {
        MemberCreateRequest request = new MemberCreateRequest("John", "Doe");

        when(memberMapper.createRequestToEntity(request)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toResponse(member)).thenReturn(memberResponse);

        MemberResponse result = memberService.createMember(request);

        assertThat(result).isEqualTo(memberResponse);
        verify(memberMapper).createRequestToEntity(request);
        verify(memberRepository).save(member);
        verify(memberMapper).toResponse(member);
    }

    // ---------------- getAll ----------------

    @Test
    void getAll_shouldReturnPageOfMemberResponse() {
        int page = 0;
        int size = 10;
        MemberStatus status = MemberStatus.ACTIVE;
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage = new PageImpl<>(List.of(member), pageable, 1);

        when(memberRepository.findAllByStatus(eq(status), any(Pageable.class))).thenReturn(memberPage);
        when(memberMapper.toResponse(member)).thenReturn(memberResponse);

        Page<MemberResponse> result = memberService.getAll(page, size, status);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(memberResponse);
        verify(memberRepository).findAllByStatus(eq(status), any(Pageable.class));
    }

    // ---------------- getById ----------------

    @Test
    void getById_whenMemberExists_shouldReturnMemberResponse() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberMapper.toResponse(member)).thenReturn(memberResponse);

        MemberResponse result = memberService.getById(memberId);

        assertThat(result).isEqualTo(memberResponse);
        verify(memberRepository).findById(memberId);
    }

    @Test
    void getById_whenMemberNotFound_shouldThrowNotFoundException() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.getById(memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(memberId.toString());

        verify(memberRepository, never()).save(any());
    }

    // ---------------- updateById ----------------

    @Test
    void updateById_whenMemberExists_shouldReturnUpdatedMemberResponse() {
        MemberUpdateRequest request = new MemberUpdateRequest("Jane", "Doe", MemberStatus.ACTIVE);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberMapper.updateRequestToEntity(request, member)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toResponse(member)).thenReturn(memberResponse);

        MemberResponse result = memberService.updateById(memberId, request);

        assertThat(result).isEqualTo(memberResponse);
        verify(memberRepository).findById(memberId);
        verify(memberMapper).updateRequestToEntity(request, member);
        verify(memberRepository).save(member);
    }

    @Test
    void updateById_whenMemberNotFound_shouldThrowNotFoundException() {
        MemberUpdateRequest request = new MemberUpdateRequest("Jane", "Doe", MemberStatus.ACTIVE);
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.updateById(memberId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(memberId.toString());

        verify(memberRepository, never()).save(any());
    }

    // ---------------- deleteById ----------------

    @Test
    void deleteById_whenMemberExists_shouldDeleteAndReturnMessage() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        doNothing().when(memberRepository).delete(member);

        String result = memberService.deleteById(memberId);

        assertThat(result).isEqualTo("Member deleted successfully");
        verify(memberRepository).delete(member);
    }

    @Test
    void deleteById_whenMemberNotFound_shouldThrowNotFoundException() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.deleteById(memberId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(memberId.toString());

        verify(memberRepository, never()).delete(any());
    }

    // ---------------- changeStatusById ----------------

    @Test
    void changeStatusById_whenMemberExists_shouldUpdateStatusAndReturnResponse() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toResponse(member)).thenReturn(memberResponse);

        MemberResponse result = memberService.changeStatusById(memberId, MemberStatus.BLOCKED);

        assertThat(result).isEqualTo(memberResponse);
        assertThat(member.getStatus()).isEqualTo(MemberStatus.BLOCKED);
        verify(memberRepository).save(member);
    }

    @Test
    void changeStatusById_whenMemberNotFound_shouldThrowNotFoundException() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.changeStatusById(memberId, MemberStatus.BLOCKED))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(memberId.toString());

        verify(memberRepository, never()).save(any());
    }

    // ---------------- borrowBook ----------------

    @Test
    void borrowBook_whenAllValid_shouldBorrowSuccessfully() {
        book.setAvailableCopies(3);

        when(memberRepository.findByIdAndStatus(memberId, MemberStatus.ACTIVE)).thenReturn(Optional.of(member));
        when(bookRepository.findByIdAndStatus(bookId, BookStatus.ACTIVE)).thenReturn(Optional.of(book));
        when(memberRepository.save(member)).thenReturn(member);

        String result = memberService.borrowBook(memberId, bookId);

        assertThat(result).isEqualTo("Book borrowed successfully");
        assertThat(book.getAvailableCopies()).isEqualTo(2);
        assertThat(member.getBooks()).contains(book);
        assertThat(book.getMembers()).contains(member);
        verify(memberRepository).save(member);
    }

    @Test
    void borrowBook_whenMemberNotFoundOrNotActive_shouldThrowNotFoundException() {
        when(memberRepository.findByIdAndStatus(memberId, MemberStatus.ACTIVE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.borrowBook(memberId, bookId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(memberId.toString());

        verify(bookRepository, never()).findByIdAndStatus(any(), any());
        verify(memberRepository, never()).save(any());
    }

    @Test
    void borrowBook_whenBookNotFoundOrNotActive_shouldThrowNotFoundException() {
        when(memberRepository.findByIdAndStatus(memberId, MemberStatus.ACTIVE)).thenReturn(Optional.of(member));
        when(bookRepository.findByIdAndStatus(bookId, BookStatus.ACTIVE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.borrowBook(memberId, bookId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(bookId.toString());

        verify(memberRepository, never()).save(any());
    }

    @Test
    void borrowBook_whenNoAvailableCopies_shouldThrowNotAvailableException() {
        book.setAvailableCopies(0);

        when(memberRepository.findByIdAndStatus(memberId, MemberStatus.ACTIVE)).thenReturn(Optional.of(member));
        when(bookRepository.findByIdAndStatus(bookId, BookStatus.ACTIVE)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> memberService.borrowBook(memberId, bookId))
                .isInstanceOf(NotAvailableException.class)
                .hasMessageContaining("not available");

        verify(memberRepository, never()).save(any());
    }
}