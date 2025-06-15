package com.pikolo.pikolo.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.service.user.SignUpCheckService;

@RestController
public class SignUpCheckController {
    
    @Autowired
    private SignUpCheckService SignUpCheckService;
    
    @GetMapping("/api/user/nicknameCheck")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String userNickname) {
        boolean isAvailable = SignUpCheckService.isNicknameAvailable(userNickname);
        
        if (isAvailable) {
            return ResponseEntity.ok(Map.of("result", "success"));
        } else {
            return ResponseEntity.ok(Map.of("result", "fail"));
        }// end else-if
    }// checkNickname

    @GetMapping("/api/user/emailCheck")
    public ResponseEntity<?> checkEmail(@RequestParam("email") String userEmail) {
        boolean isAvailable = SignUpCheckService.isEmailAvailable(userEmail);
        
        if (isAvailable) {
            return ResponseEntity.ok(Map.of("result", "success"));
        } else {
            return ResponseEntity.ok(Map.of("result", "fail"));
        }// end else-if
    }// checkEmail

}// class
