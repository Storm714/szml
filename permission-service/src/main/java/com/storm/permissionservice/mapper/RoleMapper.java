package com.storm.permissionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storm.permissionservice.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

// 角色数据访问层
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    // 根据角色代码查找角色
    @Select("SELECT * FROM roles WHERE role_code = #{roleCode}")
    Role findByRoleCode(@Param("roleCode") String roleCode);

    // 检查角色代码是否存在
    @Select("SELECT COUNT(1) FROM roles WHERE role_code = #{roleCode}")
    boolean existsByRoleCode(@Param("roleCode") String roleCode);
}
