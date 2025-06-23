package com.pikolo.pikolo.service.lookalike;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pikolo.pikolo.dto.ContentDTO;
import com.pikolo.pikolo.dto.lookalike.LookAlikeDTO;
import com.pikolo.pikolo.mapper.lookalike.LookAlikeDAO;

@Service
public class LookAlikeService {

@Autowired
private LookAlikeDAO lookAlikeDAO;

// 업로드 디렉토리 설정
@Value("${app.upload.dir:/app/images/lookalike/uploads}")
private String uploadDir;

public String saveUploadedImage(MultipartFile file, String uploadId) throws IOException {
    // 업로드 디렉토리 생성
    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
    }
    
    // 원본 파일명에서 확장자 추출
    String originalFilename = file.getOriginalFilename();
    String fileExtension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
        fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    
    // 새 파일명 생성 (uploadId + 확장자)
    String newFileName = uploadId + fileExtension;
    Path filePath = uploadPath.resolve(newFileName);
    
    // 파일 저장
    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    
    System.out.println("파일 저장 완료: " + originalFilename + " -> " + filePath.toString());
    
    return filePath.toString();
}

public LookAlikeDTO analyzeLookalike(String uploadId) {
    // 1. 저장된 이미지 파일 로드
    String imagePath = getImagePath(uploadId);
    
    // 2. AI 분석 로직 (외부 API 호출 또는 자체 알고리즘)
    // 실제로는 AI 서비스나 ML 모델을 호출
    
    // 3. 임시 더미 데이터 반환
    LookAlikeDTO result = new LookAlikeDTO();
    result.setCelebrityName("아이유");
    result.setCelebrityImage("/images/celebrities/iu.jpg");
    result.setOccupation("가수/배우");
    result.setAge(31);
    result.setScore(87);
    
    // 4. 분석 완료 후 임시 파일 삭제 (옵션)
    deleteTemporaryFile(uploadId);
    
    return result;
}

private String getImagePath(String uploadId) {
    // uploadId로 저장된 파일 경로 찾기
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

public ContentDTO searchLookAlike(int id, String lang) {
    switch (lang) {
        case "ko":
            return lookAlikeDAO.selectLookalikeKO(id);
        case "en":
            return lookAlikeDAO.selectLookalikeEN(id);
        case "jp":
            return lookAlikeDAO.selectLookalikeJP(id);
        default:
            return lookAlikeDAO.selectLookalikeKO(id);
    }
}




}
