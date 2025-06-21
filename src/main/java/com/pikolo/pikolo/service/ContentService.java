package com.pikolo.pikolo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.ContentDTO;
import com.pikolo.pikolo.mapper.ContentDAO;


@Service
public class ContentService {
    
    @Autowired
    private ContentDAO contentMapper;

    public List<ContentDTO> getAllContent(String lang) {
        switch (lang) {
            case "ko":
                return contentMapper.selectAllContentKO();
            case "en":
                return contentMapper.selectAllContentEN();
            case "jp":
                return contentMapper.selectAllContentJP();
            default:
                return contentMapper.selectAllContentKO();
        }
    }
}
