package com.devskilltracker.consumer.service;

import com.devskilltracker.consumer.model.SkillEventEntity;
import com.devskilltracker.consumer.model.TechnologyProgressEntity;
import com.devskilltracker.consumer.repository.SkillEventRepository;
import com.devskilltracker.consumer.repository.TechnologyProgressRepository;
import com.devskilltracker.events.EventType;
import com.devskilltracker.events.SkillCompletedEvent;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SkillProgressServiceTest {
    @Test
    void storesEventAndUpdatesEachTechnology() {
        SkillEventRepository eventRepository = mock(SkillEventRepository.class);
        TechnologyProgressRepository progressRepository = mock(TechnologyProgressRepository.class);
        SkillCompletedEvent event = new SkillCompletedEvent(
                UUID.randomUUID(),
                "fonte",
                "dev-skill-tracker",
                "Kafka Basics",
                EventType.PROJECT_COMPLETED,
                List.of("Java", "Kafka"),
                25,
                Instant.now()
        );

        when(eventRepository.save(any(SkillEventEntity.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(progressRepository.upsertProgress(eq("fonte"), any(String.class), eq(25)))
                .thenReturn(Mono.just(new TechnologyProgressEntity(1L, "fonte", "Java", 1, 25, Instant.now())));

        SkillProgressService service = new SkillProgressService(eventRepository, progressRepository);

        StepVerifier.create(service.apply(event)).verifyComplete();

        verify(progressRepository).upsertProgress("fonte", "Java", 25);
        verify(progressRepository).upsertProgress("fonte", "Kafka", 25);
    }

    @Test
    void returnsProgressOrderedByRepositoryQuery() {
        TechnologyProgressRepository progressRepository = mock(TechnologyProgressRepository.class);
        SkillProgressService service = new SkillProgressService(mock(SkillEventRepository.class), progressRepository);
        TechnologyProgressEntity java = new TechnologyProgressEntity(1L, "fonte", "Java", 2, 80, Instant.now());

        when(progressRepository.findByUserIdOrderByProgressPointsDesc("fonte")).thenReturn(Flux.just(java));

        StepVerifier.create(service.progressFor("fonte"))
                .expectNext(java)
                .verifyComplete();
    }
}
