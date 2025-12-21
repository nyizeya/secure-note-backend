package com.secure.notes.web.resources;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/hello")
public class HelloResource {

    @GetMapping
    public String hello() {
        log.info("hello world");
        return "Hello, World";
    }

}
