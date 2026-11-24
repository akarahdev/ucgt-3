package dev.akarah.ucgt.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UCGTController {
    @GetMapping("/v1/hello")
    public HelloWorldResponse hello() {
        return new HelloWorldResponse("world");
    }
}
