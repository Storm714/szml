package com.storm.permissionservice.client;

import com.storm.common.dto.Result;
import com.storm.permissionservice.entity.UserRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// RPC接口
@RestController
public interface PermissionServiceClient {

    // 绑定默认角色
    @PostMapping("/bind-default-role")
    Result<Void> bindDefaultRole(@RequestParam("userId") Long userId);

    // 查询用户角色码
    @GetMapping("/user-role-code")
    Result<String> getUserRoleCode(@RequestParam("userId") Long userId);

    // 超管调用：升级用户为管理员
    @PostMapping("/upgrade-to-admin")
    Result<Void> upgradeToAdmin(@RequestParam("userId") Long userId);

    // 超管调用：降级用户为普通角色
    @PostMapping("/downgrade-to-user")
    Result<Void> downgradeToUser(@RequestParam("userId") Long userId);

    // 初始化调用：升级用户为超级管理员
    @PostMapping("/upgrade-to-super-admin")
    Result<Void> upgradeToSuperAdmin(@RequestParam("userId") Long userId);

    @GetMapping("/user-all-roles")
    List<Long> selectUsersByRoleWithPage(
            @RequestParam("roleCode") String roleCode,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    );
}
