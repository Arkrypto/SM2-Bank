package com.xidian.bankdemo.security.timestamp;

import com.xidian.bankdemo.common.TimeStampInfo;
import com.zayk.tsa.api.TSHandle;
import com.zayk.tsa.api.ZaTSAApi;
import com.zayk.tsa.org.bouncycastle.util.encoders.Base64;
import com.zayk.tsa.util.ConstUtil;
import io.jsonwebtoken.io.Encoders;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class ZaykTimeStamp {
    private static final String ip = "";
    private static final int port = -1;
    private static int hashAlg = ConstUtil.SGD_SM3;//ConstUtil.SGD_SM3;
    private static int signAlg = ConstUtil.SGD_SM3_SM2;// ConstUtil.SGD_SM3_SM2;
    private static final String certName = "server.cer";
//    private static String subject = "CN=unionpayte@DA@Z00000000@1,OU=Organizational-1,OU=unionpaytest,O=CFCA TEST OCA31 SM2,C=CN";

    static{
        System.out.println("初始化服务连接: " + ZaTSAApi.InitServiceConnect(ip, port));
//        try {
//            subject = new String(subject.getBytes("GBK"), "ISO-8859-1");
//        } catch (Exception e) {
//            System.out.println("In getBytes, " + e.getMessage());
//        }
//        TSHandle handle = new TSHandle();
        // 测试一下连接
//        int rv = ZaTSAApi.STF_InitEnvironment(handle);
//        if (rv != 0) {
//            System.out.println("---------->TSA_InitEnv Err = " + rv);
//        } else {
//            System.out.println("==========>STF_InitEnvironment OK");
//        }
//        rv = ZaTSAApi.STF_ClearEnvironment(handle);
//        if (rv != 0) {
//            System.out.println("---------->TSA_ClearEnv Err = " + rv);
//        } else {
//            System.out.println("==========>STF_ClearEnvironment OK");
//        }
    }

    public static void setAlgo(int i){
        if(i == 1){
            hashAlg = ConstUtil.SGD_SHA1;
            signAlg = ConstUtil.SGD_SHA1_RSA;
        } else if(i == 2){
            hashAlg = ConstUtil.SGD_SM3;
            signAlg = ConstUtil.SGD_SM3_SM2;
        }
    }

    private static byte[] generateTSRequest(TSHandle handle, String pucInData){
        // 创建时间戳请求
//        byte[] pucTSRequest = ZaTSAApi.STF_CreateTSRequest(handle, pucInData.getBytes(), 0, "".getBytes(), hashAlg);
        byte[] pucTSRequest = ZaTSAApi.STF_CreateTSRequest(handle, pucInData.getBytes(), 1, "".getBytes(), hashAlg);
        if (Objects.isNull(pucTSRequest)) {
            assert handle != null;
            System.out.println("---------->STF_CreateTSResponse_Ex Err = " + handle.getErrCode());
        } else {
            System.out.println("STF_CreateTSResponse_Ex=" + new String(Base64.encode(pucTSRequest)));
            System.out.println("STF_CreateTSResponse_ExLen=" + pucTSRequest.length);
            System.out.println("==========>STF_CreateTSResponse_Ex OK");
        }
        return pucTSRequest;
    }

    private static byte[] getTSResponse(TSHandle handle, byte[] pucTSRequest){
        byte[] pucTSResponse = ZaTSAApi.STF_CreateTSResponse(handle, pucTSRequest, signAlg);
        if (Objects.isNull(pucTSResponse)) {
            assert handle != null;
            System.out .println("---------->TSA_CreateTSAResponse Err = " + handle.getErrCode());
        } else {
            System.out.println("TSA_CreateTSAResponse=" + new String(Base64.encode(pucTSResponse)));
            System.out.println("TSA_CreateTSAResponseLen=" + pucTSResponse.length);
            System.out.println("==========>STF_CreateTSResponse OK");
        }
        return pucTSResponse;
    }


    public static byte[] readCertFile(String fileName) {
        File filename = new File(fileName);
        FileInputStream reader = null;
        try {
            reader = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] bytes = null;
        try {
            bytes = new byte[reader.available()];
            while (reader.read(bytes) != -1) {}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
        return  bytes;
    }


    private static Date parseDate(byte[] dateStr) {
        if(dateStr == null){
            return null;
        }
        Date date = null;
        try{
            SimpleDateFormat sDate = new SimpleDateFormat("yyyyMMddHHmmss");
            Date parse = sDate.parse(new String(dateStr));
            long time = parse.getTime() + 28800000;
            date = new Date(time);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }


    private static boolean verifyValidity(TSHandle handle, byte[] pucTSResponse, byte[] cert){
//        System.out.println("开始验证有效性");
        int rv = ZaTSAApi.STF_VerifyTSValidity(handle, pucTSResponse, hashAlg, signAlg, cert);
        if (rv != 0) {
            System.out.println("---------->TSA_VerifyTSValidity Err = " + handle.getErrCode());
        } else {
            System.out.println("==========>STF_VerifyTSValidity OK");
        }
        return rv==0;
    }

    private static Date getDateInfo(TSHandle handle, byte[] pucTSResponse){
        byte[] dateStr = ZaTSAApi.STF_GetTSDetail(handle, pucTSResponse, ConstUtil.STF_TIME_OF_STAMP);
        if(!Objects.isNull(dateStr)){
            System.out.println("时间戳请求的原始信息=" + new String(dateStr));
            System.out.println("时间戳请求的原始信息长度=" + new String(dateStr).length());
            return parseDate(dateStr);
        }
        return null;
    }

    private static String getAboutInfo(TSHandle handle, byte[] pucTSResponse){
        byte[] about = ZaTSAApi.STF_GetTSDetail(handle, pucTSResponse, ConstUtil.STF_CN_OF_TSSIGNER);
        if(!Objects.isNull(about)){
            System.out.println("签发者的通用名=" + new String(about));
            System.out.println("签发者的通用名长度=" + new String(about).length());
            return new String(about);
        }
        return null;
    }

    public static TimeStampInfo getTimeStampInfo(){
        return getTimeStampInfo("123");
    }

    public static TimeStampInfo getTimeStampInfo(String pucInData) {
        System.out.println(pucInData);
        TSHandle handle = new TSHandle();
        TimeStampInfo timeStampInfo = new TimeStampInfo();
        // 创建时间戳请求
        byte[] pucTSRequest = generateTSRequest(handle, pucInData);
        // 获取时间戳应答
        byte[] pucTSResponse = getTSResponse(handle, pucTSRequest);
        timeStampInfo.setTimeStamp(Encoders.BASE64URL.encode(pucTSResponse));
        // 验证时间戳的有效性，无效为0，有效为1
        // 没有cert啦，certName
        timeStampInfo.setValid(verifyValidity(handle, pucTSResponse, readCertFile(certName)));
//        timeStampInfo.setValid(verifyValidity(handle, pucTSResponse, null));
        // 设置签名时间
        timeStampInfo.setTime(getDateInfo(handle, pucTSResponse));
        // 设置通用名信息
        timeStampInfo.setAbout(getAboutInfo(handle, pucTSResponse));
        System.out.println(timeStampInfo);
        return timeStampInfo;

    }



    public static void test() throws ParseException {
        SimpleDateFormat sDate = new SimpleDateFormat("yyyyMMddHHmmss");
        Date parse = sDate.parse("20240612061600.656z");
        long time = parse.getTime() + 28800000;
        Date date = new Date(time);
        System.out.println(date);
    }
    public static void main(String[] args) throws ParseException {
//        String message = "hello world";
//        TimeStampInfo timeStampInfo = getTimeStampInfo(message);
//        System.out.println(timeStampInfo);
//        System.out.println(timeStampInfo.getTime());
//        System.out.println(new Date());
        test();
    }

}
