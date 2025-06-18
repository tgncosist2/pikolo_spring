package com.pikolo.pikolo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.ContentDTO;
import com.pikolo.pikolo.service.ContentService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/api/content")
    public List<ContentDTO> getAllItems() {
        return contentService.getAllContent();
    }
}

