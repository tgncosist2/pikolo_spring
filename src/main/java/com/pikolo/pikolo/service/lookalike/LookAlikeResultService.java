package com.pikolo.pikolo.service.lookalike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.pikolo.pikolo.config.SecurityConfig;
import com.pikolo.pikolo.dto.lookalike.LookAlikeResultDTO;
import com.pikolo.pikolo.service.springbootGemini.GeminiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.io.IOException;
import java.net.URI;

@Service
public class LookAlikeResultService {


    @Autowired
    private GeminiService geminiService;

    @Value("${app.upload.dir:/app/images/uploads}")
    private String uploadDir;

    public LookAlikeResultDTO getLookAlikeResult(String uploadId, String language) {

        try {
            // 1. LookAlikeService의 메서드로 이미지 경로 찾기
            String imagePath = getImagePath(uploadId);
            if (imagePath == null) {
                throw new RuntimeException("업로드된 이미지를 찾을 수 없습니다.");
            }

            // 2. 프롬프트 생성
            String prompt = createPrompt(language);

            // 3. OpenAI Vision API 호출
            System.out.println("=== 닮은꼴 분석 시작 ===");
            System.out.println("Upload ID: " + uploadId);
            System.out.println("Image Path: " + imagePath);
            System.out.println("Language: " + language);
            System.out.println("Prompt: " + prompt);

            String aiResponse = geminiService.getGeminiVisionResponse(prompt, imagePath);

            System.out.println("=== AI 응답 결과 ===");
            System.out.println(aiResponse);
            System.out.println("==================");

            // 4. JSON 파싱하여 DTO 생성
            LookAlikeResultDTO result = parseAiResponseToDTO(aiResponse, uploadId, imagePath);

            // 5. 분석 완료 후 임시 파일 삭제 (옵션)
            deleteTemporaryFile(uploadId);

            return result;

        } catch (Exception e) {
            System.out.println("닮은꼴 분석 중 오류 발생: " + e.getMessage());
            e.printStackTrace();

            // 오류 발생 시 기본값 반환
            return createDefaultResult(language, uploadId);
        }
    }

    // 기존 LookAlikeService와 동일한 로직으로 이미지 경로 찾기
    private String getImagePath(String uploadId) {
        Path uploadPath = Paths.get(uploadDir);
        try {
            return Files.list(uploadPath)
                    .filter(path -> path.getFileName().toString().startsWith(uploadId))
                    .findFirst()
                    .map(Path::toString)
                    .orElse(null);
        } catch (IOException e) {
            System.err.println("파일 경로 조회 실패: " + e.getMessage());
            return null;
        }
    }

    // 기존 LookAlikeService와 동일한 로직으로 임시 파일 삭제
    private void deleteTemporaryFile(String uploadId) {
        String imagePath = getImagePath(uploadId);
        if (imagePath != null) {
            try {
                Files.deleteIfExists(Paths.get(imagePath));
                System.out.println("임시 파일 삭제 완료: " + imagePath);
            } catch (IOException e) {
                System.err.println("임시 파일 삭제 실패: " + e.getMessage());
            }
        }
    }

    // 언어별 프롬프트 생성
    private String createPrompt(String language) {
        StringBuilder prompt = new StringBuilder();
    
        prompt.append("Analyze the facial characteristics in the photo and ");
        prompt.append("find a Korean entertainment celebrity or famous character whose style is similar. ");
        prompt.append("This is a fun style-matching game for entertainment purposes.\n\n");
    
        prompt.append("If there is not enough data, or the analysis is difficult, ");
        prompt.append("use your imagination to select the best-fitting Korean celebrity or character. ");
        prompt.append("Always respond only with a JSON object, without any explanations or extra text. ");
        prompt.append("Creatively match a celebrity or character with a similar vibe. ");
        prompt.append("If you lack actual data, feel free to invent fun fictional details.\n\n");
    
        prompt.append("Give your answer ONLY in the following JSON format (no explanations):\n");
        prompt.append("{\n");
        prompt.append("  \"name\": \"Name of a celebrity or character with similar style\",\n");
        prompt.append("  \"age\": Age (number, estimated or fictional),\n");
        prompt.append("  \"actor\": \"Profession or character type\",\n");
        prompt.append("  \"score\": \"Similarity score (0 to 100, can be fictional)\",\n");
        prompt.append("  \"koreanName\": \"Korean name in Hangul\",\n");
        prompt.append("  \"koreanAge\": Age in number (Hangul),\n");
        prompt.append("  \"koreanActor\": \"Profession or character type in Hangul\"\n");
        prompt.append("}\n\n");
    
        prompt.append("IMPORTANT: Output ONLY a single JSON object, never any other text or comments.\n\n");
    
        switch (language) {
            case "ko":
                prompt.append("All field values must be in Korean (Hangul). ");
                prompt.append("koreanName and koreanActor must also be in Hangul.\n");
                break;
            case "en":
                prompt.append("All field values must be in English. ");
                prompt.append("However, koreanName and koreanActor must be written in Korean (Hangul).\n");
                break;
            case "jp":
                prompt.append("すべてのフィールド値は日本語で記入してください。");
                prompt.append("ただし、koreanName と koreanActor は必ずハングルで記入してください。\n");
                break;
            default:
                prompt.append("All field values must be in Korean (Hangul). ");
                prompt.append("koreanName and koreanActor must also be in Hangul.\n");
                break;
        }
    
        return prompt.toString();
    }

    // AI 응답을 DTO로 파싱
    private LookAlikeResultDTO parseAiResponseToDTO(String aiResponse, String uploadId, String imagePath) {
        try {
            // ✨ "죄송합니다" 같은 거부 답변 감지
            if (aiResponse.contains("죄송") || aiResponse.contains("도와드릴 수 없") ||
                    aiResponse.contains("식별할 수 없") || aiResponse.length() < 50) {

                System.out.println("Gemini API가 분석을 거부함. 랜덤 결과 생성: " + aiResponse);
                return createRandomLookalikeResult(uploadId, imagePath);
            }

            ObjectMapper mapper = new ObjectMapper();
            String jsonPart = extractJsonFromResponse(aiResponse);
            JsonNode jsonNode = mapper.readTree(jsonPart);

            LookAlikeResultDTO dto = new LookAlikeResultDTO();
            dto.setName(jsonNode.get("name").asText());
            dto.setAge(jsonNode.get("age").asInt());
            dto.setActor(jsonNode.get("actor").asText());
            dto.setScore(jsonNode.get("score").asText());

            String webImagePath = convertToWebPath(imagePath, uploadId);
            dto.setOriginalImagePath(webImagePath);

            dto.setKoreanName(jsonNode.get("koreanName").asText());
            dto.setKoreanAge(jsonNode.get("koreanAge").asInt());
            dto.setKoreanActor(jsonNode.get("koreanActor").asText());

            String celebrityImageUrl = getImageSearch(dto.getKoreanName(), dto.getKoreanActor());
            dto.setResultImageUrl(celebrityImageUrl);

            return dto;

        } catch (Exception e) {
            System.out.println("AI 응답 파싱 실패: " + e.getMessage());
            return createRandomLookalikeResult(uploadId, imagePath);
        }
    }

    private String convertToWebPath(String filePath, String uploadId) {
        // 파일명 추출 (uploadId + 확장자)
        String fileName = Paths.get(filePath).getFileName().toString();

        // 웹에서 접근 가능한 URL로 변환
        return "/images/lookalike/uploads/" + fileName;
    }

    private LookAlikeResultDTO createRandomLookalikeResult(String uploadId, String imagePath) {
        // 인기 연예인 목록
        String[] celebrities = { "아이유", "박보영", "수지", "한지민", "김고은", "박서준", "차은우", "송중기", "현빈", "박보검" };
        String[] occupations = { "가수/배우", "배우", "가수", "모델/배우", "아이돌/배우" };

        // 랜덤 선택
        Random random = new Random();
        String selectedCelebrity = celebrities[random.nextInt(celebrities.length)];
        String selectedOccupation = occupations[random.nextInt(occupations.length)];
        int randomAge = 25 + random.nextInt(15); // 25-40세
        int randomScore = 70 + random.nextInt(25); // 70-95점

        LookAlikeResultDTO dto = new LookAlikeResultDTO();
        dto.setName(selectedCelebrity);
        dto.setAge(randomAge);
        dto.setActor(selectedOccupation);
        dto.setScore(String.valueOf(randomScore));
        dto.setKoreanName(selectedCelebrity);
        dto.setKoreanAge(randomAge);
        dto.setKoreanActor(selectedOccupation);

        String fileName = Paths.get(imagePath).getFileName().toString();
        dto.setOriginalImagePath("/images/lookalike/" + fileName);

        String celebrityImageUrl = getImageSearch(selectedCelebrity, selectedOccupation);
        dto.setResultImageUrl(celebrityImageUrl);

        System.out.println("랜덤 생성된 결과: " + selectedCelebrity + " (" + randomScore + "점)");

        return dto;
    }

    // 응답에서 JSON 부분만 추출
    private String extractJsonFromResponse(String response) {
        int startIndex = response.indexOf("{");
        int endIndex = response.lastIndexOf("}");

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return response.substring(startIndex, endIndex + 1);
        }

        return response;
    }

    // 오류 시 기본 결과 생성
    private LookAlikeResultDTO createDefaultResult(String language, String uploadId) {
        LookAlikeResultDTO dto = new LookAlikeResultDTO();

        switch (language) {
            case "en":
                dto.setName("Analysis Failed");
                dto.setActor("Unknown");
                dto.setKoreanName("분석 실패");
                dto.setKoreanActor("알 수 없음");
                break;
            case "jp":
                dto.setName("分析失敗");
                dto.setActor("不明");
                dto.setKoreanName("분석 실패");
                dto.setKoreanActor("알 수 없음");
                break;
            default:
                dto.setName("분석 실패");
                dto.setActor("알 수 없음");
                dto.setKoreanName("분석 실패");
                dto.setKoreanActor("알 수 없음");
                break;
        }

        dto.setAge(0);
        dto.setKoreanAge(0);
        dto.setScore("0");
        dto.setOriginalImagePath("");
        dto.setResultImageUrl("");

        return dto;
    }

    // 네이버 검색 API 호출
    private String getImageSearch(String name, String actor) {
        String apiUrl = "https://openapi.naver.com/v1/search/image";
        String clientId = "vQhtW2kM75FaIm_vc50t";
        String clientSecret = "pwkQ0qSRBl";

        try {
            // 검색 쿼리 구성 (연예인 이름 + "프로필")
            String searchQuery = actor + " " + name;

            // URI 구성
            URI uri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("query", searchQuery)
                    .queryParam("display", 1) // 첫 번째 이미지만
                    .queryParam("start", 1)
                    .queryParam("sort", "sim") // 유사도순
                    .queryParam("filter", "large") // 큰 이미지 우선
                    .encode()
                    .build().toUri();

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            // ✅ ResponseEntity<String> 사용은 맞습니다!
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            // 전체 응답을 콘솔에 출력
            System.out.println("=== 네이버 이미지 검색 응답 ===");
            System.out.println("검색어: " + searchQuery);
            System.out.println("응답: " + response.getBody());
            System.out.println("=============================");

            // JSON에서 첫 번째 이미지 URL 추출
            return extractImageUrlFromResponse(response.getBody());

        } catch (Exception e) {
            System.out.println("네이버 이미지 검색 실패: " + e.getMessage());
            e.printStackTrace();
            return ""; // 빈 문자열 반환
        }
    }

    // JSON 응답에서 이미지 URL 추출
    private String extractImageUrlFromResponse(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseBody);

            JsonNode items = rootNode.path("items");
            if (items.isArray() && items.size() > 0) {
                JsonNode firstItem = items.get(0);
                String imageUrl = firstItem.path("link").asText();

                System.out.println("추출된 이미지 URL: " + imageUrl);
                return imageUrl;
            }

            System.out.println("검색 결과 없음");
            return "";

        } catch (Exception e) {
            System.out.println("이미지 URL 추출 실패: " + e.getMessage());
            return "";
        }
    }
}
