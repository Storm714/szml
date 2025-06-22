package enums;

import lombok.Data;
import lombok.Getter;

// 角色枚举
@Getter
public enum RoleEnum {

    // 超级管理员
    SUPER_ADMIN(1, "SUPER_ADMIN", "超级管理员"),

    // 普通用户
    USER(2, "USER", "普通用户"),

    // 管理员
    ADMIN(3, "ADMIN", "管理员");

    private final Integer roleId;
    private final String roleCode;
    private final String roleName;

    RoleEnum(Integer roleId, String roleCode, String roleName) {
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public static RoleEnum getByRoleId(Integer roleId) {
        for (RoleEnum role : values()) {
            if (role.getRoleId().equals(roleId)) {
                return role;
            }
        }
        return null;
    }

    public static RoleEnum getByRoleCode(String roleCode) {
        for (RoleEnum role : values()) {
            if (role.getRoleCode().equals(roleCode)) {
                return role;
            }
        }
        return null;
    }
}
