package com.pikolo.pikolo.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.ContentDTO;
import com.pikolo.pikolo.mapper.test.MindtestDAO;

@Service
public class MindtestService {

    @Autowired
    private MindtestDAO mindtestDAO;

    public ContentDTO selectMindtest(int id, String lang) {
        switch (lang) {
            case "ko":
                return mindtestDAO.selectMindtestKO(id);
            case "en":
                return mindtestDAO.selectMindtestEN(id);
            case "jp":
                return mindtestDAO.selectMindtestJP(id);
            default:
                return mindtestDAO.selectMindtestKO(id);
        }
    }

}
