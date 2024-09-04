package com.xidian.bankdemo.controller;

import com.xidian.bankdemo.common.Result;
import com.xidian.bankdemo.dto.LoginInfoDTO;
import com.xidian.bankdemo.entity.Log;
import com.xidian.bankdemo.entity.User;
import com.xidian.bankdemo.service.LogService;
import com.xidian.bankdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    LogService logService;

    @GetMapping("/list")
    public Result getList(String token){
        return Result.OK(logService.getLog(token));
    }
    @DeleteMapping("/del")
    public Result delLog(@RequestParam("id") Long id) {
        if (logService.delLog(id) == 1) {
            return Result.OK();
        }
        return Result.ERROR("删除日志失败");
    }
    @DeleteMapping("/del/all")
    public Result delAll() {
        if (logService.delAll() == 1) {
            return Result.OK();
        }
        return Result.ERROR("清空日志失败");
    }
}
