package com.xidian.bankdemo.security.signverify.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单包验证数字签名 - 请求格式包
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifySignedDataReq {

    /**
     * 签名算法标识
     */
    private Integer signMethod;

    /**
     * 验证数字签名时使用证书或证书序列号，1表示使用证书，2表示使用证书序列号
     */
    private Integer type;

    /**
     * Base64编码的证书，type取值1时有效
     */
    private String cert;

    /**
     * Base64编码的证书证书序列号，type取值2时有效
     */
    private String certSN;

    /**
     * Base64编码的待签名的数据原文
     */
    private String inData;

    /**
     * 待签名的数据原文长度
     */
    private Integer inDataLen;

    /**
     * Base64编码的签名者的ID值，当signMethod为SM3WithSM2时有效
     */
    private String signerID;

    /**
     * 签名者的ID长度，当signMethod为SGD_SM3_SM2时有效。
     */
    private Integer signerIDLen;

    /**
     * Base64编码的签名结果
     */
    private String signature;

    /**
     * 表示证书验证级别，0：验证时间，1:验证时间和根证书签名，2:验证时间、根证书签名和CRL
     */
    private Integer verifyLevel;

}
