package com.devskilltracker.auth.service;

import com.devskilltracker.auth.model.UserEntity;
import com.devskilltracker.auth.repository.UserRepository;
import com.devskilltracker.auth.security.JwtService;
import com.devskilltracker.auth.web.dto.AuthResponse;
import com.devskilltracker.auth.web.dto.LoginRequest;
import com.devskilltracker.auth.web.dto.ProfileResponse;
import com.devskilltracker.auth.web.dto.RegisterRequest;
import com.devskilltracker.auth.web.dto.UpdateProfileRequest;
import com.devskilltracker.auth.web.dto.VisibleUserResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Mono<AuthResponse> register(RegisterRequest request) {
        UserEntity user = new UserEntity(
                request.id(),
                request.displayName(),
                passwordEncoder.encode(request.password()),
                true,
                Instant.now()
        );

        return userRepository.save(user)
                .map(saved -> new AuthResponse(jwtService.generateToken(saved.id()), saved.id()))
                .onErrorMap(DuplicateKeyException.class,
                        ignored -> new ResponseStatusException(HttpStatus.CONFLICT, "User already exists"));
    }

    public Mono<AuthResponse> login(LoginRequest request) {
        return userRepository.findById(request.id())
                .filter(user -> passwordEncoder.matches(request.password(), user.passwordHash()))
                .map(user -> new AuthResponse(jwtService.generateToken(user.id()), user.id()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")));
    }

    public Mono<ProfileResponse> profile(String userId) {
        return userRepository.findById(userId)
                .map(user -> new ProfileResponse(user.id(), user.displayName(), user.leaderboardVisible()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    public Mono<ProfileResponse> updateProfile(String userId, UpdateProfileRequest request) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    boolean visible = request.leaderboardVisible() != null
                            ? request.leaderboardVisible()
                            : user.leaderboardVisible();
                    UserEntity updated = new UserEntity(
                            user.id(),
                            user.displayName(),
                            user.passwordHash(),
                            visible,
                            user.createdAt()
                    );
                    return userRepository.save(updated);
                })
                .map(user -> new ProfileResponse(user.id(), user.displayName(), user.leaderboardVisible()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    public Flux<VisibleUserResponse> visibleUsers() {
        return userRepository.findByLeaderboardVisibleTrue()
                .map(user -> new VisibleUserResponse(user.id(), user.displayName()));
    }
}
