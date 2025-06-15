package com.pikolo.pikolo.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.user.SignUpDTO;
import com.pikolo.pikolo.service.user.SignUpService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class SignUpController {
    
    @Autowired
    private SignUpService signUpService;

    @PostMapping("/api/user/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO, HttpServletRequest request) {
        
        String clientIp = getClientIpAddress(request);
        signUpDTO.setInputIp(clientIp); // 클라이언트 IP 설정

        boolean isSignUpResult = signUpService.insertSignUp(signUpDTO);

        if (isSignUpResult) {
            return ResponseEntity.ok(Map.of("message","회원가입 성공"));
        } else {
            return ResponseEntity.status(400).body(Map.of("message","회원가입 성공"));
        } // end else-if

    }// signUp

    // Nginx 프록시 환경에서 실제 클라이언트 IP 획득하는 메소드
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }// getClientIpAddress

}// class
