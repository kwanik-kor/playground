package com.gani.springai.presentation.prompttemplate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prompt-template")
public class PromptTemplateController {

    @GetMapping
    public String promptTemplateHome() {
        return "prompt-template/home";
    }

    @GetMapping("/prompt-template")
    public String promptTemplate() {
        return "/prompt-template/prompt-template";
    }

    @GetMapping("/multi-messages")
    public String multiMessages() {
        return "/prompt-template/multi-messages";
    }

    @GetMapping("/default-method")
    public String defaultMethod() {
        return "/prompt-template/default-method";
    }

    @GetMapping("/zero-shot-prompt")
    public String zeroShotPrompt() {
        return "/prompt-template/zero-shot-prompt";
    }

    @GetMapping("/few-shot-prompt")
    public String fewShotPrompt() {
        return "/prompt-template/few-shot-prompt";
    }

    @GetMapping("/role-assignment")
    public String rollAssignment() {
        return "/prompt-template/role-assignment";
    }

    @GetMapping("/step-back-prompt")
    public String stepBackPrompt() {
        return "/prompt-template/step-back-prompt";
    }

    @GetMapping("/chain-of-thought")
    public String chainOfThought() {
        return "/prompt-template/chain-of-thought";
    }

    @GetMapping("/self-consistency")
    public String selfConsistency() {
        return "/prompt-template/self-consistency";
    }

}
