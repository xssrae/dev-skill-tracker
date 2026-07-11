package com.devskilltracker.consumer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("technology_progress")
public record TechnologyProgressEntity(
        @Id Long id,
        String userId,
        String technology,
        int completedItems,
        int progressPoints,
        Instant lastUpdatedAt
) {
}
