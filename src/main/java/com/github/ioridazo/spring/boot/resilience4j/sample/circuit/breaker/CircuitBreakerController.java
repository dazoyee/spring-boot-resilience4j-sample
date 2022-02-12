package com.github.ioridazo.spring.boot.resilience4j.sample.circuit.breaker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CircuitBreakerController {

    private final CircuitBreakerService service;

    public CircuitBreakerController(CircuitBreakerService service) {
        this.service = service;
    }

    @GetMapping("success")
    public String success() {
        log.info("access success!");
        return service.success();
    }

    @GetMapping("failure")
    public String failure() {
        log.info("access failure!");
        return service.failure();
    }
}
