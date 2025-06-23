package com.pikolo.pikolo.dto.lookalike;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LookAlikeResultDTO {
    private String name;
    private int age;
    private String actor;
    private String score;
    private String originalImagePath;
    private String resultImageUrl;
    private String koreanName;
    private String koreanActor;
    private int koreanAge;
}
