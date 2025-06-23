package com.pikolo.pikolo.mapper.lookalike;

import org.apache.ibatis.annotations.Mapper;

import com.pikolo.pikolo.dto.ContentDTO;

@Mapper
public interface LookAlikeDAO {
    ContentDTO selectLookalikeKO(int id);
    ContentDTO selectLookalikeEN(int id);
    ContentDTO selectLookalikeJP(int id);
}
