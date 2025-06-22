package com.storm.userservice.mq;

import com.storm.common.constant.MQConstant;
import com.storm.common.dto.LogEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 日志消息生产者
@Slf4j
@Component
public class LogMessageProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 发送日志消息
    public void sendLogMessage(Long userId, String action, String detail, String ip) {
        try {
            LogEventDTO logEvent = LogEventDTO.builder()
                    .userId(userId)
                    .ip(ip)
                    .action(action)
                    .detail(detail)
                    .build();

            rocketMQTemplate.convertAndSend(MQConstant.LOG_TOPIC, logEvent);

            log.info("日志消息发送成功: userId={}, action={}", userId, action);

        } catch (Exception e) {
            log.error("发送日志消息失败: userId={}, action={}, error={}", userId, action, e.getMessage(), e);
        }
    }

    // 发送用户注册日志
    public void sendUserRegisterLog(Long userId, String username, String ip) {
        sendLogMessage(userId, "USER_REGISTER", "用户注册: " + username, ip);
    }

    // 发送用户登录日志
    public void sendUserLoginLog(Long userId, String username, String ip) {
        sendLogMessage(userId, "USER_LOGIN", "用户登录: " + username, ip);
    }

    // 发送用户更新日志
    public void sendUserUpdateLog(Long userId, String detail, String ip) {
        sendLogMessage(userId, "USER_UPDATE", detail, ip);
    }

    // 发送密码重置日志
    public void sendPasswordResetLog(Long userId, String ip) {
        sendLogMessage(userId, "PASSWORD_RESET", "密码重置", ip);
    }
}
