package com.xidian.bankdemo.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class QueryDTO {
    @JsonSerialize(using=ToStringSerializer.class)
    private Long myAccount;
    @JsonSerialize(using=ToStringSerializer.class)
    private Long objAccount;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Integer limit;
    private Integer offset;

    public Long getMyAccount() {
        return myAccount;
    }

    public void setMyAccount(Long myAccount) {
        this.myAccount = myAccount;
    }

    public Long getObjAccount() {
        return objAccount;
    }

    public void setObjAccount(Long objAccount) {
        this.objAccount = objAccount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "QueryDTO{" +
                "myAccount=" + myAccount +
                ", objAccount=" + objAccount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
