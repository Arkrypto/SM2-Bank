package com.xidian.bankdemo.security.signverify.pojo;

import lombok.Data;

/**
 * 单包验证数字签名 - 响应格式包
 **/
@Data
public class VerifySignedDataResp {

    /**
     * 响应码。0表示成功，非0表示错误
     */
    private Integer respValue;

}
