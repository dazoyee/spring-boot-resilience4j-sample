package com.github.ioridazo.spring.boot.resilience4j.sample.rate.limiter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RateLimiterController {

    private final RateLimiterService service;

    public RateLimiterController(final RateLimiterService service) {
        this.service = service;
    }

    @GetMapping("rate-limit")
    public String rateLimit() {
        log.info("access rate-limit");
        return service.rateLimit();
    }
}
