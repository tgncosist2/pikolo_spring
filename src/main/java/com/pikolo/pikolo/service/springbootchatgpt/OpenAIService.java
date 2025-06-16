package com.pikolo.pikolo.service.springbootchatgpt;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {
    
    @Value("${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}")
    private String apiKey;

    @Value("${https://api.openai.com/v1/completions}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String getChatGptResponse(String prompt){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Beared "+apiKey);
        headers.set("Content-Type", "application/json");
        
         String requestBody = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"You are a helpful assistant.\"},\n" +
                "    {\"role\": \"user\", \"content\": \"" + prompt + "\"}\n" +
                "  ]\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

}

