package com.storm.permissionservice.service;

import com.storm.common.dto.Result;

import java.util.List;

// 权限服务接口
public interface PermissionService {

    Result<Void> bindDefaultRole(Long userId);

    Result<String> getUserRoleCode(Long userId);

    Result<Void> upgradeToAdmin(Long userId);

    Result<Void> upgradeToSuperAdmin(Long userId);

    Result<Void> downgradeToUser(Long userId);

    Result<List<String>> getUserAllRoles(Long userId);

    List<Long> selectUsersByRoleWithPage(String roleCode, Integer offset, Integer size);
}

