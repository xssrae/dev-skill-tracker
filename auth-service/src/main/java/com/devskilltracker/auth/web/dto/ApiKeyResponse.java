package com.devskilltracker.auth.web.dto;

import java.time.Instant;
import java.util.UUID;

public record ApiKeyResponse(UUID id, String label, Instant createdAt) {
}
