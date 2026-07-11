package com.devskilltracker.consumer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("skill_events")
public record SkillEventEntity(
        @Id UUID eventId,
        String userId,
        String repository,
        String projectName,
        String eventType,
        String technologies,
        int progressDelta,
        Instant completedAt
) {
}
