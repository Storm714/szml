package com.storm.permissionservice.service;

import com.storm.common.dto.Result;
import com.storm.permissionservice.entity.Role;

import java.util.List;

// 用户服务接口
public interface RoleService {

    Result<List<Role>> getAllRoles();

    Result<Role> getRoleByCode(String roleCode);

    Result<Role> getRoleById(Integer roleId);
}
