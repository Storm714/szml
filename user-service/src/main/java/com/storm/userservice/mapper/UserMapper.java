package com.storm.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storm.userservice.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 根据用户名查询用户
    User selectByUsername(@Param("username") String username);

    //根据用户ID查询用户
    User selectByUserId(@Param("userId") Long userId);

    // 分页查询用户
    List<User> selectUsersWithPage(@Param("offset") int offset, @Param("size") int size);

    // 检查用户名是否存在
    boolean existsByUsername(@Param("username") String username);

    // 检查邮箱是否存在
    boolean existsByEmail(@Param("email") String email);

    // 检查手机号是否存在
    boolean existsByPhone(@Param("phone") String phone);

    // 插入用户
    int insertUser(User user);

    // 更新用户信息
    int updateUser(User user);

    // 更新用户密码
    int updatePassword(@Param("userId") Long userId, @Param("password") String password);
}
