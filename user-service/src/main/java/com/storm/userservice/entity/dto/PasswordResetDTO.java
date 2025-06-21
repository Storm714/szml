package com.storm.userservice.entity.dto;

import lombok.Data;

// 密码重置DTO
@Data
public class PasswordResetDTO {

    private String username;

    private String newPassword;

    private String confirmPassword;
}
