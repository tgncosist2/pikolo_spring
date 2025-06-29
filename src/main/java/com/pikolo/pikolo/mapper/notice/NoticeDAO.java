package com.pikolo.pikolo.mapper.notice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pikolo.pikolo.dto.notice.NoticeDTO;

@Mapper
public interface NoticeDAO {
    public List<NoticeDTO> selectNoticeListKo();
    public List<NoticeDTO> selectNoticeListEn();
    public List<NoticeDTO> selectNoticeListJp();
}
