package com.storm.userservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.storm.userservice.mapper")
public class MybatisConfig {
}

