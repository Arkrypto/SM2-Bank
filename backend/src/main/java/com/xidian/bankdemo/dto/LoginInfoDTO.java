package com.xidian.bankdemo.dto;

public class LoginInfoDTO {
    private String username;
    private String password;
    private String code;
    private  String signature;
    private  String iniData;
    private String idCard;
    private String payPassword;

    public String getIniData() {
        return iniData;
    }

    public void setIniData(String iniData) {
        this.iniData = iniData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }
}
