package com.storm.loggingservice.dto;

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

    private String ip;

    private LocalDateTime gmt_create;

    private String detail;

    private String action;
}
