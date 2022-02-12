package com.github.ioridazo.spring.boot.resilience4j.sample.circuit.breaker;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.function.Supplier;

@Slf4j
@Service
public class CircuitBreakerService {

    private static final String CIRCUIT_BREAKER_NAME = "sample";

    private final RestTemplate restTemplate;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerService(
            RestTemplate restTemplate,
            CircuitBreakerRegistry circuitBreakerRegistry) {
        this.restTemplate = restTemplate;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public String success() {
        return withCircuitBreaker(() -> restTemplate.getForObject("http://localhost:8080/server/hello", String.class));
    }

    public String failure() {
        if (new Random().nextInt() % 2 == 0) {
            return withCircuitBreaker(() -> restTemplate.getForObject("http://localhost:8080/server/failure", String.class));
        } else {
            return withCircuitBreaker(() -> restTemplate.getForObject("http://localhost:8080/server/not-found", String.class));
        }
    }

    private String withCircuitBreaker(Supplier<String> supplier) {
        try {
            return circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME)
                    .decorateSupplier(supplier)
                    .get();
        } catch (CallNotPermittedException e) {
            log.error("circuit breaker open", e);
            return "circuit breaker open!";
        }
    }
}
