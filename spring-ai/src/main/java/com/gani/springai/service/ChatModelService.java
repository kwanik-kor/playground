package com.gani.springai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatModelService {
    private final ChatModel chatModel;

    public String generateText(String question) {
        SystemMessage systemMessage = SystemMessage.builder()
                .text("사용자의 질문에 대해 한국어로 답변해야 합니다. 불필요한 수식어는 최대한 배제합니다.")
                .build();

        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .build();

        ChatOptions chatOptions = ChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.3)
                .maxTokens(1000)
                .build();

        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .chatOptions(chatOptions)
                .build();

        ChatResponse chatResponse = chatModel.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        String answer = assistantMessage.getText();

        return answer;
    }

    public Flux<String> generateStreamText(String question) {
        SystemMessage systemMessage = SystemMessage.builder()
                .text("사용자의 질문에 대해 한국어로 답변해야 합니다. 불필요한 수식어는 최대한 배제합니다.")
                .build();

        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .build();

        ChatOptions chatOptions = ChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.3)
                .maxTokens(1000)
                .build();

        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .chatOptions(chatOptions)
                .build();

        Flux<ChatResponse> fluxResponse = chatModel.stream(prompt);

        return fluxResponse.map(chatResponse -> {
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            String chunk = assistantMessage.getText();
            if (chunk == null) chunk = "";
            return chunk;
        });
    }
}
