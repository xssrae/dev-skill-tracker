package com.devskilltracker.producer.web;

import com.devskilltracker.events.EventType;
import com.devskilltracker.producer.web.dto.CompletionWebhookRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CompletionWebhookRequestTest {
    @Test
    void defaultsEventTypeWhenMissing() {
        CompletionWebhookRequest request = new CompletionWebhookRequest(
                "fonte",
                "dev-skill-tracker",
                "Kafka Basics",
                null,
                List.of("Java", "Kafka"),
                20,
                null
        );

        assertThat(request.toEvent().type()).isEqualTo(EventType.EXERCISE_COMPLETED);
        assertThat(request.toEvent().eventId()).isNotNull();
        assertThat(request.toEvent().completedAt()).isNotNull();
    }
}
