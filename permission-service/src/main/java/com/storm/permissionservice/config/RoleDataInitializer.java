package com.storm.permissionservice.config;

import com.storm.permissionservice.entity.Role;
import com.storm.permissionservice.mapper.RoleMapper;
import enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// 角色数据初始化
@Slf4j
@Component
public class RoleDataInitializer implements CommandLineRunner {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化角色数据...");

        // 初始化所有角色
        for (RoleEnum roleEnum : RoleEnum.values()) {
            Role existingRole = roleMapper.findByRoleCode(roleEnum.getRoleCode());
            if (existingRole == null) {
                Role role = Role.builder()
                        .roleId(roleEnum.getRoleId())
                        .roleCode(roleEnum.getRoleCode())
                        .roleName(roleEnum.getRoleName())
                        .build();

                roleMapper.insert(role);
                log.info("初始化角色: {} - {}", roleEnum.getRoleCode(), roleEnum.getRoleName());
            }
        }

        log.info("角色数据初始化完成");
    }
}