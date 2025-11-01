package com.example.mipt_hello_spring_tic_tac_toe;

import lombok.Data;

@Data
public class Message {
    private Long id;
    private String content;

    public Message(String content) {
        this.content = content;
    }
}

