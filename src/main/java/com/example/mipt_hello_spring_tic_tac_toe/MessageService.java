package com.example.mipt_hello_spring_tic_tac_toe;

import jakarta.transaction.Transactional;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final Counter messagesCreatedCounter;

    public MessageService(
            MessageRepository messageRepository,
            UserRepository userRepository,
            MeterRegistry meterRegistry
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messagesCreatedCounter = Counter.builder("messages_created_total")
                .description("Number of messages successfully saved")
                .register(meterRegistry);
        initializeSampleData();
    }

    @Transactional
    private void initializeSampleData() {
        User user1 = userRepository.save(new User("Alice"));
        User user2 = userRepository.save(new User("Bob"));

        messageRepository.save(new Message("Hello, JPA and Hibernate!", user1));
        messageRepository.save(new Message("This uses Spring Data JPA interface.", user2));
        messageRepository.save(new Message("Relationships are now managed by ORM.", user1));
    }

    @Transactional
    public Message saveMessage(String content, String username) {
        User author = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(new User(username)));

        Message message = new Message(content, author);
        Message saved = messageRepository.save(message);
        messagesCreatedCounter.increment();
        return saved;
    }

    @Transactional
    public List<Message> getLatestMessagesNPlus1() {
        System.out.println("--- ЗАПРОС N+1 ---");
        List<Message> messages = messageRepository.findTop30ByOrderByCreatedAtDesc();
        messages.forEach(m -> m.getAuthor().getUsername());
        return messages;
    }

    @Transactional
    public List<Message> getLatestMessagesOptimized() {
        System.out.println("--- ОПТИМИЗИРОВАННЫЙ ЗАПРОС JOIN FETCH ---");
        return messageRepository.findLatestWithAuthors(PageRequest.of(0, 30));
    }

    public Message getMessageToFail(Long id) {
        return messageRepository.findById(id).orElseThrow();
    }
}
