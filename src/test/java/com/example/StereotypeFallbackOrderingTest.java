package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.faulttolerance.api.CircuitBreakerMaintenance;

@QuarkusTest
class StereotypeFallbackOrderingTest {

    @Inject
    GuardedService service;

    @Inject
    MockDependencyBean mockDependency;

    @Inject
    CircuitBreakerMaintenance circuitBreakerMaintenance;

    @BeforeEach
    void setUp() {
        mockDependency.reset();
        circuitBreakerMaintenance.resetAll();
    }

    @Test
    void retryIteratesBeforeFallback_whenAnnotationsCrossClassAndMethodLevels() {
        AtomicInteger callCount = new AtomicInteger(0);
        mockDependency.setBehavior(() -> {
            callCount.incrementAndGet();
            throw new RuntimeException("transient failure");
        });

        service.guardedMethod();

        assertThat(callCount.get()).isEqualTo(3);
    }

    @Test
    void circuitBreakerAccumulatesFailures_whenFallbackIsAtMethodLevel() {
        AtomicInteger callCount = new AtomicInteger(0);
        mockDependency.setBehavior(() -> {
            callCount.incrementAndGet();
            throw new RuntimeException("always fail");
        });

        for (int i = 0; i < 6; i++) {
            service.guardedMethod();
        }

        callCount.set(0);
        service.guardedMethod();

        assertThat(callCount.get()).isZero();
    }

    @Test
    void returnsRealValue_whenNoExceptionThrown() {
        mockDependency.setBehavior(() -> "success");

        assertThat(service.guardedMethod()).isEqualTo("success");
    }
}
