package com.pikolo.pikolo.dto.test;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MindtestResultDTO {
    private int resultId;
    private int contentId;
    private int scoreMin;
    private int scoreMax;
    private String resultTitle;
    private String resultText;
}
