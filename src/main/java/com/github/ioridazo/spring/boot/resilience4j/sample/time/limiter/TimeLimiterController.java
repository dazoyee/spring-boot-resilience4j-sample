package com.github.ioridazo.spring.boot.resilience4j.sample.time.limiter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TimeLimiterController {

    private final TimeLimiterService service;

    public TimeLimiterController(final TimeLimiterService service) {
        this.service = service;
    }

    @GetMapping("time-in")
    public String success() {
        log.info("access time-in");
        return service.success();
    }

    @GetMapping("timeout")
    public String timeout() {
        log.info("access timeout");
        return service.timeout();
    }
}
