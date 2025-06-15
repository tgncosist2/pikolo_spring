package com.pikolo.pikolo.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.user.LoginRequestDTO;
import com.pikolo.pikolo.dto.user.UserInfoDTO;
import com.pikolo.pikolo.service.JwtService;
import com.pikolo.pikolo.service.JwtServiceImpl;
import com.pikolo.pikolo.service.user.LoginService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/api/user/login")
    public ResponseEntity<?> getUserLoginInfo(@RequestBody LoginRequestDTO loginRequest, HttpServletRequest request, HttpServletResponse res) {

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
                
                // JWT 토큰 생성
                jwtService = new JwtServiceImpl();
                int id = user.getUserId();
                String token = jwtService.getToken("id", id, 15 * 60 * 1000); // 15분 유효

                // JWT 토큰을 쿠키에 저장
                Cookie cookie = new Cookie("token", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(true); // 개발 환경에서는 false, 운영 환경에서는 true로 설정
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 1); // 1시간 동안 유효

                res.addCookie(cookie);

                return new ResponseEntity<>(id, HttpStatus.OK); // 200 로그인 성공
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

    @GetMapping("/api/account/check")
    public ResponseEntity<?> check(@CookieValue(value="token", required=false) String token) {
        Claims claims = jwtService.getClaims(token);

        if (claims != null) {
            int id = Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        
        return new ResponseEntity<>("null", HttpStatus.OK);
    }

    @GetMapping("/api/user/logout")
    public ResponseEntity<?> logout(HttpServletResponse res) {
        // 쿠키를 삭제하여 로그아웃 처리
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // 개발 환경에서는 false, 운영 환경에서는 true로 설정
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 만료

        res.addCookie(cookie);

        return new ResponseEntity<>(Map.of("message", "로그아웃되었습니다."), HttpStatus.OK); // 200 로그아웃 성공
    }

}// class
