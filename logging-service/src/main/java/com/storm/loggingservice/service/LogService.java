package com.storm.loggingservice.service;

import com.storm.common.dto.LogEventDTO;
import com.storm.common.dto.Result;

import java.util.List;

// 日志服务接口
public interface LogService {

    // 保存操作日志
    void saveLog(LogEventDTO logEvent);

    // 查询用户操作日志
    Result<List<?>> getUserLogs(Long userId, int page, int size);

    // 查询所有操作日志
    Result<List<?>> getAllLogs(int page, int size);
}
