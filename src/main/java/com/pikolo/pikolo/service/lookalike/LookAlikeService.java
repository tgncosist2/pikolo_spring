package com.pikolo.pikolo.service.lookalike;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.ContentDTO;
import com.pikolo.pikolo.mapper.lookalike.LookAlikeDAO;

@Service
public class LookAlikeService {

    @Autowired
    private LookAlikeDAO lookAlikeDAO;

    public ContentDTO searchLookAlike(int id, String lang) {
        switch (lang) {
            case "ko":
                return lookAlikeDAO.selectLookalikeKO(id);
            case "en":
                return lookAlikeDAO.selectLookalikeEN(id);
            case "jp":
                return lookAlikeDAO.selectLookalikeJP(id);
            default:
                return lookAlikeDAO.selectLookalikeKO(id);
        }
    }

}
