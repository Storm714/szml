package com.storm.permissionservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storm.permissionservice.entity.UserRole;

// 用户角色关系数据访问层
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    // 根据用户ID查找用户角色关系
    @Select("SELECT * FROM user_roles WHERE user_id = #{userId}")
    List<UserRole> findByUserId(@Param("userId") Long userId);

    // 根据用户ID删除所有角色绑定
    @Delete("DELETE FROM user_roles WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    // 检查用户是否已绑定指定角色
    @Select("SELECT COUNT(1) FROM user_roles WHERE user_id = #{userId} AND role_id = #{roleId}")
    boolean existsByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Integer roleId);

    // 获取用户的角色信息（关联查询）
    @Select("SELECT r.role_code FROM user_roles ur " +
            "JOIN roles r ON ur.role_id = r.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> getUserRoleCodes(@Param("userId") Long userId);
}