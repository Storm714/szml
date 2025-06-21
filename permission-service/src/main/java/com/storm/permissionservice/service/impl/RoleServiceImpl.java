package com.storm.permissionservice.service.impl;

import com.storm.common.dto.Result;
import com.storm.permissionservice.entity.Role;
import com.storm.permissionservice.mapper.RoleMapper;
import com.storm.permissionservice.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 用户服务实现类
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Result<List<Role>> getAllRoles() {
        try {
            List<Role> roles = roleMapper.selectList(null);
            return Result.success(roles);        } catch (Exception e) {
            log.error("获取所有角色失败", e);
            return Result.error("获取角色列表失败");
        }
    }

    @Override
    public Result<Role> getRoleByCode(String roleCode) {
        try {
            Role role = roleMapper.findByRoleCode(roleCode);
            if (role == null) {
                return Result.error("角色不存在");
            }
            return Result.success(role);
        } catch (Exception e) {
            log.error("根据角色码获取角色失败: {}", roleCode, e);
            return Result.error("获取角色信息失败");
        }
    }

    @Override
    public Result<Role> getRoleById(Integer roleId) {
        try {
            Role role = roleMapper.selectById(roleId);
            if (role == null) {
                return Result.error("角色不存在");
            }
            return Result.success(role);
        } catch (Exception e) {
            log.error("根据角色ID获取角色失败: {}", roleId, e);
            return Result.error("获取角色信息失败");
        }
    }
}

