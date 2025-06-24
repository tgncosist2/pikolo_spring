package com.pikolo.pikolo.service.springbootGemini;

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
public class GeminiService {
    
    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.api.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public String getGeminiTextResponse(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
    
            Map<String, Object> requestBody = new HashMap<>();
    
            // Gemini 텍스트 전용 contents 구성
            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> content = new HashMap<>();
            List<Map<String, Object>> parts = new ArrayList<>();
    
            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", prompt);
            parts.add(textPart);
    
            content.put("parts", parts);
            contents.add(content);
            requestBody.put("contents", contents);
    
            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", 0.4);
            generationConfig.put("maxOutputTokens", 2000);
            requestBody.put("generationConfig", generationConfig);
    
            String jsonBody = objectMapper.writeValueAsString(requestBody);
    
            System.out.println("=== Gemini 텍스트 API 요청 ===");
            System.out.println("URL: " + apiUrl + "?key=" + apiKey);
            System.out.println("Prompt: " + prompt);
    
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                apiUrl + "?key=" + apiKey, 
                HttpMethod.POST, 
                entity, 
                String.class
            );
    
            System.out.println("=== Gemini 텍스트 API 응답 ===");
            System.out.println(response.getBody());
            System.out.println("====================");
    
            // 응답 파싱
            JsonNode responseNode = objectMapper.readTree(response.getBody());
            return responseNode.path("candidates").get(0)
                    .path("content").path("parts").get(0).path("text").asText();
    
        } catch (Exception e) {
            System.out.println("Gemini 텍스트 API 호출 실패: " + e.getMessage());
            e.printStackTrace();
            return "{\"error\": \"Gemini 텍스트 API 호출 실패: " + e.getMessage() + "\"}";
        }
    }

    public String getGeminiVisionResponse(String prompt, String imagePath) {
        try {
            String base64Image = encodeImageToBase64(imagePath);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            
            // Gemini API 요청 구조
            Map<String, Object> requestBody = new HashMap<>();
            
            // contents 배열 구성
            List<Map<String, Object>> contents = new ArrayList<>();
            Map<String, Object> content = new HashMap<>();
            
            // parts 배열 구성
            List<Map<String, Object>> parts = new ArrayList<>();
            
            // 텍스트 part
            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", prompt);
            parts.add(textPart);
            
            // 이미지 part
            Map<String, Object> imagePart = new HashMap<>();
            Map<String, Object> inlineData = new HashMap<>();
            inlineData.put("mime_type", "image/jpeg");
            inlineData.put("data", base64Image);
            imagePart.put("inline_data", inlineData);
            parts.add(imagePart);
            
            content.put("parts", parts);
            contents.add(content);
            requestBody.put("contents", contents);
            
            // 생성 설정
            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", 0.4);
            generationConfig.put("maxOutputTokens", 2000);
            requestBody.put("generationConfig", generationConfig);
            
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            
            System.out.println("=== Gemini API 요청 ===");
            System.out.println("URL: " + apiUrl + "?key=" + apiKey);
            System.out.println("Prompt: " + prompt);
            System.out.println("Image size: " + base64Image.length());
            
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                apiUrl + "?key=" + apiKey, 
                HttpMethod.POST, 
                entity, 
                String.class
            );
            
            System.out.println("=== Gemini API 응답 ===");
            System.out.println(response.getBody());
            System.out.println("====================");
            
            // 응답 파싱
            JsonNode responseNode = objectMapper.readTree(response.getBody());
            return responseNode.path("candidates").get(0)
                   .path("content").path("parts").get(0).path("text").asText();
            
        } catch (Exception e) {
            System.out.println("Gemini API 호출 실패: " + e.getMessage());
            e.printStackTrace();
            return "{\"error\": \"Gemini API 호출 실패: " + e.getMessage() + "\"}";
        }
    }
    
    private String encodeImageToBase64(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
