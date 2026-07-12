package com.devskilltracker.auth.web;

import com.devskilltracker.auth.service.ApiKeyService;
import com.devskilltracker.auth.service.AuthService;
import com.devskilltracker.auth.web.dto.ApiKeyCreatedResponse;
import com.devskilltracker.auth.web.dto.ApiKeyResponse;
import com.devskilltracker.auth.web.dto.AuthResponse;
import com.devskilltracker.auth.web.dto.CreateApiKeyRequest;
import com.devskilltracker.auth.web.dto.LoginRequest;
import com.devskilltracker.auth.web.dto.ProfileResponse;
import com.devskilltracker.auth.web.dto.RegisterRequest;
import com.devskilltracker.auth.web.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final ApiKeyService apiKeyService;

    public AuthController(AuthService authService, ApiKeyService apiKeyService) {
        this.authService = authService;
        this.apiKeyService = apiKeyService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/profile")
    public Mono<ProfileResponse> profile(@AuthenticationPrincipal String userId) {
        return authService.profile(userId);
    }

    @PatchMapping("/profile")
    public Mono<ProfileResponse> updateProfile(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return authService.updateProfile(userId, request);
    }

    @PostMapping("/api-keys")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ApiKeyCreatedResponse> createApiKey(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody(required = false) CreateApiKeyRequest request
    ) {
        String label = request == null ? null : request.label();
        return apiKeyService.createKey(userId, label);
    }

    @GetMapping("/api-keys")
    public Flux<ApiKeyResponse> listApiKeys(@AuthenticationPrincipal String userId) {
        return apiKeyService.listKeys(userId);
    }

    @DeleteMapping("/api-keys/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> revokeApiKey(@AuthenticationPrincipal String userId, @PathVariable UUID id) {
        return apiKeyService.revokeKey(userId, id);
    }
}
