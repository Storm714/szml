package com.storm.loggingservice.service.impl;

import com.storm.common.dto.Result;
import com.storm.loggingservice.dto.OperationLogMessage;
import com.storm.loggingservice.entity.OperationLog;
import com.storm.loggingservice.mapper.OperationLogMapper;
import com.storm.loggingservice.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// 操作日志服务实现类
@Slf4j
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOperationLog(OperationLogMessage logMessage) {
        log.debug("保存操作日志: action={}, userId={}", logMessage.getAction(), logMessage.getUserId());

        try {
            OperationLog operationLog = OperationLog.builder()
                    .logId(null)
                    .userId(logMessage.getUserId())
                    .ip(logMessage.getIp())
                    .detail(logMessage.getDetail())
                    .action(logMessage.getAction())
                    .build();

            operationLogMapper.insert(operationLog);
            log.debug("操作日志保存成功: logId={}", operationLog.getLogId());

            Result.success();

        } catch (Exception e) {
            log.error("保存操作日志失败: action={}, userId={}, error={}",
                    logMessage.getAction(), logMessage.getUserId(), e.getMessage(), e);
            Result.error("保存操作日志失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Result<List<OperationLog>> getLogsByUserId(Long userId, int limit) {
        log.info("查询用户操作日志: userId={}, limit={}", userId, limit);

        try {
            List<OperationLog> logs = operationLogMapper.findByUserId(userId, limit);
            return Result.success(logs);

        } catch (Exception e) {
            log.error("查询用户操作日志失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("查询用户操作日志失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Result<List<OperationLog>> getLogsByAction(String action, int limit) {
        log.info("根据操作类型查询日志: action={}, limit={}", action, limit);

        try {
            List<OperationLog> logs = operationLogMapper.findByAction(action, limit);
            return Result.success(logs);

        } catch (Exception e) {
            log.error("根据操作类型查询日志失败: action={}, error={}", action, e.getMessage(), e);
            return Result.error("查询操作日志失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Result<List<OperationLog>> getLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("根据时间范围查询日志: startTime={}, endTime={}", startTime, endTime);

        try {
            List<OperationLog> logs = operationLogMapper.findByTimeRange(startTime, endTime);
            return Result.success(logs);

        } catch (Exception e) {
            log.error("根据时间范围查询日志失败: startTime={}, endTime={}, error={}",
                    startTime, endTime, e.getMessage(), e);
            return Result.error("查询操作日志失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> cleanExpiredLogs(int retentionDays) {
        log.info("清理过期日志: retentionDays={}", retentionDays);

        try {
            LocalDateTime expireTime = LocalDateTime.now().minusDays(retentionDays);
            int deletedCount = operationLogMapper.deleteExpiredLogs(expireTime);

            log.info("清理过期日志完成: deletedCount={}", deletedCount);
            return Result.success(deletedCount);

        } catch (Exception e) {
            log.error("清理过期日志失败: retentionDays={}, error={}", retentionDays, e.getMessage(), e);
            return Result.error("清理过期日志失败：" + e.getMessage());
        }
    }
}
