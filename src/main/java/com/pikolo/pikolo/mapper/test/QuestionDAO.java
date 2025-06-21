package com.pikolo.pikolo.mapper.test;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.pikolo.pikolo.dto.test.QuestionDTO;

@Mapper
public interface QuestionDAO {
    List<QuestionDTO> selectQuestionKO(int contentId);
    List<QuestionDTO> selectQuestionEN(int contentId);
    List<QuestionDTO> selectQuestionJP(int contentId);
    Integer selectTotalCount(int contentId);
}
