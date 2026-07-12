package com.devskilltracker.auth.service;

import com.devskilltracker.auth.config.JwtProperties;
import com.devskilltracker.auth.security.JwtService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {
    @Test
    void generatesAndValidatesToken() {
        JwtService jwtService = new JwtService(new JwtProperties("test-secret-key-with-enough-length", 24));
        String token = jwtService.generateToken("fonte");
        assertThat(jwtService.validateAndExtractSubject(token)).isEqualTo("fonte");
    }
}
