package com.devskilltracker.consumer.service;

import com.devskilltracker.consumer.model.SkillEventEntity;
import com.devskilltracker.consumer.model.TechnologyProgressEntity;
import com.devskilltracker.consumer.repository.SkillEventRepository;
import com.devskilltracker.consumer.repository.TechnologyProgressRepository;
import com.devskilltracker.events.SkillCompletedEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SkillProgressService {
    private final SkillEventRepository eventRepository;
    private final TechnologyProgressRepository progressRepository;

    public SkillProgressService(SkillEventRepository eventRepository, TechnologyProgressRepository progressRepository) {
        this.eventRepository = eventRepository;
        this.progressRepository = progressRepository;
    }

    public Mono<Void> apply(SkillCompletedEvent event) {
        SkillEventEntity entity = new SkillEventEntity(
                event.eventId(),
                event.userId(),
                event.repository(),
                event.projectName(),
                event.type().name(),
                String.join(",", event.technologies()),
                event.progressDelta(),
                event.completedAt()
        );

        return eventRepository.save(entity)
                .onErrorResume(DuplicateKeyException.class, ignored -> Mono.empty())
                .flatMapMany(saved -> Flux.fromIterable(event.technologies()))
                .flatMap(technology -> progressRepository.upsertProgress(event.userId(), technology, event.progressDelta()))
                .then();
    }

    public Flux<TechnologyProgressEntity> progressFor(String userId) {
        return progressRepository.findByUserIdOrderByProgressPointsDesc(userId);
    }
}
