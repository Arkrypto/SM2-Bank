package com.xidian.bankdemo.security.signverify.pojo;

import lombok.Data;

/**
 * 时间戳 请求体
 **/
@Data
public class SVSRequest<T> {


    /**
     * 协议版本
     * 描述当前语法的版本，当前版本号为1，取整型值0
     */
    private Integer version ;

    /**
     * 请求类型
     * 描述了不同业务的请求类型
     */
    private String reqType;

    /**
     * 请求时间
     * 产生请求的时间，采用GeneralizedTime语法
     */
    private String reqTime;

    /**
     * 请求包
     */
    private T request;

}
