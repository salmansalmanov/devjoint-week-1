package com.salman.week1.service.abstraction;

import com.salman.week1.model.dto.request.LoginRequest;
import com.salman.week1.model.dto.request.RefreshRequest;
import com.salman.week1.model.dto.response.LoginResponse;
import com.salman.week1.model.dto.response.RefreshResponse;
import jakarta.validation.Valid;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    RefreshResponse refresh(@Valid RefreshRequest request);
}
