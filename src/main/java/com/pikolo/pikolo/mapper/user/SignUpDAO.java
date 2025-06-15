package com.pikolo.pikolo.mapper.user;

import org.apache.ibatis.annotations.Mapper;

import com.pikolo.pikolo.dto.user.SignUpDTO;

@Mapper
public interface SignUpDAO {
    Integer insertSignUp(SignUpDTO signUpDTO);
}
