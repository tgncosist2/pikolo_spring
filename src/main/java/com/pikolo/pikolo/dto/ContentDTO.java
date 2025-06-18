package com.pikolo.pikolo.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentDTO {
    private String title, description, category;
    private int content_id;
    private Date createdAt, updatedAt; 
}
