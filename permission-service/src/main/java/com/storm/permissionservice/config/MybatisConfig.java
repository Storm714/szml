package com.storm.permissionservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

// MyBatis 配置类
@Configuration
@MapperScan("com.storm.permissionservice.mapper")
public class MybatisConfig {
}
