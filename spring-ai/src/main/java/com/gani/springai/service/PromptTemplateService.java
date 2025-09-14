package com.gani.springai.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
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

    /**
     * 여러 개의 UserMessage와 AssistantMessage도 함께 전송할 수 있음 <br>
     * 본격 Agent를 개발한다면, 기존 대화를 어떤 식으로 저장하고 이를 효율적으로 불러와 컨텍스트를 유지할 것인가가 중요하겠네.
     * - Document DB를 사용하는 것도 좋아보이고, 응답 효율을 위해 적절한 TTL을 건 캐싱을 적용해도 좋겠다.
     * @param question user message
     * @param chatMemory message history
     * @return llm response
     */
    public String multiMessage(@NonNull String question, @NonNull List<Message> chatMemory) {
        if (chatMemory.isEmpty()) {
            chatMemory.add(MultiMessageChatOptions.SYSTEM_MESSAGE);
        }

        log.info("Message histories: {}", chatMemory.toString());

        ChatResponse response = chatClient.prompt()
                .messages(chatMemory)
                .user(question)
                .call()
                .chatResponse();

        UserMessage userMessage = UserMessage.builder().text(question).build();
        chatMemory.add(userMessage);

        // Memoization(AI도 DP다..)
        AssistantMessage assistantMessage = response.getResult().getOutput();
        chatMemory.add(assistantMessage);

        return assistantMessage.getText();
    }

    private static class MultiMessageChatOptions {
        static final SystemMessage SYSTEM_MESSAGE = SystemMessage.builder()
                .text("""
                당신은 AI 비서입니다.
                제공되는 지난 대화 내용을 보고 우선적으로 답변해 주세요.
                """)
                .build();
    }


}
