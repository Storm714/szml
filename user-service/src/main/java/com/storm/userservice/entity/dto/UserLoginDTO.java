package com.storm.userservice.entity.dto;

import lombok.Data;

// 用户登录DTO
@Data
public class UserLoginDTO {

    private String username;

    private String password;
}
