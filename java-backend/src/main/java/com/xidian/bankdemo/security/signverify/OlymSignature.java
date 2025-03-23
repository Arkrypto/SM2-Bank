package com.xidian.bankdemo.security.signverify;

import com.xidian.bankdemo.security.signverify.properties.SignVerifyProperties;
import com.xidian.bankdemo.security.signverify.pojo.*;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class OlymSignature {
    private static final Integer KeyIndex = -1;
    private static final String Password = "";
    private static final Integer Type = -1;
    private static final Integer VerifyLevel = -1;
    private static final String SignerID = "";
    private static final Integer SGD_SM3_SM2 = 131585;
    private static final String Cert = "";
	private static final String CertCN = "";
    public static final Integer SVSRESPONSE_RESPVALUE_SUCCESS = 0;

    // 单包数字签名
    public static String signature(String in) throws Exception {
        byte[] signerIDBytes = "1234567812345678".getBytes();
        Integer signerIDLen = signerIDBytes.length; // 签名者长度
        String signerID = Base64.getEncoder().encodeToString(signerIDBytes);

        byte[] inDataBytes = in.getBytes();
        Integer inDataLen = inDataBytes.length;
        System.out.println(inDataLen);
        // 尝试不编码
        String inData = Base64.getEncoder().encodeToString(inDataBytes); // 对原文Base64编码
        String keyValue = Base64.getEncoder().encodeToString(Password.getBytes()); // PIN码Base64编码


        // 单包数字签名请求封装
        SignDataReq signDataReq = new SignDataReq();
        signDataReq.setSignMethod(SGD_SM3_SM2); // 131585
        signDataReq.setKeyIndex(KeyIndex); // 101
        signDataReq.setKeyValue(keyValue); // Base64(123456)
        signDataReq.setSignerID(signerID); // Base64(1234567812345678)
        signDataReq.setSignerIDLen(signerIDLen);
        signDataReq.setInData(inData); // Base64(原文字符串数据)
        signDataReq.setInDataLen(inDataLen);
        String signature = SignVerifyUtil.signData(signDataReq);

        System.out.println(signature);

        return signature;
    }

    public static String generateSignature(Map<String, Object> map) {
        String signed = "";
        try{
            signed = signature(map.toString());
        }catch (Exception e){
            System.out.println("---------->签名出错");
            e.printStackTrace();
        }
        return signed;
    }


    // 单包数字签名验证
    // 是证书验证还是证书序列号验证
    // 验签的严格等级选哪个
    public static boolean verify(String cert, String inData, String signature) throws Exception {
        if(cert == null){
			cert = Cert;
        }

        byte[] signerIDBytes = SignerID.getBytes();
        Integer signerIDLen = signerIDBytes.length;
        String signerID = Base64.getEncoder().encodeToString(signerIDBytes);

        byte[] inDataBytes = inData.getBytes();
        Integer inDataLen = inDataBytes.length;
        System.out.println(inDataLen);
        inData = Base64.getEncoder().encodeToString(inDataBytes);

        System.out.println("验签所使用的证书为: " + cert);
        System.out.println("验签所使用的原文为: " + inData);
        System.out.println("验签所使用的签名密文为: " + signature);
        System.out.println("验签所使用的签名ID为: " + signerID);

        VerifySignedDataReq verifySignedDataReq = new VerifySignedDataReq();
        verifySignedDataReq.setSignMethod(SGD_SM3_SM2);
        verifySignedDataReq.setType(Type);
        verifySignedDataReq.setCert(cert);
        // Type为1时certSN没用，将使用cert
//        verifySignedDataReq.setCertSN(certSN);
        verifySignedDataReq.setInData(inData);
        verifySignedDataReq.setInDataLen(inDataLen);
        // 这个ID默认为"1234567812345678"
        verifySignedDataReq.setSignerID(signerID);
        verifySignedDataReq.setSignerIDLen(signerIDLen);
        verifySignedDataReq.setSignature(signature);
        verifySignedDataReq.setVerifyLevel(VerifyLevel);
        System.out.println(verifySignedDataReq);
        Integer verifySignedDataResult = SignVerifyUtil.verifySignedData(verifySignedDataReq);
        System.out.println(
                "单包验证数字签名结果：" + Objects.equals(SVSRESPONSE_RESPVALUE_SUCCESS, verifySignedDataResult));
        return SignVerifyUtil.verifySignedData(verifySignedDataReq) == 0;
    }


    public static boolean verifySignature(String cert, String inData, String signed){
        boolean flag = false;
        try{
            flag = verify(cert, inData, signed);
        }catch (Exception e){
            System.out.println("---------->验签出错");
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String[] args) {
        String iniData = "{\"username\":\"canoe\",\"password\":\"Xzt011026\",\"key\":\"123456\",\"code\":\"\",\"signature\":null,\"iniData\":null}";
        String base64 = Base64.getEncoder().encodeToString(iniData.getBytes());
        System.out.println(base64);
    }
}
