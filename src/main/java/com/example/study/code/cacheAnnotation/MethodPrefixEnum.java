package com.example.study.code.cacheAnnotation;

public enum MethodPrefixEnum {
    GET("GET", "GET"),
    DELETE("DELETE", "DELETE"),
    UPDATE("UPDATE", "UPDATE"),
    INSERT("INSERT", "INSERT");

    final String code;
    final String msg;

    MethodPrefixEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }
}
