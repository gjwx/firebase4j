package com.jfztkg.common;

/**
 * @Author gongjiawei
 * @Date 2025/11/28 23:19
 * @注释
 */

import java.io.Serializable;

/**
 * 统一对外返回结果
 */
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 业务状态码：0 表示成功，其他表示各种错误 */
    private int code;

    /** 提示信息 */
    private String msg;

    /** 业务数据 */
    private T data;

    /** 时间戳（毫秒） */
    private long timestamp;

    // ======== 构造方法 ========

    public ApiResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ======== 静态工厂方法（推荐用这个来创建实例） ========

    public static <T> ApiResult<T> success() {
        return new ApiResult<T>(
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMsg(),
                null
        );
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(
                ResultCode.SUCCESS.getCode(),
                ResultCode.SUCCESS.getMsg(),
                data
        );
    }

    public static <T> ApiResult<T> failure(ResultCode codeEnum) {
        return new ApiResult<T>(
                codeEnum.getCode(),
                codeEnum.getMsg(),
                null
        );
    }

    public static <T> ApiResult<T> failure(ResultCode codeEnum, String extraMsg) {
        String msg = codeEnum.getMsg();
        if (extraMsg != null && extraMsg.trim().length() > 0) {
            msg = msg + "：" + extraMsg;
        }
        return new ApiResult<T>(
                codeEnum.getCode(),
                msg,
                null
        );
    }

    public static <T> ApiResult<T> failure(int code, String msg) {
        return new ApiResult<T>(code, msg, null);
    }

    // ======== getter / setter ========

    public int getCode() {
        return code;
    }

    public ApiResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ApiResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ApiResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ApiResult<T> setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    // ======== 便捷判断 ========

    public boolean isSuccess() {
        return this.code == ResultCode.SUCCESS.getCode();
    }
}

