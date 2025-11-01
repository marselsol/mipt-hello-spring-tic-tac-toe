package com.example.mipt_hello_spring_tic_tac_toe;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MessageRepository {
    private final ConcurrentHashMap<Long, Message> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public Message save(Message message) {
        if (message.getId() == null) {
            message.setId(idCounter.incrementAndGet());
        }
        storage.put(message.getId(), message);
        return message;
    }

    public Message findById(Long id) {
        return storage.get(id);
    }

    public Iterable<Message> findAll() {
        Collection<Message> values = storage.values();
        return values::iterator;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public void clear() {
        storage.clear();
        idCounter.set(0);
    }
}

