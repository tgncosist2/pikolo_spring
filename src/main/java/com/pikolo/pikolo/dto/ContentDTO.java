package com.pikolo.pikolo.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentDTO {
    private String title, description, category, imgPath;
    private int contentId;
    private Date createdAt, updatedAt; 
}
