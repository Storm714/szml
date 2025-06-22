package com.storm.userservice.entity.dto;

import lombok.Data;

// 用户更新DTO
@Data
public class UserUpdateDTO {

    private String username;

    private String email;

    private String phone;
}
