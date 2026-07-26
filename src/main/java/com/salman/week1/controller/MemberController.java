package com.salman.week1.controller;

import com.salman.week1.model.dto.request.MemberCreateRequest;
import com.salman.week1.model.dto.request.MemberUpdateRequest;
import com.salman.week1.model.dto.response.MemberResponse;
import com.salman.week1.model.enums.MemberStatus;
import com.salman.week1.service.abstraction.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Member Management", description = "REST APIs for managing library members and borrowing operations")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "Create a new member", description = "Creates a new library member with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (Validation error)")
    })
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody @Valid MemberCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberService.createMember(request));
    }

    @Operation(summary = "Get all members", description = "Returns a paginated list of members, optionally filtered by status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<Page<MemberResponse>> getAllMembers(
            @Parameter(description = "Page index (zero-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Filter members by status (optional)", example = "ACTIVE")
            @RequestParam(required = false) MemberStatus status
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.getAll(page, size, status));
    }

    @Operation(summary = "Get member by ID", description = "Returns details of a specific member based on the provided UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member found"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(
            @Parameter(description = "Unique ID of the member (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.getById(id));
    }

    @Operation(summary = "Update member by ID", description = "Updates details of an existing member based on the ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (Validation error)"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMemberById(
            @RequestBody @Valid MemberUpdateRequest request,
            @Parameter(description = "Unique ID of the member (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.updateById(id, request));
    }

    @Operation(summary = "Delete member by ID", description = "Deletes a member record from the system based on the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMemberById(
            @Parameter(description = "Unique ID of the member (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.deleteById(id));
    }

    @Operation(summary = "Change member status", description = "Updates the status of a specific member (e.g., ACTIVE, BLOCKED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member status successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status value provided"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponse> changeMemberStatusById(
            @Parameter(description = "Unique ID of the member (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,

            @Parameter(description = "New status to be assigned to the member", example = "ACTIVE")
            @RequestParam MemberStatus status
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.changeStatusById(id, status));
    }

    @Operation(summary = "Borrow a book", description = "Assigns a specific book to a member as borrowed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully borrowed"),
            @ApiResponse(responseCode = "400", description = "Book is not available for borrowing or member status is invalid"),
            @ApiResponse(responseCode = "404", description = "Member or book not found")
    })
    @PostMapping("/{memberId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(
            @Parameter(description = "Unique ID of the member borrowing the book", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID memberId,

            @Parameter(description = "Unique ID of the book to be borrowed", example = "987e6543-e21b-12d3-a456-426614174000")
            @PathVariable UUID bookId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(memberService.borrowBook(memberId, bookId));
    }
}