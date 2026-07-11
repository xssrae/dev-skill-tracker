package com.devskilltracker.producer.web.dto;

import com.devskilltracker.events.EventType;
import com.devskilltracker.events.SkillCompletedEvent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CompletionWebhookRequest(
        @NotBlank String userId,
        @NotBlank String repository,
        @NotBlank String projectName,
        EventType type,
        @NotEmpty List<@NotBlank String> technologies,
        @Min(1) @Max(100) int progressDelta,
        Instant completedAt
) {
    public SkillCompletedEvent toEvent() {
        EventType eventType = type == null ? EventType.EXERCISE_COMPLETED : type;
        return new SkillCompletedEvent(
                UUID.randomUUID(),
                userId,
                repository,
                projectName,
                eventType,
                technologies,
                progressDelta,
                completedAt
        );
    }
}
