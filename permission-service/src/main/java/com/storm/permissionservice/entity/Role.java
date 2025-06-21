package com.storm.permissionservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("roles")
public class Role {

    @TableId(value = "role_id")
    private Integer roleId;

    @TableField("role_code")
    private String roleCode;

    @TableField("role_name")
    private String roleName;
}
