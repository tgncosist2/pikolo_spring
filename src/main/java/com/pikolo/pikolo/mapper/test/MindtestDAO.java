package com.pikolo.pikolo.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pikolo.pikolo.dto.ContentDTO;
import com.pikolo.pikolo.dto.test.MindtestResultDTO;

@Mapper
public interface MindtestDAO {
    ContentDTO selectMindtestKO(int id);
    ContentDTO selectMindtestEN(int id);
    ContentDTO selectMindtestJP(int id);
    List<MindtestResultDTO> selectMindtestResultKO(int contentId);
    List<MindtestResultDTO> selectMindtestResultEN(int contentId);
    List<MindtestResultDTO> selectMindtestResultJP(int contentId);
}
