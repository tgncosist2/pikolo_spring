package com.pikolo.pikolo.controller.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.notice.NoticeDTO;
import com.pikolo.pikolo.service.notice.NoticeService;

@RestController
public class NoticeController {
    
    @Autowired
    private NoticeService noticeService;
    
    @GetMapping("/api/notice/list")
    public List<NoticeDTO> searchNoticeList(@RequestParam("language") String lang) { 
        return noticeService.searchNoticeList(lang);
    }

}
