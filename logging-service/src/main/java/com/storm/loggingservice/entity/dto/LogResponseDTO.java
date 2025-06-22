package com.storm.loggingservice.entity.dto;

import lombok.Data;

// 日志响应DTO
@Data
public class LogResponseDTO {

    private Long logId;

    private Long userId;

    private String username;

    private String ip;

    private String action;

    private String actionDesc;

    private String detail;
}
