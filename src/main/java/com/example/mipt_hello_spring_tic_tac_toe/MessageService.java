package com.example.mipt_hello_spring_tic_tac_toe;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository repository;

    @PostConstruct
    private void initializeSampleData() {
        repository.save(new Message("Hello, Spring!"));
        repository.save(new Message("In-memory storage works"));
    }

    public Iterable<Message> getAllMessages() {
        return repository.findAll();
    }
}

