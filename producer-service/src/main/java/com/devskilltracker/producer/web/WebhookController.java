package com.devskilltracker.producer.web;

import com.devskilltracker.producer.web.dto.CompletionWebhookRequest;
import com.devskilltracker.producer.web.dto.EventAcceptedResponse;
import com.devskilltracker.producer.service.SkillEventPublisher;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {
    private final SkillEventPublisher publisher;

    public WebhookController(SkillEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/completions")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<EventAcceptedResponse> captureCompletion(@Valid @RequestBody CompletionWebhookRequest request) {
        return publisher.publish(request.toEvent())
                .map(event -> new EventAcceptedResponse(event.eventId(), "accepted"));
    }
}
