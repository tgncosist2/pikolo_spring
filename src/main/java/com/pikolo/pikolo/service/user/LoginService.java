package com.pikolo.pikolo.service.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.user.LoginRequestDTO;
import com.pikolo.pikolo.dto.user.UserInfoDTO;
import com.pikolo.pikolo.mapper.user.LoginDAO;

@Service
public class LoginService {
    
    @Autowired
    private LoginDAO loginMapper;

    public UserInfoDTO getUserLoginInfo(LoginRequestDTO loginRequest) {
        return loginMapper.selectUserLogin(loginRequest);
    }// end getUserLoginInfo

    public void updateUserLoginDate(Map<String, Object> params) {
        loginMapper.updateUserLoginDate(params);
    }// end updateUserLoginDate

}// class
