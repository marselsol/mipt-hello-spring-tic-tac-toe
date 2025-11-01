package com.example.mipt_hello_spring_tic_tac_toe;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {
    private final MessageService service;

    @GetMapping("/")
    public String root() {
        return "Hello, World!";
    }

    @GetMapping("/messages")
    public Iterable<Message> getMessages() {
        return service.getAllMessages();
    }
}

