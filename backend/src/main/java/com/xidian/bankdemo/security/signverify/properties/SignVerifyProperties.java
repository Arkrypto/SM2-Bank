package com.xidian.bankdemo.security.signverify.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 签名验签服务器配置
 **/
@Data
@Component
@ConfigurationProperties(prefix = "signverify")
public class SignVerifyProperties {

    /**
     * 签名验签服务器url
     */
    private String url;

}
