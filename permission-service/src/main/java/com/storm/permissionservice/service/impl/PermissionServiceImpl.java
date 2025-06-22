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
import java.util.Objects;

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
                    .roleCode(RoleEnum.USER.getRoleCode())
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
    public Result<Void> upgradeToAdmin(Long userId) {
        log.info("升级用户为管理员请求: userId={}", userId);

        try {
            // 检查用户当前角色
            String currentRoleCode = userRoleMapper.getUserRoleCode(userId);
            if (currentRoleCode.isEmpty()) {
                return Result.error("用户未绑定任何角色，请先绑定默认角色");
            }

            // 检查用户是否已经是管理员或超管
            if (currentRoleCode.contains(RoleEnum.SUPER_ADMIN.getRoleCode())) {
                return Result.error("用户已经是超级管理员，无需升级");
            }

            if (currentRoleCode.contains(RoleEnum.ADMIN.getRoleCode())) {
                return Result.error("用户已经是管理员");
            }

            userRoleMapper.updateUserRole(userId, RoleEnum.ADMIN.getRoleId(), RoleEnum.ADMIN.getRoleCode());

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
            String currentRoleCode = userRoleMapper.getUserRoleCode(userId);
            if (currentRoleCode.isEmpty()) {
                return Result.error("用户未绑定任何角色");
            }

            // 检查是否只有普通用户角色
            if (currentRoleCode.equals("USER")) {
                return Result.error("用户已经是普通用户");
            }

            userRoleMapper.updateUserRole(userId, RoleEnum.USER.getRoleId(), RoleEnum.USER.getRoleCode());

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
            UserRole existingRole = userRoleMapper.selectRoleIdByUserId(userId);

            // 检查用户是否已经是超管
            if (existingRole != null) {
                if (existingRole.getRoleId().equals(1)) {
                    return Result.error("用户已经是超级管理员");
            }

            // 添加超级管理员角色（保留原有角色）
            existingRole.setRoleId(1);
            existingRole.setRoleCode(RoleEnum.SUPER_ADMIN.getRoleCode());
            userRoleMapper.updateById(existingRole);
            log.info("用户 {} 角色已更新为超级管理员", userId);
            } else {
                // 如果用户没有任何角色，直接绑定超级管理员角色
                UserRole superAdminRole = UserRole.builder()
                        .userId(userId)
                        .roleId(RoleEnum.SUPER_ADMIN.getRoleId())
                        .roleCode(RoleEnum.SUPER_ADMIN.getRoleCode())
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

    @Override
    public Result<String> getUserRoleCode(Long userId) {
        log.info("查询用户角色码请求: userId={}", userId);

        try {
            if (userRoleMapper.existsUserByUserId(userId) == 0) {
                log.warn("用户未绑定任何角色: userId={}", userId);
                return Result.error("用户未绑定任何角色");
            }
            String result = userRoleMapper.getUserRoleCode(userId);
            log.info("查询用户角色码成功: userId={}, roleCode={}", userId, result);
            return Result.success(result);

        } catch (Exception e) {
            log.error("查询用户角色码失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("查询用户角色码失败：" + e.getMessage());
        }
    }

    @Override
    public List<Long> selectUsersByRoleWithPage(String roleCode, Integer offset, Integer size) {
        log.info("查询角色用户列表请求: roleCode={}, offset={}, size={}", roleCode, offset, size);
        if (Objects.equals(roleCode, "SUPER_ADMIN")) {
            return userRoleMapper.selectAdminsAndUsers("SUPER_ADMIN");
        } else if (Objects.equals(roleCode, "ADMIN")) {
            return userRoleMapper.selectUsers();
        } else {
            log.warn("未知角色代码: {}", roleCode);
            return List.of();
        }
    }
}

