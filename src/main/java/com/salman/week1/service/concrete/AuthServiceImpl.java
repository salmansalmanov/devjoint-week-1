package com.salman.week1.service.concrete;

import com.salman.week1.exception.custom.InvalidStatusException;
import com.salman.week1.exception.custom.InvalidTokenException;
import com.salman.week1.exception.custom.NotFoundException;
import com.salman.week1.model.dto.request.LoginRequest;
import com.salman.week1.model.dto.request.RefreshRequest;
import com.salman.week1.model.dto.response.LoginResponse;
import com.salman.week1.model.dto.response.RefreshResponse;
import com.salman.week1.model.entity.Member;
import com.salman.week1.model.entity.User;
import com.salman.week1.model.enums.MemberStatus;
import com.salman.week1.repository.UserRepository;
import com.salman.week1.service.abstraction.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with username: " + request.getUsername()));
        if (user instanceof Member member) {
            if (member.getStatus().equals(MemberStatus.BLOCKED)) {
                throw new InvalidStatusException("User is blocked");
            }
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        String accessToken = jwtService.generateAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public RefreshResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        String username;
        try {
            username = jwtService.extractRefreshTokenUsername(refreshToken);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        if (!jwtService.isRefreshTokenValid(refreshToken, username)) {
            throw new InvalidTokenException("Refresh token is invalid or expired");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));

        if (user instanceof Member member) {
            if (member.getStatus().equals(MemberStatus.BLOCKED)) {
                throw new InvalidStatusException("User is blocked");
            }
        }
        String newAccessToken = jwtService.generateAccessToken(user.getUsername(), user.getRole());
        String newRefreshToken = jwtService.generateRefreshToken(user.getUsername());
        return new RefreshResponse(newAccessToken, newRefreshToken);
    }
}
