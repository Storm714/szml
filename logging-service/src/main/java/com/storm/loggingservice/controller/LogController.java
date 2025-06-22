package com.storm.loggingservice.controller;

import com.storm.common.dto.LogEventDTO;
import com.storm.common.dto.Result;
import com.storm.loggingservice.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// 日志控制器
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    // 保存操作日志
    @PostMapping("/save")
    public void saveLog(@RequestBody LogEventDTO logEvent) {
        logService.saveLog(logEvent);
    }

    // 查询用户操作日志
    @GetMapping("/user/{userId}")
    public Result<?> getUserLogs(@PathVariable Long userId,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        return logService.getUserLogs(userId, page, size);
    }

    // 查询所有操作日志
    @GetMapping("/all")
    public Result<?> getAllLogs(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size) {
        return logService.getAllLogs(page, size);
    }
}
