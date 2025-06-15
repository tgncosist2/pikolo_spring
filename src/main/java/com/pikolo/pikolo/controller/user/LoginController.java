package com.pikolo.pikolo.controller.user;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.user.LoginRequestDTO;
import com.pikolo.pikolo.dto.user.UserInfoDTO;
import com.pikolo.pikolo.service.user.LoginService;

@RestController
public class LoginController {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private LoginService loginService;

    @PostMapping("/api/user/login")
    public ResponseEntity<?> getUserLoginInfo(@RequestBody LoginRequestDTO loginRequest) {

        log.info("로그인 요청 시작 - 이메일: {}", loginRequest.getUserEmail());

        // 입력값 검증
        if (loginRequest == null || loginRequest.getUserEmail() == null || loginRequest.getUserPassword() == null) {
            log.warn("로그인 요청 데이터 누락");
            return ResponseEntity.status(400).body(Map.of("message", "이메일과 비밀번호를 입력해주세요."));
        }

        // 에러코드별 예외처리
        try {
            UserInfoDTO user = loginService.getUserLoginInfo(loginRequest);
            if (user != null) {
                log.info("로그인 성공 - 사용자: {}", user.getUserEmail());
                return ResponseEntity.ok(user); // 200 로그인 성공
            } else {
                log.warn("로그인 실패 - 인증 정보 불일치: {}", loginRequest.getUserEmail());
                return ResponseEntity.status(401).body(Map.of("message","이메일 또는 비밀번호가 일치하지 않습니다.")); // 401 비일치
            }
        } catch (Exception e) {
            log.error("로그인 처리 중 서버 오류 발생 - 이메일: {}, 오류: {}", 
                     loginRequest.getUserEmail(), e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("message","서버 오류가 발생했습니다.")); // 500 서버 오류
        }// end try-catch

    }// end getUserLoginInfo

}// class
