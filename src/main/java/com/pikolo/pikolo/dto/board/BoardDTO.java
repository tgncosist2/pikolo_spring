package com.pikolo.pikolo.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDTO {
    
    private String id;
    private String title;
    private String author;
    private String category;
    private String createAt;
    private String views;
    private String isNew;
    
}
