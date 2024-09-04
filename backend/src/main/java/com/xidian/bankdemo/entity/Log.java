package com.xidian.bankdemo.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.xidian.bankdemo.security.timestamp.TimeStamp;
import com.xidian.bankdemo.security.timestamp.ZaykTimeStamp;

import java.util.Date;

public class Log {
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using=ToStringSerializer.class)
    private Long uid;
    private String username;
    private Integer type;
    private String obj;
    private Integer result;
    private String description;

    private Integer deleted;
    private Date gmtCreate;
    private Date gmtModified;

    public Log() {
        Date time = ZaykTimeStamp.getTimeStampInfo().getTime();
        this.gmtCreate=time;
        this.gmtModified = time;
    }
    public Log(Integer type, String obj, Integer result, String description) {
        this.type = type;
        this.obj = obj;
        this.result = result;
        this.description = description;
        Date time = ZaykTimeStamp.getTimeStampInfo().getTime();
        this.gmtCreate=time;
        this.gmtModified = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "log{" +
                "id=" + id +
                ", uid=" + uid +
                ", username='" + username + '\'' +
                ", type=" + type +
                ", obj='" + obj + '\'' +
                ", result=" + result +
                ", description='" + description + '\'' +
                ", deleted=" + deleted +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
