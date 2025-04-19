package com.xidian.bankdemo.security.signverify;

/**
 * 请求类型常量
 **/
public interface ReqTypeConstants {

    //导出证书reqType
    String REQTYPE_EXPORTCERT = "exportCert";

    //解析证书reqType
    String REQTYPE_PARSECERT = "parseCert";

    //验证证书有效性reqType
    String REQTYPE_VALIDATECERT = "validateCert";

    //单包数字签名reqType
    String REQTYPE_SIGNDATA = "signData";

    //单包验证数字签名reqType
    String REQTYPE_VERIFYSIGNEDDATA = "verifySignedData";

    //多包数字签名初始化reqType
    String REQTYPE_SIGNDATAINIT = "signDataInit";

    //多包数字签名更新reqType
    String REQTYPE_SIGNDATAUPDATE = "signDataUpdate";

    //多包数字签名结束reqType
    String REQTYPE_SIGNDATAFINAL = "signDataFinal";

    //多包数字验证签名初始化reqType
    String REQTYPE_VERIFYSIGNEDDATAINIT = "verifySignedDataInit";

    //多包数字验证签名更新reqType
    String REQTYPE_VERIFYSIGNEDDATAUPDATE = "verifySignedDataUpdate";

    //多包数字验证签名结束reqType
    String REQTYPE_VERIFYSIGNEDDATAFINAL = "verifySignedDataFinal";

    //单包消息签名reqType
    String REQTYPE_SIGNMESSAGE = "signMessage";

    //单包验证消息签名reqType
    String REQTYPE_VERIFYSIGNEDMESSAGE = "verifySignedMessage";

}
