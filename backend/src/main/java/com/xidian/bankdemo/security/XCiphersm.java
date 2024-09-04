package com.xidian.bankdemo.security;

import net.olymtech.javakgc.base.sm9.engine.XCipherConstant;
import net.olymtech.javakgc.base.sm9.engine.XCipherEngine;
import net.olymtech.javakgc.base.sm9.engine.XCipherSession;
import net.olymtech.javakgc.base.utils.Base64;
import net.olymtech.javakgc.base.utils.CryptUtils;
import net.olymtech.javakgc.base.utils.exception.CryptoException;

import java.io.UnsupportedEncodingException;

public class XCiphersm {
    private static final String ip = "";
    private static final int port = -1;
    private static final int symIndex = -1;
    public static final String password = ""; //(sm2)
    public static void main(String[] args) throws CryptoException, UnsupportedEncodingException {
        String pucData = "123456";
        System.out.println("明文："+pucData);
        //sm4对称加密
        String encData = sm4Encrypt(pucData);
        System.out.println("密文："+encData);
        //sm4对称解密
        String decData = sm4Decrypt(encData);
        System.out.println("解密的明文："+decData);
        //sm3
        String hashData = sm3_Hash(pucData);
        System.out.println("sm3运算结果："+hashData);
    }


    /**
     * sm3 hash运算
     *
     * @throws CryptoException
     */
    public static String sm3_Hash(String rawData) throws CryptoException {
        XCipherSession xSession = XCipherEngine.getInstance().SDF_OpenSession(ip, port);
        XCipherEngine.getInstance().SDF_HashInit(xSession, XCipherConstant.SGD_SM3, null,null, 0);
        XCipherEngine.getInstance().SDF_HashUpdate(xSession,rawData.getBytes(),rawData.length());
        byte[] pucHash;

        pucHash = XCipherEngine.getInstance().SDF_HashFinal(xSession);
        return CryptUtils.byte2hex(pucHash);
    }

    /**
     * sm4对称加密 测试
     *
     * @return
     * @throws CryptoException
     */
    public static String sm4Encrypt(String pucData) throws CryptoException {
        XCipherSession xSession = XCipherEngine.getInstance().SDF_OpenSession(ip, port, symIndex, password);
        XCipherEngine.getInstance().SDF_GetPrivateKeyAccessRight_Ex(xSession);
        byte[] iv = "XhjJeRujDsv89nkp".getBytes();
        String plainData = "0123456789123456";
        byte[] enc = XCipherEngine.getInstance().SDF_Encrypt(xSession, symIndex, 0, XCipherConstant.SGD_SMS4_CBC, iv,
                pucData.getBytes());
        XCipherEngine.getInstance().SDF_CloseSession(xSession);
        return Base64.encode(enc);
    }

    /**
     * 对称解密 测试
     *
     * @param enc
     * @return
     * @throws CryptoException
     */
    public static String sm4Decrypt(String enc) throws CryptoException, UnsupportedEncodingException {
        XCipherSession xSession = XCipherEngine.getInstance().SDF_OpenSession(ip, port, symIndex, password);
        XCipherEngine.getInstance().SDF_GetPrivateKeyAccessRight_Ex(xSession);
        byte[] iv = "XhjJeRujDsv89nkp".getBytes();
        byte[] decData = XCipherEngine.getInstance().SDF_Decrypt(xSession, symIndex, 0, XCipherConstant.SGD_SMS4_CBC, iv, Base64.decode(enc));
        String plain = new String(decData);
        XCipherEngine.getInstance().SDF_CloseSession(xSession);
        return plain;
    }



}
