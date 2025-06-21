package com.pikolo.pikolo.mapper.test;

import org.apache.ibatis.annotations.Mapper;

import com.pikolo.pikolo.dto.ContentDTO;

@Mapper
public interface MindtestDAO {
    ContentDTO selectMindtestKO(int id);
    ContentDTO selectMindtestEN(int id);
    ContentDTO selectMindtestJP(int id);
}
