package com.xidian.bankdemo.mapper;

import com.xidian.bankdemo.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Mapper
public interface LogMapper {
    List<Log> findLog();
    int insertLog(Log log);
    int deleteById(@Param("id") Long id);
    int deleteAll();
    List<Log> findLogByDate(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date start,  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date end);
}
