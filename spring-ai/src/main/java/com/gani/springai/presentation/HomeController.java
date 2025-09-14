package com.gani.springai.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/chat")
    public String chatModel() {
        return "chat-model/chat-model";
    }

    @GetMapping("/chat-stream")
    public String chatModelStream() {
        return "chat-model/chat-model-stream";
    }

}

