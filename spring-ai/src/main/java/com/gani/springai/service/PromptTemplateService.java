package com.gani.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class PromptTemplateService {
    private final ChatClient chatClient;

    private final PromptTemplate systemTemplate = SystemPromptTemplate.builder()
            .template("""
            답변을 생성할 때 HTML과 CSS를 사용하여 파란 글자로 출력하세요.
            <span> 태그 안에 들어갈 내용만 출력하세요.
            """)
            .build();

    private final PromptTemplate userTemplate = PromptTemplate.builder()
            .template("""
            다음 한국어 문장을 {language}로 번역해주세요.
            문장: {statement}
            """)
            .build();

    public PromptTemplateService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public Flux<String> promptTemplate(String statement, String language) {
        return chatClient.prompt()
                .system(systemTemplate.render())
                .user(userTemplate.render(Map.of("statement", statement, "language", language)))
                .stream()
                .content();
    }


}
