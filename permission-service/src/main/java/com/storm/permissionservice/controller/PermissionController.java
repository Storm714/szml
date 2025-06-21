package com.storm.permissionservice.controller;

import com.storm.common.dto.Result;
import com.storm.permissionservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    // 绑定默认角色
    @PostMapping("/bind-default-role")
    public Result<Void> bindDefaultRole(@RequestParam Long userId) {
        return permissionService.bindDefaultRole(userId);
    }

    // 获取用户角色码
    @GetMapping("/user-role-code")
    public Result<String> getUserRoleCode(@RequestParam Long userId) {
        return permissionService.getUserRoleCode(userId);
    }

    // 升级用户为管理员
    @PostMapping("/upgrade-to-admin")
    public Result<Void> upgradeToAdmin(@RequestParam Long userId) {
        return permissionService.upgradeToAdmin(userId);
    }

    // 降级用户为普通用户
    @PostMapping("/downgrade-to-user")
    public Result<Void> downgradeToUser(@RequestParam Long userId) {
        return permissionService.downgradeToUser(userId);
    }

    // 升级用户为超级管理员
    @PostMapping("/upgrade-to-super-admin")
    public Result<Void> upgradeToSuperAdmin(@RequestParam Long userId) {
        return permissionService.upgradeToSuperAdmin(userId);
    }

    // 获取用户所有角色
    @GetMapping("/user-all-roles")
    public Result<java.util.List<String>> getUserAllRoles(@RequestParam Long userId) {
        return permissionService.getUserAllRoles(userId);
    }
}
