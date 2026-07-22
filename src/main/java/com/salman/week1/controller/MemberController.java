package com.salman.week1.controller;

import com.salman.week1.model.dto.request.MemberCreateRequest;
import com.salman.week1.model.dto.request.MemberUpdateRequest;
import com.salman.week1.model.dto.response.MemberResponse;
import com.salman.week1.model.enums.MemberStatus;
import com.salman.week1.service.abstraction.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody @Valid MemberCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberService.createMember(request));
    }

    @GetMapping
    public ResponseEntity<Page<MemberResponse>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) MemberStatus status
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.getAll(page, size, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMemberById(@RequestBody @Valid MemberUpdateRequest request, @PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMemberById(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.deleteById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponse> changeMemberStatusById(@PathVariable UUID id, @RequestParam MemberStatus status) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.changeStatusById(id, status));
    }

    @PostMapping("/{memberId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable UUID memberId, @PathVariable UUID bookId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.borrowBook(memberId, bookId));
    }
}
