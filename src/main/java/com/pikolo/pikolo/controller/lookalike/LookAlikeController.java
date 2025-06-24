package com.pikolo.pikolo.controller.lookalike;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.pikolo.pikolo.dto.ContentDTO;
import com.pikolo.pikolo.dto.lookalike.LookAlikeResultDTO;
import com.pikolo.pikolo.service.lookalike.LookAlikeResultService;
import com.pikolo.pikolo.service.lookalike.LookAlikeService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
public class LookAlikeController {

    private final LookAlikeResultService lookAlikeResultService;

    @Autowired
    private LookAlikeService lookAlikeService;

    LookAlikeController(LookAlikeResultService lookAlikeResultService) {
        this.lookAlikeResultService = lookAlikeResultService;
    }

    @GetMapping("/api/lookalike")
    public ContentDTO getLookAlike(@RequestParam int id, @RequestParam String language) {
        return lookAlikeService.searchLookAlike(id, language);
    }

    // 이미지 분석
    @GetMapping("/api/lookalike/result")
    public LookAlikeResultDTO getLookAlikeResult(@RequestParam String uploadId, @RequestParam String language) {

        try {
            // LookAlikeResultService 의존성 주입 필요 (클래스 상단에)
            return lookAlikeResultService.getLookAlikeResult(uploadId, language);

        } catch (Exception e) {
            // 에러 발생 시 기본값 반환 또는 예외 처리
            System.out.println("컨트롤러에서 오류 발생: " + e.getMessage());
            e.printStackTrace();

            // 에러 응답용 DTO 반환
            LookAlikeResultDTO errorResult = new LookAlikeResultDTO();
            errorResult.setName("분석 실패");
            errorResult.setAge(0);
            errorResult.setActor("알 수 없음");
            errorResult.setScore("0");
            errorResult.setOriginalImagePath("");
            errorResult.setResultImageUrl("");

            return errorResult;
        }
    }

    // 이미지 업로드
    @PostMapping("/api/lookalike/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("image") MultipartFile file,
            HttpServletRequest request) {

        try {
            // 파일 유효성 검사
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "파일이 비어있습니다."));
            }

            int maxFileSize = 10 * 1024 * 1024; // 10MB
            if (file.getSize() > maxFileSize) {
                System.out.println(file.getSize());
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body(Map.of("error", "파일 크기가 너무 큽니다."));
            }

            // 파일 타입 검증
            String contentType = file.getContentType();
            if (!isValidImageType(contentType)) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body(Map.of("error", "지원하지 않는 파일 형식입니다."));
            }

            // 고유한 업로드 ID 생성
            String uploadId = UUID.randomUUID().toString();

            // 파일 저장 및 처리
            String savedFilePath = lookAlikeService.saveUploadedImage(file, uploadId);

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("uploadId", uploadId);
            response.put("originalFilename", file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            response.put("savedPath", savedFilePath);
            response.put("timestamp", LocalDateTime.now().toString());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            System.err.println("파일 업로드 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "파일 업로드 중 오류가 발생했습니다."));
        } catch (Exception e) {
            System.err.println("예상치 못한 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "서버 내부 오류가 발생했습니다."));
        }
    }

    // 파일 타입 검증 메서드
    private boolean isValidImageType(String contentType) {
        return contentType != null && (contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/heic") ||
                contentType.equals("image/heif") ||
                contentType.equals("image/png"));
    }

    @GetMapping("/api/proxyImage")
    public ResponseEntity<byte[]> proxyImage(@RequestParam String url) {
        // url의 유효성, 화이트리스트 필수! (악의적 요청 방지)
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(response.getHeaders().getContentType());
    return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
    }

}
