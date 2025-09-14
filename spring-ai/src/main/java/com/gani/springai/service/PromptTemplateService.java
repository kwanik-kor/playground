package com.gani.springai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    /**
     * 제로-샷 프롬프트 <br>
     * AI 모델이 명확히 지시를 이해하고 실행할 수 있는 능력이 있을 때
     * @param review 영화 리뷰
     * @return 리뷰 분류 결과
     */
    public String zeroShotPrompt(String review) {
        return chatClient
                .prompt()
                .user(PromptTemplate.builder()
                        .template(
                        """
                            영화 리뷰를 [긍정적, 중립적, 부정적] 중에서 하나로 분류하세요.
                            레이블만 반환하세요.
                            리뷰: {review}
                        """)
                        .build()
                        .render(Map.of("review", review)))
                .options(ChatOptions.builder()
                        .temperature(0.0)
                        .maxTokens(4)
                        .build())
                .call()
                .content();
    }

    /**
     * few-shot 프롬프트
     * @param order 주문
     * @return JSON으로 변경된 주문
     */
    public String fewShotPrompt(String order) {
        final String strPrompt = """
                고객 주문을 유효한 JSON 형식으로 바꿔주세요.
                추가 설명은 포함하지 마세요.
                
                예시1:
                작은 피자 하나, 치즈랑 토마토 소스, 페퍼로니 올려서 주세요.
                JSON 응답:
                {
                  "size": "small",  "type": "normal",  "ingredients": ["cheese", "tomato sauce", "pepperoni"]
                }
                
                예시1:
                큰 피자 하나, 토마토 소스랑 바질, 모짜렐라 올려서 주세요.
                JSON 응답:
                {
                  "size": "large",  "type": "normal",  "ingredients": ["tomato sauce", "basil", "mozzarella"]
                }
                
                고객 주문: %s
                """.formatted(order);

        Prompt prompt = Prompt.builder()
                .content(strPrompt)
                .build();

        return chatClient.prompt(prompt)
                .options(ChatOptions.builder()
                        .temperature(0.0)
                        .maxTokens(300)
                        .build())
                .call()
                .content();
    }

    /**
     * 역할 부여 프롬프트
     * @param requirements 요구사항
     * @return
     */
    public Flux<String> roleAssignment(String requirements) {
        return chatClient.prompt()
                // 시스템 메시지 추가
                .system("""
                당신이 여행 가이드 역할을 해 주었으면 좋겠습니다.
                아래 요청사항에서 위치를 알려주면, 근처에 있는 3곳을 제안해 주고,
                이유를 달아주세요. 경우에 따라서 방문하고 싶은 장소 유형을 
                제공할 수도 있습니다.
                출력 형식은 <ul> 태그이고, 장소는 굵게 표시해 주세요.
                """)
                // 사용자 메시지 추가
                .user("요청사항: %s".formatted(requirements))
                // 대화 옵션 설정
                .options(ChatOptions.builder()
                        .temperature(1.0)
                        .maxTokens(1000)
                        .build())
                // LLM으로 요청하고 응답 얻기
                .stream()
                .content();
    }

    /**
     * 스텝-백 프롬프트
     * @param question 질의
     * @return 단계별 답변
     * @throws Exception
     */
    public String stepBackPrompt(String question) throws Exception {
        final String questions = chatClient.prompt()
                .user("""
                사용자 질문을 처리하기 Step-Back 프롬프트 기법을 사용하려고 합니다.
                사용자 질문을 단계별 질문들로 재구성해주세요. 
                맨 마지막 질문은 사용자 질문과 일치해야 합니다.
                단계별 질문을 항목으로 하는 JSON 배열로 출력해 주세요.
                예시: ["...", "...", "...", ...]
                사용자 질문: %s
                """.formatted(question))
                .call()
                .content();

        String json = questions.substring(questions.indexOf("["), questions.indexOf("]") + 1);
        log.info("Step-Back prompt: {}", json);

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> listQuestion = objectMapper.readValue(json, new TypeReference<List<String>>() {});

        String[] answerArray = new String[listQuestion.size()];

        for(int i = 0; i < listQuestion.size(); i++) {
            String stepQuestion = listQuestion.get(i);
            String stepAnswer = getStepAnswer(stepQuestion, answerArray);
            answerArray[i] = stepAnswer;
            log.info("단계{} 질문: {}, 답변: {}", i + 1, stepQuestion, stepAnswer);
        }

        return answerArray[answerArray.length - 1];
    }

    public String getStepAnswer(String question, String... prevStepAnswers) {
        StringBuilder context = new StringBuilder();

        for (String prevStepAnswer : prevStepAnswers) {
            context.append(Objects.requireNonNullElse(prevStepAnswer, ""));
        }

        return chatClient.prompt()
                .user("""
                %s
                문맥: %s
                """.formatted(question, context.toString()))
                .call()
                .content();
    }

    public Flux<String> chainOfThought(String question) {
        return chatClient.prompt()
                .user("""
                %s
                한 걸음씩 생각해 봅시다.
      
                [예시]
                질문: 제 동생이 2살일 때, 저는 그의 나이의 두 배였어요.
                지금 저는 40살인데, 제 동생은 몇 살일까요? 한 걸음씩 생각해 봅시다.
      
                답변: 제 동생이 2살일 때, 저는 2 * 2 = 4살이었어요.
                그때부터 2년 차이가 나며, 제가 더 나이가 많습니다.
                지금 저는 40살이니, 제 동생은 40 - 2 = 38살이에요. 정답은 38살입니다.
                """.formatted(question))
                .stream()
                .content();
    }

    public String selfConsistency(String content) {
        int importantCount = 0;
        int notImportantCount = 0;

        String userText = PromptTemplate.builder()
                .template("""
                  다음 내용을 [IMPORTANT, NOT_IMPORTANT] 둘 중 하나로 분류해 주세요.
                  레이블만 반환하세요.
                  내용: {content}
                  """)
                .build()
                .render(Map.of("content", content));

        // 다섯번에 걸쳐 응답 받아 보기
        for (int i = 0; i < 5; i++) {
            // LLM 요청 및 응답 받기
            String output = chatClient.prompt()
                    .user(userText)
                    .options(ChatOptions.builder()
                            .temperature(1.0)
                            .build())
                    .call()
                    .content();

            log.info("{}: {}", i, output.toString());

            // 결과 집계
            if (output.equals("IMPORTANT")) {
                importantCount++;
            } else {
                notImportantCount++;
            }
        }

        // 다수결로 최종 분류를 결정
        String finalClassification = importantCount > notImportantCount ?
                "중요함" : "중요하지 않음";
        return finalClassification;
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
