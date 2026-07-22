package com.salman.week1.controller;

import com.salman.week1.model.dto.request.MemberRequest;
import com.salman.week1.model.dto.response.MemberResponse;
import com.salman.week1.model.enums.MemberStatus;
import com.salman.week1.service.abstraction.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
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
}
