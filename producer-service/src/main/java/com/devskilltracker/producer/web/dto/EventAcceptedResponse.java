package com.devskilltracker.producer.web.dto;

import java.util.UUID;

public record EventAcceptedResponse(UUID eventId, String status) {
}
