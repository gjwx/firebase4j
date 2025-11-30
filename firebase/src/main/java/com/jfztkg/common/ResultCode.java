package com.jfztkg.common;

/**
 * @Author gongjiawei
 * @Date 2025/11/28 23:20
 * @注释
 */
/**
 * 统一状态码枚举
 */
public enum ResultCode {

    // 基础通用
    SUCCESS(0, "success"),
    FAILURE(1, "failure"),

    // 参数、权限、资源类
    PARAM_ERROR(1001, "参数校验失败"),
    UNAUTHORIZED(1002, "未登录或登录已过期"),
    FORBIDDEN(1003, "无访问权限"),
    NOT_FOUND(1004, "请求资源不存在"),

    // 业务类（示例，可按业务扩展）
    FIREBASE_ERROR(2001, "Firebase 调用失败"),
    DATA_CONFLICT(2002, "数据冲突"),
    // ...

    // 系统类
    SERVER_ERROR(5000, "服务器内部错误");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
