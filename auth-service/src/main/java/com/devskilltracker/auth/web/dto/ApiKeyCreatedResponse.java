package com.devskilltracker.auth.web.dto;

import java.time.Instant;
import java.util.UUID;

public record ApiKeyCreatedResponse(UUID id, String apiKey, String label, Instant createdAt) {
}
