package com.devskilltracker.auth.web.dto;

public record ProfileResponse(
        String userId,
        String displayName,
        boolean leaderboardVisible
) {
}
