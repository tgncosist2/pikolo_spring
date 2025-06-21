package com.pikolo.pikolo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pikolo.pikolo.dto.ContentDTO;


@Mapper
public interface ContentDAO {
    List<ContentDTO> selectAllContentKO();
    List<ContentDTO> selectAllContentEN();
    List<ContentDTO> selectAllContentJP();
}
