package com.pikolo.pikolo.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.user.SignUpDTO;
import com.pikolo.pikolo.mapper.user.SignUpDAO;

@Service
public class SignUpService {
    
    @Autowired
    private SignUpDAO signUpDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean insertSignUp(SignUpDTO signUpDTO) {
        // 비밀번호 암호화
        String rawPassword = signUpDTO.getUserPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        signUpDTO.setUserPassword(encodedPassword);

        int result = signUpDAO.insertSignUp(signUpDTO);
        if (result > 0) {
            return true; // 회원가입 성공
        } else {
            return false; // 회원가입 실패
        }// end else-if
    }// insertSignUp

}
