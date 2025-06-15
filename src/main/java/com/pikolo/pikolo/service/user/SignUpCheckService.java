package com.pikolo.pikolo.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.mapper.user.SignUpCheckDAO;

@Service
public class SignUpCheckService {
    
    @Autowired
    private SignUpCheckDAO SignUpCheckDAO;

    public boolean isNicknameAvailable(String userNickname) {
        int count = SignUpCheckDAO.selectNicknameCheck(userNickname);
        return count != 1; // 1이 아니면 사용 가능한 닉네임 (결과 값은 0 또는 1)
    }// isNicknameAvailable

    public boolean isEmailAvailable(String userEmail) {
        int count = SignUpCheckDAO.selectEmailCheck(userEmail);
        return count != 1; // 1이 아니면 사용 가능한 이메일 (결과 값은 0 또는 1)
    }// isEmailAvailable
    
}// class
