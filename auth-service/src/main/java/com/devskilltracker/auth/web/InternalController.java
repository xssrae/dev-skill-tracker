package com.devskilltracker.auth.web;

import com.devskilltracker.auth.service.ApiKeyService;
import com.devskilltracker.auth.service.AuthService;
import com.devskilltracker.auth.web.dto.UserIdResponse;
import com.devskilltracker.auth.web.dto.VisibleUserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/internal")
public class InternalController {
    private final ApiKeyService apiKeyService;
    private final AuthService authService;

    public InternalController(ApiKeyService apiKeyService, AuthService authService) {
        this.apiKeyService = apiKeyService;
        this.authService = authService;
    }

    @GetMapping("/users/resolve-api-key")
    public Mono<UserIdResponse> resolveApiKey(@RequestHeader("X-Api-Key") String apiKey) {
        return apiKeyService.resolveUserId(apiKey).map(UserIdResponse::new);
    }

    @GetMapping("/users/visible-ids")
    public Flux<VisibleUserResponse> visibleUsers() {
        return authService.visibleUsers();
    }
}
