package com.storm.permissionservice.util;

import com.storm.permissionservice.dto.OperationLogMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

// 操作日志工具类
public class OperationLogUtil {

    // 构建操作日志消息
    public static OperationLogMessage buildLogMessage(
            Long userId,
            String username,
            String action,
            String serviceName,
            HttpServletRequest request,
            Object requestParams,
            Object responseResult,
            Long executionTime,
            String status,
            String errorMsg) {

        return OperationLogMessage.builder()
                .userId(userId)
                .username(username)
                .action(action)
                .serviceName(serviceName)
                .method(request.getMethod())
                .requestUrl(request.getRequestURL().toString())
                .ipAddress(getClientIpAddress(request))
                .userAgent(request.getHeader("User-Agent"))
                .requestParams(requestParams != null ? requestParams.toString() : null)
                .responseResult(responseResult != null ? responseResult.toString() : null)
                .executionTime(executionTime)
                .status(status)
                .errorMsg(errorMsg)
                .operationTime(LocalDateTime.now())
                .build();
    }

    /**
     * 构建简单的操作日志消息
     */
    public static OperationLogMessage buildSimpleLogMessage(
            Long userId,
            String username,
            String action,
            String serviceName,
            String status) {

        return OperationLogMessage.builder()
                .userId(userId)
                .username(username)
                .action(action)
                .serviceName(serviceName)
                .status(status)
                .operationTime(LocalDateTime.now())
                .build();
    }

    /**
     * 获取客户端真实IP地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor) && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xRealIP) && !"unknown".equalsIgnoreCase(xRealIP)) {
            return xRealIP;
        }

        String proxyClientIP = request.getHeader("Proxy-Client-IP");
        if (StringUtils.hasText(proxyClientIP) && !"unknown".equalsIgnoreCase(proxyClientIP)) {
            return proxyClientIP;
        }

        String wlProxyClientIP = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.hasText(wlProxyClientIP) && !"unknown".equalsIgnoreCase(wlProxyClientIP)) {
            return wlProxyClientIP;
        }

        return request.getRemoteAddr();
    }

    /**
     * 操作状态常量
     */
    public static class Status {
        public static final String SUCCESS = "SUCCESS";
        public static final String FAILED = "FAILED";
    }

    /**
     * 常用操作类型
     */
    public static class Action {
        public static final String USER_REGISTER = "USER_REGISTER";
        public static final String USER_LOGIN = "USER_LOGIN";
        public static final String USER_LOGOUT = "USER_LOGOUT";
        public static final String USER_UPDATE = "USER_UPDATE";
        public static final String PASSWORD_RESET = "PASSWORD_RESET";
        public static final String ROLE_BIND = "ROLE_BIND";
        public static final String ROLE_UPGRADE = "ROLE_UPGRADE";
        public static final String ROLE_DOWNGRADE = "ROLE_DOWNGRADE";
    }
}
