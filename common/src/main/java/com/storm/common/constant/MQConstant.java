package com.storm.common.constant;

// MQ常量
public class MQConstant {

    // 日志相关
    public static final String LOG_TOPIC = "operation-log-topic";
    public static final String LOG_TAG = "OPERATION_LOG";
    public static final String LOG_CONSUMER_GROUP = "logging-service-group";

    // 用户相关
    public static final String USER_TOPIC = "user-topic";
    public static final String USER_REGISTER_TAG = "USER_REGISTER";
    public static final String USER_UPDATE_TAG = "USER_UPDATE";
    public static final String USER_DELETE_TAG = "USER_DELETE";

    // 权限相关
    public static final String PERMISSION_TOPIC = "permission-topic";
    public static final String ROLE_CHANGE_TAG = "ROLE_CHANGE";

    // 系统事件相关
    public static final String SYSTEM_TOPIC = "system-topic";
    public static final String SYSTEM_ALERT_TAG = "SYSTEM_ALERT";
}
