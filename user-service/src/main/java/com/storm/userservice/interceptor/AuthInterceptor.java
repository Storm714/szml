package com.storm.userservice.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storm.common.dto.Result;
import com.storm.userservice.annotation.RequireRole;
import com.storm.userservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.lang.reflect.Method;

// 权限拦截器
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    @Lazy
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private ObjectMapper objectMapper;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("拦截器被触发，请求路径: {}", request.getRequestURI());

        // 只拦截Controller方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        log.info("处理方法: {}.{}", method.getDeclaringClass().getSimpleName(), method.getName());

        // 检查是否需要权限验证
        RequireRole requireRole = method.getAnnotation(RequireRole.class);
        if (requireRole == null) {
            requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        }

        if (requireRole == null) {
            log.info("方法无需权限验证，直接通过");
            return true;
        }

        log.info("开始权限验证，需要角色: {}", String.join(",", requireRole.value()));

        try {
            // 获取JWT Token
            String token = getTokenFromRequest(request);
            if (!StringUtils.hasText(token)) {
                return handleAuthFailure(response, "缺少认证Token");
            }

            // 验证Token并获取用户信息
            if (!jwtUtil.validateToken(token)) {
                return handleAuthFailure(response, "Token无效或已过期");
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);

            // 直接从JWT中获取角色信息，避免远程调用和循环依赖
            String userRole = jwtUtil.getRoleFromToken(token);
            if (!StringUtils.hasText(userRole)) {
                return handleAuthFailure(response, "Token中缺少角色信息");
            }

            log.info("用户信息 - ID: {}, 用户名: {}, 角色: {}", userId, username, userRole);

            // 检查权限
            if (!hasPermission(userRole, requireRole.value(), userId, request)) {
                log.warn("权限不足 - 用户角色: {}, 需要角色: {}", userRole, String.join(",", requireRole.value()));
                return handleAuthFailure(response, "权限不足");
            }

            // 将用户信息放入请求属性中
            request.setAttribute("currentUserId", userId);
            request.setAttribute("currentUsername", username);
            request.setAttribute("currentUserRole", userRole);

            log.info("权限验证通过");
            return true;

        } catch (Exception e) {
            log.error("权限校验异常", e);
            return handleAuthFailure(response, "权限校验异常");
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    private boolean hasPermission(String userRole, String[] requiredRoles, Long userId, HttpServletRequest request) {
        if (requiredRoles == null || requiredRoles.length == 0) {
            return true;
        }

        for (String requiredRole : requiredRoles) {
            if (requiredRole.equals(userRole)) {
                return true;
            }
            // 超管有所有权限
            if ("SUPER_ADMIN".equals(userRole)) {
                return true;
            }
            // 管理员有普通用户权限
            if ("ADMIN".equals(userRole) && "USER".equals(requiredRole)) {
                return true;
            }
        }

        // 检查是否可以访问自己的数据
        String pathUserId = extractUserIdFromPath(request);
        if (pathUserId != null && pathUserId.equals(userId.toString())) {
            return true;
        }

        return false;
    }

    private String extractUserIdFromPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 匹配 /user/{userId} 格式的路径
        if (path.matches(".*/user/\\d+.*")) {
            String[] parts = path.split("/");
            for (int i = 0; i < parts.length - 1; i++) {
                if ("user".equals(parts[i]) && i + 1 < parts.length) {
                    return parts[i + 1];
                }
            }
        }
        return null;
    }

    private boolean handleAuthFailure(HttpServletResponse response, String message) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        try {
            Result<Object> result = Result.error(401, message);
            String json = objectMapper.writeValueAsString(result);
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error("写入响应失败", e);
        }

        return false;
    }
}