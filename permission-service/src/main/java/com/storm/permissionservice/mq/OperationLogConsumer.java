package com.storm.permissionservice.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storm.permissionservice.dto.OperationLogMessage;
import com.storm.permissionservice.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 操作日志消息消费者
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "operation-log-topic",
        consumerGroup = "permission-service-log-consumer",
        maxReconsumeTimes = 3
)
public class OperationLogConsumer implements RocketMQListener<String> {

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(String message) {
        log.info("接收到操作日志消息: {}", message);

        try {
            // 解析消息
            OperationLogMessage logMessage = objectMapper.readValue(message, OperationLogMessage.class);
            log.debug("解析日志消息成功: action={}, userId={}", logMessage.getAction(), logMessage.getUserId());

            // 保存日志
            operationLogService.saveOperationLog(logMessage);
            log.info("操作日志消费处理成功: action={}, userId={}", logMessage.getAction(), logMessage.getUserId());

        } catch (Exception e) {
            log.error("操作日志消息处理失败: message={}, error={}", message, e.getMessage(), e);
            // 这里可以选择将消息发送到死信队列或进行其他处理
            throw new RuntimeException("操作日志消息处理失败", e);
        }
    }
}
