package com.example.journalApp.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/health-check")
    public String healthChack() {
        return "Ok i'll check it later";
    }
}
