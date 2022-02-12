package com.github.ioridazo.spring.boot.resilience4j.sample.bulkhead;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BulkheadController {

    private final BulkheadService service;

    public BulkheadController(BulkheadService service) {
        this.service = service;
    }

    @GetMapping("delay")
    public String delay() throws InterruptedException {
        Thread.sleep(1000);
        log.info("access delay!");
        return service.delay();
    }
}
