package com.pikolo.pikolo.mapper.user;

import org.apache.ibatis.annotations.Mapper;

import com.pikolo.pikolo.dto.user.LoginRequestDTO;
import com.pikolo.pikolo.dto.user.UserInfoDTO;

@Mapper
public interface LoginDAO {
    UserInfoDTO selectUserLogin(LoginRequestDTO loginRequest);
}
