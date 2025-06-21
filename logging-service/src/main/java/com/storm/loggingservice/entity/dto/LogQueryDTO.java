package com.storm.loggingservice.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

// 日志查询请求DTO
@Data
public class LogQueryDTO {

    private Long userId;

    private String action;

    private String ip;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer page = 1;

    private Integer size = 10;
}
