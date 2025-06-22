package com.storm.loggingservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("operation_logs")
public class OperationLog {

    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    @TableField("user_id")
    private Long userId;

    @TableField("ip")
    private String ip;

    @TableField("action")
    private String action;

    @TableField("detail")
    private String detail;

    @TableField("gmt_create")
    private LocalDateTime gmtCreate;
}