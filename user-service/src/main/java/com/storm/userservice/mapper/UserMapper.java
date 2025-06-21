package com.storm.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storm.userservice.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 根据用户名查询用户 - 使用逻辑表名 users
    @Select("SELECT * FROM users WHERE username = #{username} AND deleted = 0")
    User selectByUsername(@Param("username") String username);

    @Select("SELECT * FROM users WHERE user_id = #{userId} AND deleted = 0")
    User selectByUserId(@Param("userId") Long userId);

    // 分页查询用户
    @Select("SELECT * FROM users WHERE deleted = 0 ORDER BY created_at DESC LIMIT #{size} OFFSET #{offset}")
    List<User> selectUsersWithPage(@Param("offset") int offset, @Param("size") int size);

    // 检查用户名是否存在
    @Select("SELECT COUNT(1) > 0 FROM users WHERE username = #{username} AND deleted = 0")
    boolean existsByUsername(@Param("username") String username);

    // 检查邮箱是否存在
    @Select("SELECT COUNT(1) > 0 FROM users WHERE email = #{email} AND deleted = 0")
    boolean existsByEmail(@Param("email") String email);

    // 检查手机号是否存在
    @Select("SELECT COUNT(1) > 0 FROM users WHERE phone = #{phone} AND deleted = 0")
    boolean existsByPhone(@Param("phone") String phone);

    @Insert("INSERT INTO users(user_id, username, password, email, phone, created_at, updated_at, deleted) " +
            "VALUES(#{userId}, #{username}, #{password}, #{email}, #{phone}, #{createdAt}, #{updatedAt}, #{deleted})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    int insertUser(User user);

    @Update("UPDATE users SET username = #{username}, email = #{email}, phone = #{phone}, " +
            "updated_at = NOW() WHERE user_id = #{userId} AND deleted = 0")
    int updateUser(User user);

    // 更新用户密码
    @Update("UPDATE users SET password = #{password}, updated_at = NOW() WHERE user_id = #{userId} AND deleted = 0")
    int updatePassword(@Param("userId") Long userId, @Param("password") String password);
}