package com.pikolo.pikolo.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String userEmail;
    private String userPassword;
}
