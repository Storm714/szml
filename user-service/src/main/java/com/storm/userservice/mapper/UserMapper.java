package com.storm.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storm.userservice.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 根据用户名查询用户
    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);

    // 根据用户ID查询用户
    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    User selectByUserId(@Param("userId") Long userId);

    // 分页查询用户
    @Select("SELECT * FROM users LIMIT #{size} OFFSET #{offset}")
    List<User> selectUsersWithPage(@Param("offset") int offset, @Param("size") int size);

    // 检查用户ID是否存在
    @Select("SELECT COUNT(1) > 0 FROM users WHERE user_id = #{userId}")
    boolean existsByUserId(@Param("userId") Long userId);

    // 检查用户名是否存在
    @Select("SELECT COUNT(1) > 0 FROM users WHERE username = #{username}")
    boolean existsByUsername(@Param("username") String username);

    // 检查邮箱是否存在
    @Select("SELECT COUNT(1) > 0 FROM users WHERE email = #{email}")
    boolean existsByEmail(@Param("email") String email);

    // 检查手机号是否存在
    @Select("SELECT COUNT(1) > 0 FROM users WHERE phone = #{phone}")
    boolean existsByPhone(@Param("phone") String phone);

    @Insert("INSERT INTO users(user_id, username, password, email, phone) " +
            "VALUES(#{userId}, #{username}, #{password}, #{email}, #{phone})")
    void insertUser(User user);

    @Update("UPDATE users SET username = #{username}, email = #{email}, phone = #{phone} WHERE user_id = #{userId}")
    void updateUser(User user);

    // 更新用户密码
    @Update("UPDATE users SET password = #{password} WHERE user_id = #{userId}")
    void updatePassword(@Param("userId") Long userId, @Param("password") String password);

    @Select("SELECT user_id FROM users WHERE username = #{username}")
    Long selectUserIdByUsername(@Param("username") String username);
}