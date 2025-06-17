package com.pikolo.pikolo.controller.springbootchatgpt;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pikolo.pikolo.service.springbootchatgpt.OpenAIService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class OpenAIController {

    private final OpenAIService openAIService;  // OpenAIService를 인스턴스 변수로 선언

    // 생성자 주입을 통해 OpenAIService 인스턴스 주입
    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("/api/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        // openAIService를 통해 getChatGptResponse 호출
        return openAIService.getChatGptResponse(prompt);  // 인스턴스를 통해 호출
    }
}