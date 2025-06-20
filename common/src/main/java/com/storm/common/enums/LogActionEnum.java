package com.storm.common.enums;

// 日志操作枚举
public enum LogActionEnum {

    REGISTER("register", "用户注册"),
    LOGIN("login", "用户登录"),
    UPDATE_USER("update_user", "修改用户信息"),
    RESET_PASSWORD("reset_password", "重置密码"),
    UPGRADE_ROLE("upgrade_role", "角色升级"),
    DOWNGRADE_ROLE("downgrade_role", "角色降级");

    private final String code;
    private final String description;

    LogActionEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
