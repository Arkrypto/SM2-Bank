package com.xidian.bankdemo.security.signverify.pojo;

import lombok.Data;

/**
 * 时间戳 响应体
 **/
@Data
public class SVSRespond<T> {

    /**
     * 协议版本
     * 描述响应语法的版本，当前版本号为1，取整型值0
     */
    private Integer version;


    /**
     * 响应类型
     */
    private String respType;

    /**
     * 响应时间
     * 产生请求的时间，采用GeneralizedTime语法
     */
    private String respTime;

    /**
     * 响应包
     */
    private T respond;

}
