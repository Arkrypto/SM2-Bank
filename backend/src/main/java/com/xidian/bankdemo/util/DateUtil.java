package com.xidian.bankdemo.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 **/
public class DateUtil {

    /**
     * 产生请求签名服务器的时间
     *      采用GeneralizedTime语法。格式要求：YYYYMMDDhhmm[ss[.s...]]{Z} + hhmm|-hhmm}
     *      例子：20131001120000Z+0800
     * @return
     */
    public static String generateSvsRequestTime(){
        Instant instant = Instant.now();
        //时区，zoneId = Asia/Shanghai
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        String svsRequestTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss'Z'Z").format(zonedDateTime);
        return svsRequestTime;
    }

}
