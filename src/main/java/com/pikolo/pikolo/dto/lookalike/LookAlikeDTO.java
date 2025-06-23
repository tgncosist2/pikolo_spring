package com.pikolo.pikolo.dto.lookalike;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LookAlikeDTO {
    private String celebrityName;
    private String celebrityImage;
    private String occupation;
    private int age;
    private int score;
}
