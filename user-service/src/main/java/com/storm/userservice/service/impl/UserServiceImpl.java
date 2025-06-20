package com.storm.userservice.service.impl;

import com.storm.common.dto.Result;
import com.storm.userservice.entity.User;
import com.storm.userservice.entity.dto.UserLoginDTO;
import com.storm.userservice.entity.dto.UserRegisterDTO;
import com.storm.userservice.feign.PermissionServiceFeign;
import com.storm.userservice.mapper.UserMapper;
import com.storm.userservice.mq.LogMessageProducer;
import com.storm.userservice.service.UserService;
import com.storm.userservice.util.JwtUtil;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionServiceFeign permissionServiceFeign;

    @Autowired
    private LogMessageProducer logMessageProducer;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public Result<Void> register(UserRegisterDTO dto) {
        log.info("用户注册请求: username={}", dto.getUsername());

        try {
            // 检查用户名是否已存在
            User existingUser = userMapper.selectByUsername(dto.getUsername());
            if (existingUser != null) {
                return Result.error("用户名已存在");
            }

            // 创建新用户
            User user = User.builder()
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .phone(dto.getPhone())
                    .build();

            // 保存用户到数据库（分库分表）
            userMapper.insert(user);
            log.info("用户创建成功: userId={}, username={}", user.getId(), dto.getUsername());

            // RPC调用权限服务绑定默认角色
            Result<Void> roleBindResult = permissionServiceFeign.bindDefaultRole(user.getId());
            if (!roleBindResult.isSuccess()) {
                log.error("角色绑定失败: userId={}, error={}", user.getId(), roleBindResult.getMessage());
                throw new RuntimeException("角色绑定失败: " + roleBindResult.getMessage());
            }
            log.info("角色绑定成功: userId={}", user.getId());

            // 发送注册日志消息到MQ
            logMessageProducer.sendUserRegisterLog(user.getId(), dto.getUsername());

            log.info("用户注册成功: userId={}, username={}", user.getId(), dto.getUsername());
            return Result.success();

        } catch (Exception e) {
            log.error("用户注册失败: username={}, error={}", dto.getUsername(), e.getMessage(), e);
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> login(UserLoginDTO dto) {
        log.info("用户登录请求: username={}", dto.getUsername());

        try {
            // 查找用户
            User user = userMapper.selectByUsername(dto.getUsername());
            if (user == null) {
                return Result.error("用户名或密码错误");
            }

            // 验证密码
            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                return Result.error("用户名或密码错误");
            }

            // 获取用户角色
            String roleCode = "USER"; // 默认角色
            try {
                roleCode = permissionServiceFeign.getUserRoleCode(user.getId());
                Result<String> roleResult = Result.success(roleCode);
                if (roleResult.isSuccess() && StringUtils.hasText(roleResult.getData())) {
                    roleCode = roleResult.getData();
                }
            } catch (Exception e) {
                log.warn("获取用户角色失败，使用默认角色: userId={}, error={}", user.getId(), e.getMessage());
            }

            // 生成JWT Token
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), roleCode);

            // 发送登录日志消息
            logMessageProducer.sendUserLoginLog(user.getId(), user.getUsername());

            log.info("用户登录成功: username={}, roleCode={}", dto.getUsername(), roleCode);
            return Result.success("登录成功", token);

        } catch (Exception e) {
            log.error("用户登录失败: username={}, error={}", dto.getUsername(), e.getMessage(), e);
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Result<?> getUsers(int page, int size, HttpServletRequest request) {
        log.info("获取用户列表请求: page={}, size={}", page, size);

        try {
            // 权限检查已在拦截器中完成，这里直接获取当前用户角色
            String currentUserRole = (String) request.getAttribute("currentUserRole");
            Long currentUserId = (Long) request.getAttribute("currentUserId");

            int offset = (page - 1) * size;
            List<User> users;

            // 根据角色返回不同的数据
            if ("SUPER_ADMIN".equals(currentUserRole) || "ADMIN".equals(currentUserRole)) {
                // 超管和管理员可以看到所有用户
                users = userMapper.selectUsersWithPage(offset, size);
            } else {
                // 普通用户只能看到自己
                User user = userMapper.selectByUserId(currentUserId);
                users = user != null ? List.of(user) : List.of();
            }

            // 清除密码等敏感信息
            users.forEach(user -> user.setPassword(null));

            Map<String, Object> result = new HashMap<>();
            result.put("users", users);
            result.put("page", page);
            result.put("size", size);

            return Result.success(result);

        } catch (Exception e) {
            log.error("获取用户列表失败: error={}", e.getMessage(), e);
            return Result.error("获取用户列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Result<?> getUserById(Long userId, HttpServletRequest request) {
        log.info("获取用户信息请求: userId={}", userId);

        try {
            // 权限检查已在拦截器中完成
            Long currentUserId = (Long) request.getAttribute("currentUserId");
            String currentUserRole = (String) request.getAttribute("currentUserRole");

            // 检查是否有权限访问指定用户信息
            if (!"SUPER_ADMIN".equals(currentUserRole) &&
                    !"ADMIN".equals(currentUserRole) &&
                    !userId.equals(currentUserId)) {
                return Result.error(403, "无权限访问该用户信息");
            }

            User user = userMapper.selectByUserId(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 清除敏感信息
            user.setPassword(null);

            return Result.success(user);

        } catch (Exception e) {
            log.error("获取用户信息失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> updateUser(Long userId, UserRegisterDTO dto, HttpServletRequest request) {
        log.info("修改用户信息请求: userId={}, username={}", userId, dto.getUsername());

        try {
            // 权限检查已在拦截器中完成
            Long currentUserId = (Long) request.getAttribute("currentUserId");
            String currentUserRole = (String) request.getAttribute("currentUserRole");

            // 检查是否有权限修改指定用户信息
            if (!"SUPER_ADMIN".equals(currentUserRole) &&
                    !"ADMIN".equals(currentUserRole) &&
                    !userId.equals(currentUserId)) {
                return Result.error(403, "无权限修改该用户信息");
            }

            User user = userMapper.selectByUserId(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 检查用户名是否被其他用户使用
            if (!user.getUsername().equals(dto.getUsername())) {
                User existingUser = userMapper.selectByUsername(dto.getUsername());
                if (existingUser != null) {
                    return Result.error("用户名已存在");
                }
            }

            // 更新用户信息
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());

            // 如果提供了新密码，则更新密码
            if (StringUtils.hasText(dto.getPassword())) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            userMapper.updateUser(user);
            log.info("用户信息修改成功: userId={}", userId);

            return Result.success();

        } catch (Exception e) {
            log.error("修改用户信息失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("修改失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> resetPassword(Long userId, String newPassword, HttpServletRequest request) {
        log.info("重置密码请求: userId={}", userId);

        try {
            // 权限检查已在拦截器中完成
            Long currentUserId = (Long) request.getAttribute("currentUserId");
            String currentUserRole = (String) request.getAttribute("currentUserRole");

            // 检查是否有权限重置指定用户密码
            if (!"SUPER_ADMIN".equals(currentUserRole) &&
                    !"ADMIN".equals(currentUserRole) &&
                    !userId.equals(currentUserId)) {
                return Result.error(403, "无权限重置该用户密码");
            }

            User user = userMapper.selectByUserId(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            String encodedPassword = passwordEncoder.encode(newPassword);
            userMapper.updatePassword(userId, encodedPassword);

            log.info("密码重置成功: userId={}", userId);
            return Result.success();

        } catch (Exception e) {
            log.error("重置密码失败: userId={}, error={}", userId, e.getMessage(), e);
            return Result.error("重置密码失败：" + e.getMessage());
        }
    }
}
