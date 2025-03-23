package com.xidian.bankdemo.security.signverify.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单包数字签名 - 请求格式包
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignDataReq {

    /**
     * 签名算法标识
     */
    private Integer signMethod;

    /**
     * 签名者私钥的索引值
     */
    private Integer keyIndex;

    /**
     * base64编码签名者私钥权限码
     */
    private String keyValue;

    /**
     * 签名者的ID长度，当signMethod为SM3WithSM2时有效
     */
    private Integer signerIDLen;

    /**
     * base64编码签名者的ID值，当signMethod为SM3WithSM2时有效
     */
    private String signerID;

    /**
     * 待签名的数据原文长度
     */
    private Integer inDataLen;

    /**
     * Base64编码的待签名的数据原文
     */
    private String inData;

}
