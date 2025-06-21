package com.storm.permissionservice.controller;

import com.storm.common.dto.Result;
import com.storm.permissionservice.entity.OperationLog;
import com.storm.permissionservice.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// 操作日志控制器
@RestController
@RequestMapping("/logs")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    // 根据用户ID查询操作日志
    @GetMapping("/user/{userId}")
    public Result<List<OperationLog>> getLogsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "100") int limit) {
        return operationLogService.getLogsByUserId(userId, limit);
    }

    // 根据操作类型查询日志
    @GetMapping("/action/{action}")
    public Result<List<OperationLog>> getLogsByAction(
            @PathVariable String action,
            @RequestParam(defaultValue = "100") int limit) {
        return operationLogService.getLogsByAction(action, limit);
    }

    // 根据时间范围查询日志
    @GetMapping("/time-range")
    public Result<List<OperationLog>> getLogsByTimeRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return operationLogService.getLogsByTimeRange(startTime, endTime);
    }

    // 清理过期日志
    @DeleteMapping("/clean")
    public Result<Integer> cleanExpiredLogs(
            @RequestParam(defaultValue = "30") int retentionDays) {
        return operationLogService.cleanExpiredLogs(retentionDays);
    }
}

