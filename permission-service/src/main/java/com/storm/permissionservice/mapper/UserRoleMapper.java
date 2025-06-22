package com.storm.permissionservice.mapper;

import java.util.List;

import com.storm.common.dto.Result;
import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storm.permissionservice.entity.UserRole;

// 用户角色关系数据访问层
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    // 根据用户ID查找用户角色关系
    @Select("SELECT * FROM user_roles WHERE user_id = #{userId}")
    List<UserRole> findByUserId(@Param("userId") Long userId);

    // 根据用户ID查找用户角色关系
    @Select("SELECT * FROM user_roles WHERE user_id = #{userId}")
    UserRole selectRoleIdByUserId(@Param("userId") Long userId);

    // 根据用户ID删除所有角色绑定
    @Delete("DELETE FROM user_roles WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    // 检查用户是否已绑定指定角色
    @Select("SELECT COUNT(1) FROM user_roles WHERE user_id = #{userId} AND role_id = #{roleId}")
    boolean existsByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Integer roleId);

    // 检查用户是否存在（通过用户ID）
    @Select("SELECT COUNT(1) FROM user_roles WHERE user_id = #{userId}")
    int existsUserByUserId(@Param("userId") Long userId);

    // 获取用户的角色信息（关联查询）
    @Select("SELECT r.role_code FROM user_roles ur " +
            "JOIN roles r ON ur.role_id = r.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> getUserRoleCodes(@Param("userId") Long userId);

    @Select("SELECT role_code FROM user_roles WHERE user_id = #{userId}")
    String getUserRoleCode(@Param("userId") Long userId);

    @Select("SELECT user_id FROM user_roles WHERE role_code != #{roleCode}")
    List<Long> selectAdminsAndUsers(@Param("roleCode") String roleCode);

    @Select("SELECT user_id FROM user_roles WHERE role_code = 'USER'")
    List<Long> selectUsers();

    @Update("UPDATE user_roles SET role_code = #{roleCode}, role_id = #{roleId} WHERE user_id = #{userId}")
    void updateUserRole(@Param("userId") Long userId, @Param("roleId") Integer roleId, @Param("roleCode") String roleCode);
}