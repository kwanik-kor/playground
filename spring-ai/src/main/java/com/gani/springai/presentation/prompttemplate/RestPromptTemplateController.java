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

        String answer = promptTemplateService.multiMessage(question, chatMemory);
        return answer;
    }
}
