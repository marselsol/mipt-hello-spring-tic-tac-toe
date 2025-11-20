package com.example.mipt_hello_spring_tic_tac_toe;

import lombok.Data;

@Data
public class CreateMessageRequest {
    private String content;
    private String username;
}