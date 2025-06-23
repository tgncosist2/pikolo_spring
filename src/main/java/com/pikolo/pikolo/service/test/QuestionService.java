package com.pikolo.pikolo.service.test;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pikolo.pikolo.dto.test.QuestionDTO;
import com.pikolo.pikolo.mapper.test.QuestionDAO;

@Service
public class QuestionService {

    @Autowired
    private QuestionDAO questionDAO;

    /**
     * 언어별 문제와 선택지 조회
     */
    public List<QuestionDTO> getQuestions(int contentId, String lang) {
        switch (lang) {
            case "ko":
                return questionDAO.selectQuestionKO(contentId);
            case "en":
                return questionDAO.selectQuestionEN(contentId);
            case "jp":
                return questionDAO.selectQuestionJP(contentId);
            default:
                return questionDAO.selectQuestionKO(contentId);
        }
    }
    
    /**
     * 전체 문제 수 조회
     */
    public Integer getTotalQuestionCount(int contentId) {
        return questionDAO.selectTotalCount(contentId);
    }
}
