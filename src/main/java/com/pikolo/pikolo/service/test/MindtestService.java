package com.pikolo.pikolo.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.ContentDTO;
import com.pikolo.pikolo.dto.test.MindtestResultDTO;
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

    public MindtestResultDTO selectMindtestResult(int contentId, int score, String lang) {
        switch (lang) {
            case "ko":
                MindtestResultDTO resultKo = null;

                for (MindtestResultDTO item : mindtestDAO.selectMindtestResultKO(contentId)) {
                    if (score >= item.getScoreMin() && score <= item.getScoreMax()) {
                        resultKo = item;
                        break;
                    }
                }

                return resultKo;
            case "en":
                MindtestResultDTO resultEn = null;

                for (MindtestResultDTO item : mindtestDAO.selectMindtestResultEN(contentId)) {
                    if (score >= item.getScoreMin() && score <= item.getScoreMax()) {
                        resultEn = item;
                        break;
                    }
                }

                return resultEn;
            case "jp":
                MindtestResultDTO resultJp = null;

                for (MindtestResultDTO item : mindtestDAO.selectMindtestResultJP(contentId)) {
                    if (score >= item.getScoreMin() && score <= item.getScoreMax()) {
                        resultJp = item;
                        break;
                    }
                }

                return resultJp;
            default:
                MindtestResultDTO resultDefault = null;

                for (MindtestResultDTO item : mindtestDAO.selectMindtestResultKO(contentId)) {
                    if (score >= item.getScoreMin() && score <= item.getScoreMax()) {
                        resultDefault = item;
                        break;
                    }
                }

                return resultDefault;
        }
    }

}
