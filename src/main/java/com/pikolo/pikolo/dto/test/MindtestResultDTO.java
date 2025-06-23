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
    private String resultKeyPoint1Title;
    private String resultKeyPoint1Desc;
    private String resultKeyPoint2Title;
    private String resultKeyPoint2Desc;
    private String resultKeyPoint3Title;
    private String resultKeyPoint3Desc;
    private String resultTitle;
    private String resultText;
}
