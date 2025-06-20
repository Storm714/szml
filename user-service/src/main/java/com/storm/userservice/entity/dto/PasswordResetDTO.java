package com.storm.userservice.entity.dto;

import lombok.Data;

// 密码重置DTO
@Data
public class PasswordResetDTO {

    private Long id;

    private String newPassword;

    private String confirmPassword;
}
