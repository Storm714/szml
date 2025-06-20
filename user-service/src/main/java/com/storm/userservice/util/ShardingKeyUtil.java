package com.storm.userservice.util;

import org.springframework.stereotype.Component;

@Component
public class ShardingKeyUtil {

    // 根据用户ID计算分库索引
    public static int getDatabaseIndex(Long userId) {
        return Math.toIntExact(userId % 2);
    }

    // 根据用户ID计算分表索引
    public static int getTableIndex(Long userId) {
        return Math.toIntExact(userId % 4);
    }

    // 获取数据库名称
    public static String getDatabaseName(Long userId) {
        return "ds" + getDatabaseIndex(userId);
    }

    // 获取表名称
    public static String getTableName(Long userId) {
        return "users_" + getTableIndex(userId);
    }
}
