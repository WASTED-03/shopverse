package com.shopverse.user_service;

import com.shopverse.user_service.dto.RegisterRequest;
import com.shopverse.user_service.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.shopverse.user_service.dto.LoginRequest;
import com.shopverse.user_service.dto.LoginResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public org.springframework.http.ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return org.springframework.http.ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/me")
    public org.springframework.http.ResponseEntity<String> me(
            org.springframework.security.core.Authentication authentication) {
        return org.springframework.http.ResponseEntity.ok("Authenticated user: " + authentication.getName());
    }
}
