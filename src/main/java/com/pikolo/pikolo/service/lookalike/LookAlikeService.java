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
import com.pikolo.pikolo.mapper.lookalike.LookAlikeDAO;

@Service
public class LookAlikeService {

    @Autowired
    private LookAlikeDAO lookAlikeDAO;

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
