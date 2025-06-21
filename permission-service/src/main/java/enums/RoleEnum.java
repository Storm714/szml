package enums;

// 角色枚举
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

    public Integer getRoleId() {
        return roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    // 根据角色ID获取角色枚举
    public static RoleEnum getByRoleId(Integer roleId) {
        for (RoleEnum role : values()) {
            if (role.getRoleId().equals(roleId)) {
                return role;
            }
        }
        return null;
    }

    // 根据角色代码获取角色枚举
    public static RoleEnum getByRoleCode(String roleCode) {
        for (RoleEnum role : values()) {
            if (role.getRoleCode().equals(roleCode)) {
                return role;
            }
        }
        return null;
    }
}
