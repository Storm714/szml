package com.storm.permissionservice.service;

import com.storm.common.dto.Result;

// 权限服务接口
public interface PermissionService {

    Result<Void> bindDefaultRole(Long userId);

    Result<String> getUserRoleCode(Long userId);

    Result<Void> upgradeToAdmin(Long userId);

    Result<Void> upgradeToSuperAdmin(Long userId);

    Result<Void> downgradeToUser(Long userId);

    Result<java.util.List<String>> getUserAllRoles(Long userId);
}

