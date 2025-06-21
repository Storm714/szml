package com.storm.permissionservice.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storm.permissionservice.dto.OperationLogMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 操作日志消息生产者
@Slf4j
@Component
public class OperationLogProducer {

    private static final String TOPIC = "operation-log-topic";

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 发送操作日志消息
    public void sendOperationLog(OperationLogMessage logMessage) {
        try {
            String messageBody = objectMapper.writeValueAsString(logMessage);
            rocketMQTemplate.convertAndSend(TOPIC, messageBody);
            log.debug("发送操作日志消息成功: action={}, userId={}", logMessage.getAction(), logMessage.getUserId());

        } catch (Exception e) {
            log.error("发送操作日志消息失败: action={}, userId={}, error={}",
                    logMessage.getAction(), logMessage.getUserId(), e.getMessage(), e);
        }
    }

    // 发送操作日志消息（带标签）
    public void sendOperationLogWithTag(OperationLogMessage logMessage, String tag) {
        try {
            String messageBody = objectMapper.writeValueAsString(logMessage);
            rocketMQTemplate.convertAndSend(TOPIC + ":" + tag, messageBody);
            log.debug("发送操作日志消息成功(带标签): action={}, userId={}, tag={}",
                    logMessage.getAction(), logMessage.getUserId(), tag);

        } catch (Exception e) {
            log.error("发送操作日志消息失败(带标签): action={}, userId={}, tag={}, error={}",
                    logMessage.getAction(), logMessage.getUserId(), tag, e.getMessage(), e);
        }
    }
}

