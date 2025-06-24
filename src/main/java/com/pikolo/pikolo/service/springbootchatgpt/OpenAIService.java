package com.pikolo.pikolo.service.springbootchatgpt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();  // Jackson ObjectMapper
    }

    public String getChatGptResponse(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String requestBody = "{\n" +
                "  \"model\": \"" + model + "\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"You are a helpful assistant.\"},\n" +
                "    {\"role\": \"user\", \"content\": \"" + prompt + "\"}\n" +
                "  ]\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            // JSON 문자열을 JsonNode로 변환
            JsonNode responseJsonNode = objectMapper.readTree(response.getBody());
            
            // content만 추출하여 반환하기 위해 문자열로 변환
            return responseJsonNode.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            // 예외 처리
            return "{\"error\": \"OpenAI API 호출 실패: " + e.getMessage() + "\"}";
        }
    }

    // OpenAIService의 getChatGptVisionResponse 메서드 수정
public String getChatGptVisionResponse(String prompt, String imagePath) {
    try {
        // 이미지를 Base64로 인코딩
        String base64Image = encodeImageToBase64(imagePath);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // ObjectMapper를 사용한 안전한 JSON 생성
        ObjectMapper mapper = new ObjectMapper();
        
        // 메시지 내용 구성
        Map<String, Object> textContent = new HashMap<>();
        textContent.put("type", "text");
        textContent.put("text", prompt);
        
        Map<String, Object> imageUrlMap = new HashMap<>();
        imageUrlMap.put("url", "data:image/jpeg;base64," + base64Image);
        // ✨ 이미지 해상도 조절 (토큰 절약)
        imageUrlMap.put("detail", "high"); // "high", "low", "auto" 중 선택
        
        Map<String, Object> imageContent = new HashMap<>();
        imageContent.put("type", "image_url");
        imageContent.put("image_url", imageUrlMap);
        
        List<Map<String, Object>> content = new ArrayList<>();
        content.add(textContent);
        content.add(imageContent);
        
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", content);
        
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message);
        
        // 전체 요청 바디 구성
        Map<String, Object> requestBody = new HashMap<>();
        // ✅ 최신 모델로 변경 (2024년 12월 기준)
        requestBody.put("model", "gpt-4o"); // 또는 "gpt-4o-mini"
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 1000);
        
        String jsonBody = mapper.writeValueAsString(requestBody);
        
        System.out.println("=== 전송할 JSON 요청 (일부) ===");
        System.out.println("Model: gpt-4o");
        System.out.println("Prompt: " + prompt);
        System.out.println("Image size: " + base64Image.length() + " characters");
        System.out.println("====================");

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
        
        System.out.println("=== OpenAI Vision API 전체 응답 ===");
        System.out.println(response.getBody());
        System.out.println("================================");
        
        JsonNode responseJsonNode = objectMapper.readTree(response.getBody());
        return responseJsonNode.path("choices").get(0).path("message").path("content").asText();
        
    } catch (Exception e) {
        System.out.println("Vision API 호출 실패: " + e.getMessage());
        e.printStackTrace();
        return "{\"error\": \"Vision API 호출 실패: " + e.getMessage() + "\"}";
    }
}

    

    // 새로 추가: Base64 인코딩 헬퍼 메서드
    private String encodeImageToBase64(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new IOException("이미지 파일을 찾을 수 없습니다: " + imagePath);
        }
        
        // ✨ 이미지 크기 줄이기 (선택사항)
        // BufferedImage로 리사이징 후 Base64 인코딩
        
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
