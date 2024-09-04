package com.xidian.bankdemo.security.signverify;

/**
 * URI常量
 **/
public interface URIConstants {

    //导出证书URI
    String URI_EXPORTCERT = "/ExportCert";

    //解析证书URI
    String URI_PARSECERT = "/ParseCert";

    //验证证书有效性URI
    String URI_VALIDATECERT = "/ValidateCert";

    //单包数字签名URI
    String URI_SIGNDATA = "/SignData";

    //单包验证数字签名URI
    String URI_VERIFYSIGNEDDATA = "/VerifySignedData";

    //多包数字签名初始化URI
    String URI_SIGNDATAINIT = "/SignDataInit";

    //多包数字签名更新URI
    String URI_SIGNDATAUPDATE = "/SignDataUpdate";

    //多包数字签名结束URI
    String URI_SIGNDATAFINAL = "/SignDataFinal";

    //多包数字验证签名初始化URI
    String URI_VERIFYSIGNEDDATAINIT = "/VerifySignedDataInit";

    //多包数字验证签名更新URI
    String URI_VERIFYSIGNEDDATAUPDATE = "/VerifySignedDataUpdate";

    //多包验证数字签名结束URI
    String URI_VERIFYSIGNEDDATAFINAL = "/VerifySignedDataFinal";

    //单包消息签名URI
    String URI_SIGNMESSAGE = "/SignMessage";

    //单包验证消息签名URI
    String URI_VERIFYSIGNEDMESSAGE = "/VerifySignedMessage";

}
