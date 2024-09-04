package com.xidian.bankdemo.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.util.Date;

public class StatementDTO {
    @JsonSerialize(using=ToStringSerializer.class)
    private Long myAccount;
    @JsonSerialize(using=ToStringSerializer.class)
    private Long tradeAccount;
    private String tradeUser;
    private Date tradeTime;
    private String tradeType;
    private BigDecimal amount;
    private String description;
    private Integer status;
    private String signature;
    private String server_signature;
    private String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getServer_signature() {
        return server_signature;
    }

    public void setServer_signature(String server_signature) {
        this.server_signature = server_signature;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getMyAccount() {
        return myAccount;
    }

    public void setMyAccount(Long myAccount) {
        this.myAccount = myAccount;
    }

    public Long getTradeAccount() {
        return tradeAccount;
    }

    public void setTradeAccount(Long tradeAccount) {
        this.tradeAccount = tradeAccount;
    }

    public String getTradeUser() {
        return tradeUser;
    }

    public void setTradeUser(String tradeUser) {
        this.tradeUser = tradeUser;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatementDTO{" +
                "myAccount=" + myAccount +
                ", tradeAccount=" + tradeAccount +
                ", tradeUser='" + tradeUser + '\'' +
                ", tradeTime=" + tradeTime +
                ", tradeType='" + tradeType + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", signature='" + signature + '\'' +
                ", server_signature='" + server_signature + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
