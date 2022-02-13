package com.github.ioridazo.spring.boot.resilience4j.sample.circuit.breaker;

import com.github.ioridazo.spring.boot.resilience4j.sample.config.SampleConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CircuitBreakerServiceTest {

    private static final String CIRCUIT_BREAKER_NAME2 = "sample2";

    private final RestTemplate restTemplate = new SampleConfig().restTemplate();
    private final CircuitBreakerRegistry circuitBreakerRegistry = new CircuitBreakerRegistry.Builder()
            .withCircuitBreakerConfig(
                    new CircuitBreakerConfig.Builder()
                            .failureRateThreshold(100)
                            .permittedNumberOfCallsInHalfOpenState(1)
                            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                            .slidingWindowSize(1)
                            .recordException(new CircuitBreakerService.RecordFailurePredicate())
                            .build()
            )
            .build();
    private final CircuitBreakerService service = new CircuitBreakerService(restTemplate, circuitBreakerRegistry);

    @Test
    void open() {
        try {
            // for open
            service.failurePart2();
        } catch (Exception ignored) {
            assertEquals("circuit breaker open!", service.failurePart2());
            assertEquals("OPEN", circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME2).getState().name());

        }
    }
}