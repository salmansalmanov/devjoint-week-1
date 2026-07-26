package com.salman.week1.service.concrete;

import com.salman.week1.model.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${spring.security.access-token.secret}")
    private String accessTokenSecret;

    @Value("${spring.security.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${spring.security.refresh-token.secret}")
    private String refreshTokenSecret;

    @Value("${spring.security.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(String username, Role role) {
        return buildToken(username, role, accessTokenExpiration, getSigningKey(accessTokenSecret));
    }

    public String generateRefreshToken(String username) {
        return buildToken(username, null, refreshTokenExpiration, getSigningKey(refreshTokenSecret));
    }

    private String buildToken(String username, Role role, long expiration, Key key) {
        var builder = Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration));

        if (role != null) {
            builder.claim("role", role.name());
        }

        return builder
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token, accessTokenSecret).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token, accessTokenSecret).get("role", String.class);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token, accessTokenSecret).getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    public String extractRefreshTokenUsername(String token) {
        return extractAllClaims(token, refreshTokenSecret).getSubject();
    }

    public boolean isRefreshTokenExpired(String token) {
        return extractAllClaims(token, refreshTokenSecret).getExpiration().before(new Date());
    }

    public boolean isRefreshTokenValid(String token, String username) {
        String extractedUsername = extractRefreshTokenUsername(token);
        return extractedUsername.equals(username) && !isRefreshTokenExpired(token);
    }

    private Key getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Claims extractAllClaims(String token, String secret) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}