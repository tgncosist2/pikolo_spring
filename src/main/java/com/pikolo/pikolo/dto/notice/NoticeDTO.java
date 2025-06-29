package com.pikolo.pikolo.dto.notice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeDTO {
    
    private String id;
    private String title;
    private String summary;
    private String category;
    private String createdAt;
    private String views;
    private String isNew;
    
}
