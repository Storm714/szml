package com.storm.permissionservice.entity.dto;

import lombok.Data;

// 用户角色DTO
@Data
public class UserRoleDTO {

    private Long id;

    private Long userId;

    private Integer roleId;

    private String roleCode;

    private String roleName;
}
