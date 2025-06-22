package com.storm.userservice.controller;

import com.storm.common.dto.Result;
import com.storm.userservice.annotation.RequireRole;
import com.storm.userservice.entity.dto.UserLoginDTO;
import com.storm.userservice.entity.dto.UserRegisterDTO;
import com.storm.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户注册
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO dto, HttpServletRequest request) {
        return userService.register(dto, request);
    }

    // 用户登录
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserLoginDTO dto, HttpServletRequest request) {
        return userService.login(dto, request);
    }

    // 获取用户列表
    @GetMapping("/users")
    @RequireRole({"ADMIN", "SUPER_ADMIN"})
    public Result<?> getUsers(@RequestParam int page,
                                  @RequestParam int size,
                              HttpServletRequest request) {
        return userService.getUsers(page, size, request);
    }

    // 根据ID获取用户信息
    @GetMapping("/{userId:\\d+}")
    @RequireRole({"USER", "ADMIN", "SUPER_ADMIN"})
    public Result<?> getUserById(@PathVariable Long userId, HttpServletRequest request) {
        return userService.getUserById(userId, request);
    }

    // 修改用户信息
    @PutMapping("/{userId:\\d+}")
    @RequireRole({"USER", "ADMIN", "SUPER_ADMIN"})
    public Result<Void> updateUser(@PathVariable     Long userId,
                                   @RequestBody UserRegisterDTO dto,
                                   HttpServletRequest request) {
        return userService.updateUser(userId, dto, request);
    }

    // 重置密码
    @PostMapping("/reset-password")
    @RequireRole({"USER", "ADMIN", "SUPER_ADMIN"})
    public Result<Void> resetPassword(@RequestParam Long userId,
                                      @RequestParam String newPassword,
                                      HttpServletRequest request) {
        return userService.resetPassword(userId, newPassword, request);
    }
}
