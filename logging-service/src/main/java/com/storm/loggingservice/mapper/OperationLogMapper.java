package com.storm.loggingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storm.loggingservice.entity.OperationLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

// 操作日志Mapper接口
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    // 分页查询用户操作日志
    @Select("SELECT * FROM operation_logs WHERE user_id = #{userId} ORDER BY gmt_create DESC LIMIT #{offset}, #{size}")
    List<OperationLog> selectByUserIdWithPage(@Param("userId") Long userId,
                                              @Param("offset") int offset,
                                              @Param("size") int size);

    // 分页查询所有操作日志
    @Select("SELECT * FROM operation_logs ORDER BY gmt_create DESC LIMIT #{offset}, #{size}")
    List<OperationLog> selectAllWithPage(@Param("offset") int offset,
                                         @Param("size") int size);


    @Select("SELECT * FROM operation_logs WHERE user_id = #{userId} ORDER BY gmt_create DESC LIMIT #{limit}")
    List<OperationLog> findByUserId(Long userId, int limit);

    @Select("SELECT * FROM operation_logs WHERE action = #{action} ORDER BY gmt_create DESC LIMIT #{limit}")
    List<OperationLog> findByAction(String action, int limit);

    @Select("SELECT * FROM operation_logs WHERE gmt_create >= #{startTime} AND gmt_create <= #{endTime} ORDER BY gmt_create DESC")
    List<OperationLog> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    @Delete("DELETE FROM operation_logs WHERE gmt_create < #{expireTime}")
    int deleteExpiredLogs(LocalDateTime expireTime);
}

