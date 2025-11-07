package com.example.mipt_hello_spring_tic_tac_toe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

record CreateMessageRequest(String content, String username) {}

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/optimized")
    public List<Message> getMessagesOptimized() {
        return service.getLatestMessagesOptimized();
    }

    @GetMapping("/nplus1")
    public List<Message> getMessagesNPlus1() {
        return service.getLatestMessagesNPlus1();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message createMessage(@RequestBody CreateMessageRequest request) {
        String username = (request.username() == null || request.username().trim().isEmpty())
                ? "Anonymous"
                : request.username();

        return service.saveMessage(request.content(), username);
    }

    @GetMapping("/fail/{id}")
    public ResponseEntity<?> getMessageToFail(@PathVariable Long id) {
        try {
            Message message = service.getMessageToFail(id);
            return ResponseEntity.ok(message);

        } catch (Exception e) {
            String errorMsg = "Ошибка! Проверьте консоль. Вероятно, LazyInitializationException (Proxy Exception), " +
                    "потому что вы пытаетесь получить LAZY-поле вне транзакции. " +
                    "Для решения: поместите метод getMessageToFail в @Transactional или используйте JOIN FETCH.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorMsg);
        }
    }
}
