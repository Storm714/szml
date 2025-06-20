package com.storm.userservice.entity.dto;

import lombok.Data;

// 用户更新DTO
@Data
public class UserUpdateDTO {

    private String email;

    private String phone;

    private String oldPassword;

    private String newPassword;
}
