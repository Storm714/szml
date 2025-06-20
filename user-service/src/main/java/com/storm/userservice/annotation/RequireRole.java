package com.storm.userservice.annotation;

import java.lang.annotation.*;

// 权限要求注解
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {

    String[] value() default {};

    boolean allowSelf() default false;
}
