package com.xidian.bankdemo.scheduling;

import com.xidian.bankdemo.entity.Log;
import com.xidian.bankdemo.mapper.LogMapper;
import com.xidian.bankdemo.security.XCiphersm;
import net.olymtech.javakgc.base.utils.exception.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Configuration
public class Task {



    @Autowired
    private LogMapper logMapper;


    @Scheduled(cron= "0 0 0 * * ?")
    public void task() throws CryptoException {
        Calendar calendar = Calendar.getInstance();
        Date end = getStartOfDay(calendar.getTime());
        System.out.println("end:----"+end);
        calendar.set(Calendar.HOUR_OF_DAY,-24*2);
        Date start = getStartOfDay(calendar.getTime());
        System.out.println("start:---"+start);
        List<Log> logByDate = logMapper.findLogByDate(start, end);
        String mes ="";
        for (Log log : logByDate) {
            mes+= log.toString();
        }
        String hashvalue = XCiphersm.sm3_Hash(mes);
        System.out.println(hashvalue.length());
        System.out.println(hashvalue);


    }

    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 获得某天最小时间 2020-02-17 00:00:00
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }


}
