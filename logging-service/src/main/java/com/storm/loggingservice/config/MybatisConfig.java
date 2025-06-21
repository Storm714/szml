package com.storm.loggingservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.storm.loggingservice.mapper")
public class MybatisConfig {
}
