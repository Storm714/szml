package com.storm.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 日志事件DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEventDTO {

    private Long userId;

    private String action;

    private String ip;

    private String detail;

    private LocalDateTime timestamp;
}
