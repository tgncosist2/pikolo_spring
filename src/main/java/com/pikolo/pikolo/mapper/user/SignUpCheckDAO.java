package com.pikolo.pikolo.mapper.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignUpCheckDAO {
    int selectNicknameCheck(String userNickname);
    int selectEmailCheck(String userEmail);
}
