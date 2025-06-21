package com.storm.userservice.config;

import com.storm.userservice.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    @Lazy
    private AuthInterceptor authInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/user/**")  // 拦截所有用户相关接口
                .excludePathPatterns(
                        "/user/register",     // 排除注册接口
                        "/user/login",        // 排除登录接口
                        "/actuator/**",       // 排除健康检查接口
                        "/error"              // 排除错误页面
                );
    }
}