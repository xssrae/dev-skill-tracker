package com.devskilltracker.consumer.web;

import com.devskilltracker.consumer.model.TechnologyProgressEntity;
import com.devskilltracker.consumer.service.SkillProgressService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final SkillProgressService progressService;

    public DashboardController(SkillProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping(value = "/{userId}/progress", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<TechnologyProgressEntity> progress(@PathVariable String userId) {
        return progressService.progressFor(userId);
    }

    @GetMapping(value = "/{userId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TechnologyProgressEntity> progressStream(@PathVariable String userId) {
        return progressService.progressFor(userId);
    }
}
