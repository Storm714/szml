package com.storm.permissionservice.exception;

import lombok.Getter;

// 权限服务业务异常类
@Getter
public class PermissionException extends RuntimeException {

    private final String code;

    public PermissionException(String message) {
        super(message);
        this.code = "PERMISSION_ERROR";
    }

    public PermissionException(String code, String message) {
        super(message);
        this.code = code;
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
        this.code = "PERMISSION_ERROR";
    }

    public PermissionException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
