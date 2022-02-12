package com.github.ioridazo.spring.boot.resilience4j.sample.server;

import com.github.ioridazo.spring.boot.resilience4j.sample.exception.SampleException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("server")
@RestController
public class ServerController {

    @GetMapping("hello")
    public String hello() {
        return "Server Hello!";
    }

    @GetMapping("failure")
    public String failure() {
        throw new SampleException();
    }

    @GetMapping("delay/{second}")
    public String delay(@PathVariable("second") Integer second) {
        try {
            Thread.sleep(second * 1_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "delay " + second + "s!";
    }
}
