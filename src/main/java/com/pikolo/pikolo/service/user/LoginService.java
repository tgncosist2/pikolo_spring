package com.pikolo.pikolo.service.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.user.LoginRequestDTO;
import com.pikolo.pikolo.dto.user.UserInfoDTO;
import com.pikolo.pikolo.mapper.user.LoginDAO;

@Service
public class LoginService {

    @Autowired
    private LoginDAO loginMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfoDTO getUserLoginInfo(LoginRequestDTO loginRequest) {
        // 1. 사용자 ID로 DB에서 사용자 정보 조회 (암호화된 비밀번호 포함)
        UserInfoDTO userInfo = loginMapper.selectUserLogin(loginRequest);

        // 2. 사용자가 존재하지 않으면 null 반환
        if (userInfo == null) {
            return null;
        }

        // 3. 입력받은 평문 비밀번호와 DB의 암호화된 비밀번호 비교
        String rawPassword = loginRequest.getUserPassword();
        String encodedPasswordFromDB = userInfo.getUserPassword();

        // 4. 비밀번호 일치 여부 확인
        if (passwordEncoder.matches(rawPassword, encodedPasswordFromDB)) {
            return userInfo; // 로그인 성공
        } else {
            return null; // 비밀번호 불일치
        }
    }// end getUserLoginInfo

    public void updateUserLoginDate(Map<String, Object> params) {
        loginMapper.updateUserLoginDate(params);
    }// end updateUserLoginDate

}// class
