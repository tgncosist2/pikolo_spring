package com.pikolo.pikolo.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.user.LoginRequestDTO;
import com.pikolo.pikolo.dto.user.UserInfoDTO;
import com.pikolo.pikolo.service.user.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/api/user/login")
    public ResponseEntity<?> getUserLoginInfo(@RequestBody LoginRequestDTO loginRequest, HttpServletRequest request) {

        String clientIp = getClientIpAddress(request);

        // 입력값 검증
        if (loginRequest == null || loginRequest.getUserEmail() == null || loginRequest.getUserPassword() == null) {
            return ResponseEntity.status(400).body(Map.of("message", "이메일과 비밀번호를 입력해주세요."));
        }

        // 에러코드별 예외처리
        try {
            UserInfoDTO user = loginService.getUserLoginInfo(loginRequest);
            if (user != null) {
                // 로그인 성공 시 마지막 로그인 날짜 업데이트
                loginService.updateUserLoginDate(Map.of("userEmail", user.getUserEmail(), "clientIp", clientIp));
                return ResponseEntity.ok(user); // 200 로그인 성공
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "이메일 또는 비밀번호가 일치하지 않습니다.")); // 401 비일치
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "서버 오류가 발생했습니다.")); // 500 서버 오류
        } // end try-catch

    }// end getUserLoginInfo

    // Nginx 프록시 환경에서 실제 클라이언트 IP 획득하는 메소드
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }// getClientIpAddress

}// class
