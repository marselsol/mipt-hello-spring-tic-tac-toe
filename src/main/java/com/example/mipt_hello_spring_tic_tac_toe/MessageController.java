package com.example.mipt_hello_spring_tic_tac_toe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/optimized")
    public List<Message> getMessagesOptimized() {
        return messageService.getLatestMessagesOptimized();
    }

    @GetMapping("/nplus1")
    public List<Message> getMessagesNPlus1() {
        return messageService.getLatestMessagesNPlus1();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message createMessage(@RequestBody CreateMessageRequest request) {
        String username = (request.getUsername() == null || request.getUsername().trim().isEmpty())
                ? "Anonymous"
                : request.getUsername();

        return messageService.saveMessage(request.getContent(), username);
    }

    @GetMapping("/fail/{id}")
    public ResponseEntity<?> getMessageToFail(@PathVariable Long id) {
        try {
            Message message = messageService.getMessageToFail(id);
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
