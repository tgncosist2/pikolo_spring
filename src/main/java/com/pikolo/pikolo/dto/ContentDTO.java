package com.pikolo.pikolo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentDTO {
    private String title, description, imgPath, type;
    private int contentId;
}
