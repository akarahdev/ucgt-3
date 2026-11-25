package dev.akarah.ucgt.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/v1/health")
    public int health() {
        return 1;
    }
}
