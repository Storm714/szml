package com.storm.permissionservice.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storm.permissionservice.entity.OperationLog;


// 操作日志数据访问层
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    // 根据用户ID查询操作日志
    @Select("SELECT * FROM operation_logs WHERE user_id = #{userId} ORDER BY gmt_create DESC LIMIT #{limit}")
    List<OperationLog> findByUserId(@Param("userId") Long userId, @Param("limit") int limit);

    // 根据操作类型查询日志
    @Select("SELECT * FROM operation_logs WHERE action = #{action} ORDER BY gmt_create DESC LIMIT #{limit}")
    List<OperationLog> findByAction(@Param("action") String action, @Param("limit") int limit);

    // 根据时间范围查询日志
    @Select("SELECT * FROM operation_logs WHERE gmt_create BETWEEN #{startTime} AND #{endTime} ORDER BY gmt_create DESC")
    List<OperationLog> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // 清理过期日志
    @Select("DELETE FROM operation_logs WHERE gmt_create < #{expireTime}")
    int deleteExpiredLogs(@Param("expireTime") LocalDateTime expireTime);
}

