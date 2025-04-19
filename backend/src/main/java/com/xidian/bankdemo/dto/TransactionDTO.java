package com.xidian.bankdemo.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;

public class TransactionDTO {//receive
    @JsonSerialize(using=ToStringSerializer.class)
    private Long id;
    private String name;
    @JsonSerialize(using=ToStringSerializer.class)
    private Long myAccount;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long toAccount;
    private String toUsername;
    private String toBankName;
    private BigDecimal amount;
    private String description;
    private BigDecimal fee;
    private Integer verifyCode;
    private  String signature;
    private  String iniData;
    private String payPassword;

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMyAccount() {
        return myAccount;
    }

    public void setMyAccount(Long myAccount) {
        this.myAccount = myAccount;
    }

    public Long getToAccount() {
        return toAccount;
    }

    public void setToAccount(Long toAccount) {
        this.toAccount = toAccount;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getToBankName() {
        return toBankName;
    }

    public void setToBankName(String toBankName) {
        this.toBankName = toBankName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(Integer verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", myAccount=" + myAccount +
                ", toAccount=" + toAccount +
                ", toUsername='" + toUsername + '\'' +
                ", toBankName='" + toBankName + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", fee=" + fee +
                ", verifyCode=" + verifyCode +
                ", signature='" + signature + '\'' +
                ", iniData='" + iniData + '\'' +
                ", payPassword='" + payPassword + '\'' +
                '}';
    }
}
