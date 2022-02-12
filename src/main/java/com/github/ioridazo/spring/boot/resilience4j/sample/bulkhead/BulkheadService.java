package com.github.ioridazo.spring.boot.resilience4j.sample.bulkhead;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Slf4j
@Service
public class BulkheadService {

    private static final String BULKHEAD_NAME = "sample";

    private final RestTemplate restTemplate;
    private final BulkheadRegistry bulkheadRegistry;

    public BulkheadService(final RestTemplate restTemplate, final BulkheadRegistry bulkheadRegistry) {
        this.restTemplate = restTemplate;
        this.bulkheadRegistry = bulkheadRegistry;
    }

    public String delay() {
        return withBulkhead(() -> restTemplate.getForObject("http://localhost:8080/server/delay/5", String.class));
    }

    private String withBulkhead(Supplier<String> supplier) {
        try {
            return bulkheadRegistry.bulkhead(BULKHEAD_NAME)
                    .executeSupplier(supplier);
        } catch (BulkheadFullException e) {
            log.error("bulkhead full", e);
            return "bulkhead full!";
        }
    }
}
