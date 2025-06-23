package com.pikolo.pikolo.dto.test;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class QuestionDTO {
    private Long questionId;
    private Long contentId;
    private Integer questionNo;
    private String questionText;
    private List<ChoiceDTO> choices;
}