package com.salman.week1.controller;

import com.salman.week1.model.dto.request.LoginRequest;
import com.salman.week1.model.dto.request.RefreshRequest;
import com.salman.week1.model.dto.response.LoginResponse;
import com.salman.week1.model.dto.response.RefreshResponse;
import com.salman.week1.service.abstraction.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.refresh(request));
    }
}
