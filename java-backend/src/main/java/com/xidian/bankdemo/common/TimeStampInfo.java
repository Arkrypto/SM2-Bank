package com.xidian.bankdemo.common;

import java.util.Date;

public class TimeStampInfo {

    private boolean valid;
    private String about;
    private Date time;
    private String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TimeStampInfo{" +
                "valid=" + valid +
                ", about='" + about + '\'' +
                ", time=" + time +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
