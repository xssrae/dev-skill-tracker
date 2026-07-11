package com.devskilltracker.consumer.repository;

import com.devskilltracker.consumer.model.TechnologyProgressEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TechnologyProgressRepository extends ReactiveCrudRepository<TechnologyProgressEntity, Long> {
    Flux<TechnologyProgressEntity> findByUserIdOrderByProgressPointsDesc(String userId);

    Mono<TechnologyProgressEntity> findByUserIdAndTechnology(String userId, String technology);

    @Query("""
            INSERT INTO technology_progress (user_id, technology, completed_items, progress_points, last_updated_at)
            VALUES (:userId, :technology, 1, :progressDelta, now())
            ON CONFLICT (user_id, technology)
            DO UPDATE SET
              completed_items = technology_progress.completed_items + 1,
              progress_points = LEAST(100, technology_progress.progress_points + :progressDelta),
              last_updated_at = now()
            RETURNING id, user_id, technology, completed_items, progress_points, last_updated_at
            """)
    Mono<TechnologyProgressEntity> upsertProgress(String userId, String technology, int progressDelta);
}
