package com.xidian.bankdemo.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class Friend {
    private Long id;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long account;
    private String friendName;
    private String bankName;
    @JsonSerialize(using=ToStringSerializer.class)
    private Long ownerId;

    public Friend() {
    }

    public Friend(Long account, String friendName, String bankName, Long ownerId) {
        this.account = account;
        this.friendName = friendName;
        this.bankName = bankName;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", account=" + account +
                ", friendName='" + friendName + '\'' +
                ", bankName='" + bankName + '\'' +
                ", ownerId=" + ownerId +
                '}';
    }
}
