package com.gani.springai.presentation;

import com.gani.springai.service.ChatModelByClient;
import com.gani.springai.service.PromptTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
@Slf4j
public class AiController {
//    private final ChatModelService chatModelService;
    private final ChatModelByClient chatModelService;
    private final PromptTemplateService promptTemplateService;

    @PostMapping(
            value = "/chat-model",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam("question") String question) {
        return chatModelService.generateText(question);
    }

    @PostMapping(
            value = "/chat-model-stream",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> chatStream(@RequestParam("question") String question) {
        return chatModelService.generateStreamText(question);
    }

    @PostMapping(
            value = "/prompt-template",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> promptTemplate(@RequestParam("statement") String statement,
                                       @RequestParam("language") String language) {
        return promptTemplateService.promptTemplate(statement, language);
    }

}
