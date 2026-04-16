package com.dauphine.blogger.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name = "Hello world API",
        description = "My first hello world endpoints"
)
public class HelloWorldController {

    @GetMapping("/")
    public ResponseEntity<Void> home() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/swagger-ui/index.html")
                .build();
    }

    @GetMapping("/api")
    public String apiHome() {
        return "Blogger API is running";
    }

    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello World!";
    }

    @GetMapping("/hello")
    @Operation(
            summary = "Hello by query param endpoint",
            description = "Returns 'Hello {name}' by query parameter"
    )
    public String helloByName(@RequestParam String name) {
        return "Hello " + name;
    }

    @GetMapping("/hello/{name}")
    @Operation(
            summary = "Hello by name endpoint",
            description = "Returns 'Hello {name}' by path variable"
    )
    public String hello(@Parameter(description = "Name to greet") @PathVariable String name) {
        return "Hello " + name;
    }
}
