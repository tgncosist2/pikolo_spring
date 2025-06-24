package com.pikolo.pikolo.controller.springbootGemini;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping; // 위치 위로 올림

import com.pikolo.pikolo.service.springbootGemini.GeminiService;

@RestController
public class GeminiController {

    private final GeminiService geminiService;  

    // GeminiService 주입용 생성자 (OpenAIService 주입은 필요 없음)
    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/api/gemini/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        // GeminiService의 getChatGptResponse 호출
        return geminiService.getGeminiTextResponse(prompt);
    }
}
