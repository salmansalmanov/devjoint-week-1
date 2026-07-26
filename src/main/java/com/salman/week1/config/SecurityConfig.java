package com.salman.week1.config;

import com.salman.week1.exception.handler.CustomAccessDeniedHandler;
import com.salman.week1.exception.handler.JwtAuthenticationEntryPoint;
import com.salman.week1.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/authors").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/authors").hasAnyRole("AUTHOR", "MEMBER")
                        .requestMatchers(HttpMethod.GET, "/v1/authors/*").hasAnyRole("AUTHOR", "MEMBER")
                        .requestMatchers(HttpMethod.PUT, "/v1/authors/*").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.DELETE, "/v1/authors/*").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.POST, "/v1/books").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.GET, "/v1/books").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/books/*").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/v1/books/*").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.DELETE, "/v1/books/*").hasRole("AUTHOR")
                        .requestMatchers(HttpMethod.POST, "/v1/members").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/members").hasAnyRole("MEMBER", "AUTHOR")
                        .requestMatchers(HttpMethod.GET, "/v1/members/*").hasAnyRole("MEMBER", "AUTHOR")
                        .requestMatchers(HttpMethod.PUT, "/v1/members/*").hasRole("MEMBER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/members/*").hasRole("MEMBER")
                        .anyRequest().hasAnyRole("AUTHOR", "MEMBER"))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }
}
