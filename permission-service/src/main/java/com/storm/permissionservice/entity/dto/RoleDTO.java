package com.storm.permissionservice.entity.dto;

import lombok.Data;

// 角色DTO
@Data
public class RoleDTO {

    private Integer roleId;

    private String roleCode;

    private String roleName;
}
