package com.devskilltracker.auth.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CryptoUtilsTest {
    @Test
    void hashesConsistently() {
        assertThat(CryptoUtils.sha256("abc")).isEqualTo(CryptoUtils.sha256("abc"));
        assertThat(CryptoUtils.sha256("abc")).isNotEqualTo(CryptoUtils.sha256("def"));
    }

    @Test
    void generatesApiKeyWithPrefix() {
        assertThat(CryptoUtils.generateApiKey()).startsWith("dst_");
    }
}
