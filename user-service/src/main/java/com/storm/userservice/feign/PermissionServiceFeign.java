package com.storm.userservice.feign;

import com.storm.common.dto.Result;
import com.storm.userservice.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "permission-service")
public interface PermissionServiceFeign {

    // 绑定默认角色（普通用户）
    @PostMapping("/permission/bind-default-role")
    Result<Void> bindDefaultRole(@RequestParam("userId") Long userId);

    // 查询用户角色码（返回role_code）
    @GetMapping("/permission/user-role-code")
    Result<String> getUserRoleCode(@RequestParam("userId") Long userId);

    // 超管调用：升级用户为管理员
    @PostMapping("/permission/upgrade-to-admin")
    Result<Void> upgradeToAdmin(@RequestParam("userId") Long userId);

    // 超管调用：降级用户为普通角色
    @PostMapping("/permission/downgrade-to-user")
    Result<Void> downgradeToUser(@RequestParam("userId") Long userId);

    // 初始化调用：升级用户为超级管理员
    @PostMapping("/permission/upgrade-to-super-admin")
    Result<Void> upgradeToSuperAdmin(@RequestParam("userId") Long userId);

    @GetMapping("/permission/users-by-role")
    List<Long> selectUsersByRoleWithPage(
            @RequestParam("roleCode") String roleCode,
            @RequestParam("pageNum") Integer offset,
            @RequestParam("pageSize") Integer size
    );
}
