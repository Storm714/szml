package com.storm.permissionservice.exception;

import com.storm.common.dto.Result;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

// 权限服务全局异常处理器
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理业务异常
    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handlePermissionException(PermissionException e, HttpServletRequest request) {
        log.warn("权限业务异常: URI={}, message={}", request.getRequestURI(), e.getMessage());
        return Result.error(e.getMessage());
    }

    // 处理参数验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("参数验证异常: URI={}", request.getRequestURI());

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errorMsg = new StringBuilder("参数验证失败: ");

        return getVoidResult(fieldErrors, errorMsg);
    }

    // 处理绑定异常
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("参数绑定异常: URI={}", request.getRequestURI());

        List<FieldError> fieldErrors = e.getFieldErrors();
        StringBuilder errorMsg = new StringBuilder("参数绑定失败: ");

        return getVoidResult(fieldErrors, errorMsg);
    }

    @NotNull
    private Result<Void> getVoidResult(List<FieldError> fieldErrors, StringBuilder errorMsg) {
        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError error = fieldErrors.get(i);
            errorMsg.append(error.getField()).append(" ").append(error.getDefaultMessage());
            if (i < fieldErrors.size() - 1) {
                errorMsg.append(", ");
            }
        }

        return Result.error(errorMsg.toString());
    }

    // 处理IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("非法参数异常: URI={}, message={}", request.getRequestURI(), e.getMessage());
        return Result.error("参数错误: " + e.getMessage());
    }

    // 处理运行时异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常: URI={}, message={}", request.getRequestURI(), e.getMessage(), e);
        return Result.error("系统异常: " + e.getMessage());
    }

    // 处理所有其他异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleGenericException(Exception e, HttpServletRequest request) {
        log.error("系统异常: URI={}, message={}", request.getRequestURI(), e.getMessage(), e);
        return Result.error("系统内部错误，请联系管理员");
    }
}