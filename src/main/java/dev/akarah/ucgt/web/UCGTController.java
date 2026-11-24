package dev.akarah.ucgt.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UCGTController {
    @GetMapping("/v1/hello")
    public String hello() {
        return "Hello there!";
    }
}
