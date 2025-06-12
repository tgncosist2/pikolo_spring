package com.pikolo.pikolo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    List<Integer> selectAllEmpno();
}// interface
