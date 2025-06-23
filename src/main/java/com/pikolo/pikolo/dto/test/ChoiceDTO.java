package com.pikolo.pikolo.dto.test;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChoiceDTO {
    private Long choiceId;
    private Long questionId;
    private Integer choiceNo;
    private String choiceText;
    private Integer score;
}
