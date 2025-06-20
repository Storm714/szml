package com.storm.userservice.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

// 用户响应DTO
@Data
public class UserResponseDTO {

    private Long userId;

    private String username;

    private String email;

    private String phone;

    private String roleCode;

    private String roleName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
}
