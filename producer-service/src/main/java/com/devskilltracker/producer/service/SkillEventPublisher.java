package com.devskilltracker.producer.service;

import com.devskilltracker.events.SkillCompletedEvent;
import com.devskilltracker.events.Topics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SkillEventPublisher {
    private final KafkaTemplate<String, SkillCompletedEvent> kafkaTemplate;

    public SkillEventPublisher(KafkaTemplate<String, SkillCompletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<SkillCompletedEvent> publish(SkillCompletedEvent event) {
        return Mono.fromFuture(kafkaTemplate.send(Topics.SKILL_COMPLETED, event.userId(), event))
                .thenReturn(event);
    }
}
