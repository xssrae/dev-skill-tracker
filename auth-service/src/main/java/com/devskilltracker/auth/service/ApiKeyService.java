package com.devskilltracker.auth.service;

import com.devskilltracker.auth.model.ApiKeyEntity;
import com.devskilltracker.auth.repository.ApiKeyRepository;
import com.devskilltracker.auth.util.CryptoUtils;
import com.devskilltracker.auth.web.dto.ApiKeyCreatedResponse;
import com.devskilltracker.auth.web.dto.ApiKeyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public Mono<ApiKeyCreatedResponse> createKey(String userId, String label) {
        String rawKey = CryptoUtils.generateApiKey();
        ApiKeyEntity entity = new ApiKeyEntity(
                CryptoUtils.randomUuid(),
                userId,
                CryptoUtils.sha256(rawKey),
                label,
                Instant.now(),
                null
        );

        return apiKeyRepository.save(entity)
                .map(saved -> new ApiKeyCreatedResponse(saved.id(), rawKey, saved.label(), saved.createdAt()));
    }

    public Flux<ApiKeyResponse> listKeys(String userId) {
        return apiKeyRepository.findByUserIdAndRevokedAtIsNullOrderByCreatedAtDesc(userId)
                .map(key -> new ApiKeyResponse(key.id(), key.label(), key.createdAt()));
    }

    public Mono<Void> revokeKey(String userId, UUID keyId) {
        return apiKeyRepository.findById(keyId)
                .filter(key -> key.userId().equals(userId))
                .filter(key -> key.revokedAt() == null)
                .flatMap(key -> apiKeyRepository.save(new ApiKeyEntity(
                        key.id(),
                        key.userId(),
                        key.keyHash(),
                        key.label(),
                        key.createdAt(),
                        Instant.now()
                )))
                .then()
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "API key not found")));
    }

    public Mono<String> resolveUserId(String rawApiKey) {
        if (rawApiKey == null || rawApiKey.isBlank()) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing API key"));
        }

        return apiKeyRepository.findByKeyHashAndRevokedAtIsNull(CryptoUtils.sha256(rawApiKey))
                .map(ApiKeyEntity::userId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key")));
    }
}
