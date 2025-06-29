package com.pikolo.pikolo.service.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.notice.NoticeDTO;
import com.pikolo.pikolo.mapper.notice.NoticeDAO;

@Service
public class NoticeService {
    
@Autowired
private NoticeDAO noticeDAO;

public List<NoticeDTO> searchNoticeList(String language) {
    switch (language) {
        case "ko":
            return noticeDAO.selectNoticeListKo();
        case "en":
            return noticeDAO.selectNoticeListEn();
        case "jp":
            return noticeDAO.selectNoticeListJp();
        default:
            return noticeDAO.selectNoticeListKo();
    }
}

}
