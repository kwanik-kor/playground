package com.gani.springai.presentation.prompttemplate;

import com.gani.springai.service.PromptTemplateService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/prompt-template")
@Slf4j
public class RestPromptTemplateController {
    private final PromptTemplateService promptTemplateService;

    @PostMapping(
            value = "/multi-messages",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String multiMessages(@RequestParam("question") String question, HttpSession session) {
        List<Message> chatMemory = (List<Message>) session.getAttribute("chatMemory");
        if (chatMemory == null) {
            chatMemory = new ArrayList<Message>();
            session.setAttribute("chatMemory", chatMemory);
        }

        return promptTemplateService.multiMessage(question, chatMemory);
    }

    @PostMapping(
            value = "/zero-shot-prompt",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String zeroShotPrompt(@RequestParam("review") String review) {
        return promptTemplateService.zeroShotPrompt(review);
    }

    @PostMapping(
            value = "/few-shot-prompt",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String fewShotPrompt(@RequestParam("order") String order) {
        return promptTemplateService.fewShotPrompt(order);
    }

    @PostMapping(
            value = "/role-assignment",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> roleAssignment(@RequestParam("requirements") String requirements) {
        return promptTemplateService.roleAssignment(requirements);
    }

    @PostMapping(
            value = "/step-back-prompt",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String stepBackPrompt(@RequestParam("question") String question) throws Exception {
        return promptTemplateService.stepBackPrompt(question);
    }

    @PostMapping(
            value = "/chain-of-thought",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> chainOfThought(@RequestParam("question") String question) {
        return promptTemplateService.chainOfThought(question);
    }

    @PostMapping(
            value = "/self-consistency",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String selfConsistency(@RequestParam("content") String content) {
        return promptTemplateService.selfConsistency(content);
    }

}
