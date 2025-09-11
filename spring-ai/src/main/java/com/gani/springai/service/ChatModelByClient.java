package com.gani.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class ChatModelByClient {
    private ChatClient chatClient;

    public ChatModelByClient(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateText(String question) {
        String answer = chatClient.prompt()
                .system("사용자 질문에 대해 한국어로 답변을 해야 합니다.")
                .user(question)
                .options(ChatOptions.builder()
                        .temperature(0.3)
                        .maxTokens(1000)
                        .build()
                )
                .call()
                .content();

        return answer;
    }

    public Flux<String> generateStreamText(String question) {
        Flux<String> fluxString = chatClient.prompt()
                .system("사용자 질문에 대해 한국어로 답변을 해야 합니다.")
                .user(question)
                .options(ChatOptions.builder()
                        .temperature(0.3)
                        .maxTokens(1000)
                        .build()
                )
                .stream()
                .content();

        return fluxString;
    }

}
