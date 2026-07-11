package com.devskilltracker.consumer.kafka;

import com.devskilltracker.consumer.service.SkillProgressService;
import com.devskilltracker.events.SkillCompletedEvent;
import com.devskilltracker.events.Topics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SkillCompletedListener {
    private final SkillProgressService progressService;

    public SkillCompletedListener(SkillProgressService progressService) {
        this.progressService = progressService;
    }

    @KafkaListener(topics = Topics.SKILL_COMPLETED, groupId = "${spring.kafka.consumer.group-id}")
    public void onSkillCompleted(SkillCompletedEvent event) {
        progressService.apply(event).block();
    }
}
