package com.xidian.bankdemo.security.signverify;

import com.xidian.bankdemo.security.signverify.exception.SVSResponseException;
import com.xidian.bankdemo.security.signverify.pojo.*;
import com.xidian.bankdemo.security.signverify.properties.SignVerifyProperties;
import com.xidian.bankdemo.util.DateUtil;
import com.xidian.bankdemo.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * 签名验签服务测试类
 **/
@Slf4j
public class SignVerifyUtil {

    //响应码
    public static final Integer SVS_RESPONSE_RESP_VALUE_SUCCESS = 0;
    //请求版本
    public static final Integer SVS_REQUEST_VERSION = 0;
    // 获取对应接口URL
    public static final String signVerifyUrl = SpringUtils.getBean(SignVerifyProperties.class).getUrl();

    public static final RestTemplate restTemplate = new RestTemplate();

    public static final HttpHeaders headers = new HttpHeaders();



    /**
     * 单包数字签名
     */
    public static String signData(SignDataReq signDataReq) throws SVSResponseException {

        //请求体
        SVSRequest<SignDataReq> svsRequest = new SVSRequest<>();
        svsRequest.setVersion(SVS_REQUEST_VERSION);
        svsRequest.setReqType(ReqTypeConstants.REQTYPE_SIGNDATA);
        svsRequest.setReqTime(DateUtil.generateSvsRequestTime());
        svsRequest.setRequest(signDataReq);

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<>(svsRequest, headers);

        ParameterizedTypeReference<SVSRespond<SignDataResp>> responseType =
            new ParameterizedTypeReference<SVSRespond<SignDataResp>>() {};

        System.out.println(signVerifyUrl+URIConstants.URI_SIGNDATA);
        // 发送 POST 请求并获取响应
        ResponseEntity<SVSRespond<SignDataResp>> responseEntity =
            restTemplate.exchange(signVerifyUrl + URIConstants.URI_SIGNDATA, HttpMethod.POST, requestEntity, responseType);

        // 从响应中获取泛型对象
        SVSRespond<SignDataResp> svsRespond = responseEntity.getBody();

        SignDataResp respond = null;
        //无响应
        if(Objects.isNull(svsRespond) || Objects.isNull(respond = svsRespond.getRespond())){
            log.error("signData failed,响应为空");
            throw new SVSResponseException("signData failed,响应为空");
        }
        Integer respValue = respond.getRespValue();
        //响应异常
        if(!SVS_RESPONSE_RESP_VALUE_SUCCESS.equals(respValue)){
            log.error("signData failed,异常响应码：" + respValue);
            throw new SVSResponseException(",异常码为:" + respValue);
        }
        //签名值
        return respond.getSignature();
    }

    /**
     * 单包验证数字签名
     */
    public static Integer verifySignedData(VerifySignedDataReq verifySignedDataReq) throws  SVSResponseException {
        //请求体
        SVSRequest<VerifySignedDataReq> svsRequest = new SVSRequest<>();
        svsRequest.setVersion(SVS_REQUEST_VERSION);
        svsRequest.setReqType(ReqTypeConstants.REQTYPE_VERIFYSIGNEDDATA);
        svsRequest.setReqTime(DateUtil.generateSvsRequestTime());
        svsRequest.setRequest(verifySignedDataReq);

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<>(svsRequest, headers);

        ParameterizedTypeReference<SVSRespond<VerifySignedDataResp>> responseType =
            new ParameterizedTypeReference<SVSRespond<VerifySignedDataResp>>() {};

        // 发送 POST 请求并获取响应
        ResponseEntity<SVSRespond<VerifySignedDataResp>> responseEntity =
            restTemplate.exchange(signVerifyUrl + URIConstants.URI_VERIFYSIGNEDDATA, HttpMethod.POST, requestEntity, responseType);

        // 从响应中获取泛型对象
        SVSRespond<VerifySignedDataResp> svsRespond = responseEntity.getBody();

        VerifySignedDataResp respond;
        //无响应
        if(Objects.isNull(svsRespond) || Objects.isNull(respond = svsRespond.getRespond())){
            log.error("verifySignedData failed,响应为空");
            throw new SVSResponseException("verifySignedData failed,响应为空");
        }
        return respond.getRespValue();
    }


}
