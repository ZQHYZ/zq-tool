package com.zqhyz.entity;

public record R<T>(int code, T data, String msg) {

    public static <T> R<T> result(int code, String msg, T data){
        return new R<>(code, data, msg);
    }
    public static <T> R<T> ok(T data){
        return result(200, "操作成功！", data);
    }

    public static <T> R<T> forbidden(String msg){
        return result(403, msg, null);
    }

    public static <T> R<T> unauthorized(String msg){
        return result(401, msg, null);
    }
}
