package com.xidian.bankdemo.common;

public class Result {
    private int code;//2000正常，5000出错
    private String message;
    private Object data;

    public Result() {
    }
    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result OK() {
        return OK(null);
    }
    public static Result OK(Object data) {
        return new Result(2000, "操作成功", data);
    }
    public static Result ERROR() {
        return ERROR("操作失败");
    }
    public static Result ERROR(String message) {
        return new Result(5000, message, null);
    }
    public static Result ERROR(Object reason) {
        return new Result(5000, "操作失败", reason);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
