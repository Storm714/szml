package com.storm.permissionservice.entity.dto;

import lombok.Data;

// 用户升级/降级请求DTO
@Data
public class RoleChangeDTO {

    private Long userId;

    private String targetRoleCode;

    private String reason;
}

