package com.github.ioridazo.spring.boot.resilience4j.sample.retry;

import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Slf4j
@Service
public class RetryService {

    private static final String RETRY_NAME = "sample";

    private final RestTemplate restTemplate;
    private final RetryRegistry retryRegistry;

    public RetryService(final RestTemplate restTemplate, final RetryRegistry retryRegistry) {
        this.restTemplate = restTemplate;
        this.retryRegistry = retryRegistry;
    }

    public String retry() {
        return withRetry(() -> restTemplate.getForObject("http://localhost:8080/server/failure", String.class));
    }

    private String withRetry(Supplier<String> supplier) {
        return retryRegistry.retry(RETRY_NAME)
                .executeSupplier(supplier);
    }
}
