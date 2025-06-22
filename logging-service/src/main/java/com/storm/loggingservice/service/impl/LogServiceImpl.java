package com.storm.loggingservice.service.impl;

import com.storm.common.dto.LogEventDTO;
import com.storm.common.dto.Result;
import com.storm.loggingservice.entity.OperationLog;
import com.storm.loggingservice.mapper.OperationLogMapper;
import com.storm.loggingservice.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// 日志服务实现类
@Slf4j
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public void saveLog(LogEventDTO logEvent) {
        try {
            OperationLog operationLog = OperationLog.builder()
                    .userId(logEvent.getUserId())
                    .action(logEvent.getAction())
                    .ip(logEvent.getIp())
                    .detail(logEvent.getDetail())
                    .gmtCreate(LocalDateTime.now())
                    .build();

            operationLogMapper.insert(operationLog);
            log.info("保存操作日志成功: userId={}, action={}", logEvent.getUserId(), logEvent.getAction());
        } catch (Exception e) {
            log.error("保存操作日志失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public Result<List<?>> getUserLogs(Long userId, int page, int size) {
        try {
            int offset = (page - 1) * size;
            List<OperationLog> logs = operationLogMapper.selectByUserIdWithPage(userId, offset, size);
            return Result.success(logs);
        } catch (Exception e) {
            log.error("查询用户日志失败: {}", e.getMessage(), e);
            return Result.error("查询用户日志失败");
        }
    }

    @Override
    public Result<List<?>> getAllLogs(int page, int size) {
        try {
            int offset = (page - 1) * size;
            List<OperationLog> logs = operationLogMapper.selectAllWithPage(offset, size);
            return Result.success(logs);
        } catch (Exception e) {
            log.error("查询所有日志失败: {}", e.getMessage(), e);
            return Result.error("查询所有日志失败");
        }
    }
}

