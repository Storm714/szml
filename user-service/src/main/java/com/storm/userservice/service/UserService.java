package com.storm.userservice.service;

import com.storm.common.dto.Result;
import com.storm.userservice.entity.dto.UserLoginDTO;
import com.storm.userservice.entity.dto.UserRegisterDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    // 用户注册
    Result<Void> register(UserRegisterDTO dto, HttpServletRequest request);

    // 用户登录
    Result<String> login(UserLoginDTO dto, HttpServletRequest request);

    // 获取用户列表
    Result<?> getUsers(int page, int size, HttpServletRequest request);

    // 根据ID获取用户信息
    Result<?> getUserById(Long userId, HttpServletRequest request);

    // 修改用户信息
    Result<Void> updateUser(Long userId, UserRegisterDTO dto, HttpServletRequest request);

    // 重置密码
    Result<Void> resetPassword(Long userId, String newPassword, HttpServletRequest request);
}
