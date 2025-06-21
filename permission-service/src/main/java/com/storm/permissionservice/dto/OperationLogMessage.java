package com.storm.permissionservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLogMessage {

    private Long userId;

    private String username;

    // 操作类型
    private String action;

    // 服务名称
    private String serviceName;

    // 请求方法
    private String method;

    // 请求URL
    private String requestUrl;

    // IP地址
    private String ipAddress;

    // 用户代理
    private String userAgent;

    // 请求参数
    private String requestParams;

    // 响应结果
    private String responseResult;

    // 执行时间（毫秒）
    private Long executionTime;

    // 状态
    private String status;

    // 错误信息
    private String errorMsg;

    // 操作时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;
}
