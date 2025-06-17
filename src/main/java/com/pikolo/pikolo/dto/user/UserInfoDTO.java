package com.pikolo.pikolo.dto.user;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {
    private int userId;
    private String userEmail;
    private String userName;
    private String userPassword;
    private String userNickname;
    private String userStatus;
    private Date inputDate;
    private Date lastLoginDate;
    private String inputIp;
    private String lastLoginIp;
    private String userRole;
}
