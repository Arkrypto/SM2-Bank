package com.xidian.bankdemo.service;

import com.xidian.bankdemo.entity.Log;

import java.util.List;

public interface LogService {
    int CREATE = 0;
    int DELETE = 1;
    int UPDATE = 2;
    int READ = 3;

    List<Log> getLog(String token);
    int addLog(Log log);
    int delLog(Long id);
    int delAll();
}
