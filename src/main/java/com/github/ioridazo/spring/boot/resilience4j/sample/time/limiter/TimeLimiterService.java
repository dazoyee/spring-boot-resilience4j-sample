package com.github.ioridazo.spring.boot.resilience4j.sample.time.limiter;

import com.github.ioridazo.spring.boot.resilience4j.sample.exception.SampleException;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

@Slf4j
@Service
public class TimeLimiterService {

    private static final String TIME_LIMITER_NAME = "sample";

    private final RestTemplate restTemplate;
    private final TimeLimiterRegistry timeLimiterRegistry;

    public TimeLimiterService(final RestTemplate restTemplate, final TimeLimiterRegistry timeLimiterRegistry) {
        this.restTemplate = restTemplate;
        this.timeLimiterRegistry = timeLimiterRegistry;
    }

    public String success() {
        return withTimeLimit(() -> restTemplate.getForObject("http://localhost:8080/server/delay/3", String.class));
    }

    public String timeout() {
        return withTimeLimit(() -> restTemplate.getForObject("http://localhost:8080/server/delay/5", String.class));
    }

    private String withTimeLimit(Supplier<String> supplier) {
        try {
            return timeLimiterRegistry.timeLimiter(TIME_LIMITER_NAME)
                    .executeFutureSupplier(() -> CompletableFuture.supplyAsync(supplier));
        } catch (TimeoutException e) {
            log.error("method time out", e);
            return "method time out!";
        } catch (Exception e) {
            throw new SampleException(e);
        }
    }
}
