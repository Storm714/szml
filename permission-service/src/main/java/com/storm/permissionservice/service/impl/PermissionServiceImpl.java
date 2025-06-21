package com.storm.permissionservice.service.impl;

import com.storm.common.dto.Result;
import com.storm.permissionservice.entity.UserRole;
import com.storm.permissionservice.mapper.UserRoleMapper;
import com.storm.permissionservice.service.PermissionService;
import enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public Result<Void> bindDefaultRole(Long userId) {
        log.info("绑定默认角色请求: userId={}", userId);

        try {
            // 检查用户是否已经绑定了角色
            List<UserRole> existingRoles = userRoleMapper.findByUserId(userId);
            if (!existingRoles.isEmpty()) {
                log.info("用户已绑定角色: userId={}, roleCount={}", userId, existingRoles.size());
                return Result.success();
            }

            // 绑定默认角色（普通用户）
            UserRole userRole = UserRole.builder()
                    .userId(userId)
                    .roleId(RoleEnum.USER.getRoleId())
                    .build();

            userRoleMapper.insert(userRole);
            log.info("绑定默认角色成功: userId={}, roleId={}", userId, RoleEnum.USER.getRoleId());

            return Result.success();

        } catch (Exception e) {
            log.error("绑定默认角色失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("绑定默认角色失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Result<String> getUserRoleCode(Long userId) {
        log.info("查询用户角色码请求: userId={}", userId);

        try {
            List<String> roleCodes = userRoleMapper.getUserRoleCodes(userId);

            if (roleCodes.isEmpty()) {
                log.warn("用户未绑定任何角色: userId={}", userId);
                return Result.error("用户未绑定任何角色");
            }

            // 返回最高权限的角色
            String highestRoleCode = getHighestRoleCode(roleCodes);
            log.info("查询用户角色码成功: userId={}, roleCode={}", userId, highestRoleCode);

            return Result.success(highestRoleCode);

        } catch (Exception e) {
            log.error("查询用户角色码失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("查询用户角色码失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> upgradeToAdmin(Long userId) {
        log.info("升级用户为管理员请求: userId={}", userId);

        try {
            // 检查用户当前角色
            List<String> currentRoleCodes = userRoleMapper.getUserRoleCodes(userId);
            if (currentRoleCodes.isEmpty()) {
                return Result.error("用户未绑定任何角色，请先绑定默认角色");
            }

            // 检查用户是否已经是管理员或超管
            if (currentRoleCodes.contains(RoleEnum.SUPER_ADMIN.getRoleCode())) {
                return Result.error("用户已经是超级管理员，无需升级");
            }

            if (currentRoleCodes.contains(RoleEnum.ADMIN.getRoleCode())) {
                return Result.error("用户已经是管理员");
            }

            // 添加管理员角色（保留原有角色）
            if (!userRoleMapper.existsByUserIdAndRoleId(userId, RoleEnum.ADMIN.getRoleId())) {
                UserRole adminRole = UserRole.builder()
                        .userId(userId)
                        .roleId(RoleEnum.ADMIN.getRoleId())
                        .build();

                userRoleMapper.insert(adminRole);
            }

            log.info("升级用户为管理员成功: userId={}", userId);
            return Result.success();

        } catch (Exception e) {
            log.error("升级用户为管理员失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("升级失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> downgradeToUser(Long userId) {
        log.info("降级用户为普通用户请求: userId={}", userId);

        try {
            // 检查用户当前角色
            List<String> currentRoleCodes = userRoleMapper.getUserRoleCodes(userId);
            if (currentRoleCodes.isEmpty()) {
                return Result.error("用户未绑定任何角色");
            }

            // 检查是否只有普通用户角色
            if (currentRoleCodes.size() == 1 && currentRoleCodes.contains(RoleEnum.USER.getRoleCode())) {
                return Result.error("用户已经是普通用户");
            }

            // 删除所有角色绑定
            userRoleMapper.deleteByUserId(userId);

            // 重新绑定普通用户角色
            UserRole userRole = UserRole.builder()
                    .userId(userId)
                    .roleId(RoleEnum.USER.getRoleId())
                    .build();

            userRoleMapper.insert(userRole);

            log.info("降级用户为普通用户成功: userId={}", userId);
            return Result.success();

        } catch (Exception e) {
            log.error("降级用户为普通用户失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("降级失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> upgradeToSuperAdmin(Long userId) {
        log.info("升级用户为超级管理员请求: userId={}", userId);

        try {
            // 检查用户当前角色
            List<String> currentRoleCodes = userRoleMapper.getUserRoleCodes(userId);
            if (currentRoleCodes.isEmpty()) {
                return Result.error("用户未绑定任何角色，请先绑定默认角色");
            }

            // 检查用户是否已经是超管
            if (currentRoleCodes.contains(RoleEnum.SUPER_ADMIN.getRoleCode())) {
                return Result.error("用户已经是超级管理员");
            }

            // 添加超级管理员角色（保留原有角色）
            if (!userRoleMapper.existsByUserIdAndRoleId(userId, RoleEnum.SUPER_ADMIN.getRoleId())) {
                UserRole superAdminRole = UserRole.builder()
                        .userId(userId)
                        .roleId(RoleEnum.SUPER_ADMIN.getRoleId())
                        .build();

                userRoleMapper.insert(superAdminRole);
            }

            log.info("升级用户为超级管理员成功: userId={}", userId);
            return Result.success();

        } catch (Exception e) {
            log.error("升级用户为超级管理员失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("升级失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Result<List<String>> getUserAllRoles(Long userId) {
        log.info("获取用户所有角色请求: userId={}", userId);

        try {
            List<String> roleCodes = userRoleMapper.getUserRoleCodes(userId);

            if (roleCodes.isEmpty()) {
                log.warn("用户未绑定任何角色: userId={}", userId);
                return Result.error("用户未绑定任何角色");
            }

            log.info("获取用户所有角色成功: userId={}, roles={}", userId, roleCodes);
            return Result.success(roleCodes);

        } catch (Exception e) {
            log.error("获取用户所有角色失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("获取用户所有角色失败：" + e.getMessage());
        }
    }

    /**
     * 获取最高权限的角色代码
     */
    private String getHighestRoleCode(List<String> roleCodes) {
        // 优先级：SUPER_ADMIN > ADMIN > USER
        if (roleCodes.contains(RoleEnum.SUPER_ADMIN.getRoleCode())) {
            return RoleEnum.SUPER_ADMIN.getRoleCode();
        }
        if (roleCodes.contains(RoleEnum.ADMIN.getRoleCode())) {
            return RoleEnum.ADMIN.getRoleCode();
        }
        return RoleEnum.USER.getRoleCode();
    }
}

