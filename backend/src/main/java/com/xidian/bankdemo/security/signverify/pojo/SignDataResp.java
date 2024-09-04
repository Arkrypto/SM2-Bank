package com.xidian.bankdemo.security.signverify.pojo;

import lombok.Data;

/**
 * 单包数字签名 - 响应格式包
 **/
@Data
public class SignDataResp {

    /**
     * 响应码。0表示成功，非0表示错误
     */
    private Integer respValue;

    /**
     * Base64编码的签名的结果
     */
    private String signature;

}
