package com.devskilltracker.consumer.repository;

import com.devskilltracker.consumer.model.SkillEventEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface SkillEventRepository extends ReactiveCrudRepository<SkillEventEntity, UUID> {
}
