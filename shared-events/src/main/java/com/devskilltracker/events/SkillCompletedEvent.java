package com.devskilltracker.events;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SkillCompletedEvent(
        UUID eventId,
        String userId,
        String repository,
        String projectName,
        EventType type,
        List<String> technologies,
        int progressDelta,
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant completedAt
) {
    public SkillCompletedEvent {
        if (eventId == null) {
            eventId = UUID.randomUUID();
        }
        if (completedAt == null) {
            completedAt = Instant.now();
        }
        if (technologies == null) {
            technologies = List.of();
        }
    }
}
