package com.xidian.bankdemo.security.signverify.exception;

/**
 * 签名验签服务器响应结果异常类
 **/
public class SVSResponseException extends Exception {

    /**
     * 报错信息
     */
    private String message;

    public SVSResponseException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
