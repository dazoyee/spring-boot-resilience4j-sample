package com.github.ioridazo.spring.boot.resilience4j.sample.circuit.breaker;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
@Service
public class CircuitBreakerService {

    private static final String CIRCUIT_BREAKER_NAME = "sample";
    private static final String CIRCUIT_BREAKER_NAME2 = "sample2";

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

    public String failurePart2() {
        try {
            return circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME2)
                    .decorateSupplier(() -> {
                        try {
                            return restTemplate.getForObject("http://localhost:8080/server/not-found", String.class);
                        } catch (RestClientResponseException e) {
                            log.warn("RestClientResponseException");
                            throw new CircuitBreakerRecordException(e);
                        }
                    })
                    .get();
        } catch (CallNotPermittedException e) {
            log.error("circuit breaker open", e);
            return "circuit breaker open!";
        }
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

    static class CircuitBreakerRecordException extends RuntimeException {

        /**
         * Constructs a new runtime exception with the specified cause and a
         * detail message of {@code (cause==null ? null : cause.toString())}
         * (which typically contains the class and detail message of
         * {@code cause}).  This constructor is useful for runtime exceptions
         * that are little more than wrappers for other throwables.
         *
         * @param cause the cause (which is saved for later retrieval by the
         *              {@link #getCause()} method).  (A {@code null} value is
         *              permitted, and indicates that the cause is nonexistent or
         *              unknown.)
         * @since 1.4
         */
        public CircuitBreakerRecordException(final Throwable cause) {
            super(cause);
        }
    }

    @SuppressWarnings("unused")
    public static class RecordFailurePredicate implements Predicate<Throwable> {

        @Override
        public boolean test(final Throwable throwable) {
            return throwable instanceof CircuitBreakerRecordException;
        }
    }
}
