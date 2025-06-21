package com.storm.permissionservice.service;

import com.storm.common.dto.Result;
import com.storm.permissionservice.dto.OperationLogMessage;
import com.storm.permissionservice.entity.OperationLog;

import java.time.LocalDateTime;
import java.util.List;

public interface OperationLogService {

    // 保存操作日志
    Result<Void> saveOperationLog(OperationLogMessage logMessage);

    // 根据用户ID查询操作日志
    Result<List<OperationLog>> getLogsByUserId(Long userId, int limit);

    // 根据操作类型查询日志
    Result<List<OperationLog>> getLogsByAction(String action, int limit);

    // 根据时间范围查询日志
    Result<List<OperationLog>> getLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    // 清理过期日志
    Result<Integer> cleanExpiredLogs(int retentionDays);
}

