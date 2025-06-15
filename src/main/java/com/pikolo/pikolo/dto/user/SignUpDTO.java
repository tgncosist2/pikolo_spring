package com.pikolo.pikolo.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {
    private String userEmail;
    private String userName;
    private String userPassword;
    private String userNickname;
    private String inputIp;
}
