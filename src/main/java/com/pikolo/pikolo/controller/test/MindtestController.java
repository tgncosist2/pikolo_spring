package com.pikolo.pikolo.controller.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.ContentDTO;
import com.pikolo.pikolo.dto.test.MindtestResultDTO;
import com.pikolo.pikolo.dto.test.QuestionDTO;
import com.pikolo.pikolo.service.test.MindtestService;
import com.pikolo.pikolo.service.test.QuestionService;

@RestController
public class MindtestController {
    
    @Autowired
    private MindtestService mindtestService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/api/mindtest")
    public ContentDTO selectMindtest(@RequestParam("id") int id, @RequestParam("language") String language) {
        return mindtestService.selectMindtest(id, language);
    }

    @GetMapping("/api/mindtest/question/total")
    public Integer selectMindtestQuestionTotal(@RequestParam("id") int id) {
        return questionService.getTotalQuestionCount(id);
    }

    @GetMapping("/api/mindtest/question")
    public List<QuestionDTO> selectMindtestQuestion(@RequestParam("id") int id, @RequestParam("language") String language) {
        return questionService.getQuestions(id, language);
    }

    @GetMapping("/api/mindtest/result")
    public MindtestResultDTO selectMindtestResult(@RequestParam("id") int id, @RequestParam("score") int score, @RequestParam("language") String language) {
        return mindtestService.selectMindtestResult(id, score, language);
    }
}
