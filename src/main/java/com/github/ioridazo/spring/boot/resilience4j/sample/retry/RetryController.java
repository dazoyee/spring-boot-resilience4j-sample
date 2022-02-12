package com.github.ioridazo.spring.boot.resilience4j.sample.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RetryController {

    private final RetryService service;

    public RetryController(final RetryService service) {
        this.service = service;
    }

    @GetMapping("retry")
    public String retry() {
        log.info("access retry");
        return service.retry();
    }
}
