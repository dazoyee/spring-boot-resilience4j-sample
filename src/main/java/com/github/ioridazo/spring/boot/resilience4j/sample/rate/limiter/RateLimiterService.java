package com.github.ioridazo.spring.boot.resilience4j.sample.rate.limiter;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Slf4j
@Service
public class RateLimiterService {

    private static final String RATE_LIMITER_NAME = "sample";

    private final RestTemplate restTemplate;
    private final RateLimiterRegistry rateLimiterRegistry;

    public RateLimiterService(final RestTemplate restTemplate, final RateLimiterRegistry rateLimiterRegistry) {
        this.restTemplate = restTemplate;
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    public String rateLimit() {
        return withRateLimit(() -> restTemplate.getForObject("http://localhost:8080/server/hello", String.class));
    }

    private String withRateLimit(Supplier<String> supplier) {
        try {
            return rateLimiterRegistry.rateLimiter(RATE_LIMITER_NAME)
                    .executeSupplier(supplier);
        } catch (RequestNotPermitted rateLimit) {
            log.error("no permit request", rateLimit);
            return "no permit request!";
        }
    }
}
