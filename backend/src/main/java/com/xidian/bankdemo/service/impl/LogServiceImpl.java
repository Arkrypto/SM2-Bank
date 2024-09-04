package com.xidian.bankdemo.service.impl;

import com.xidian.bankdemo.entity.Log;
import com.xidian.bankdemo.mapper.LogMapper;
import com.xidian.bankdemo.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public List<Log> getLog(String token) { // fixme: 限制接口访问或者验证token
        return logMapper.findLog();
    }

//    @Async
    @Override
    public int addLog(Log log) { // TODO: 异步执行插入日志，减小响应延迟
        if (log.getUsername() != null) {
            printLog(log);
            return logMapper.insertLog(log);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            log.setUsername(authentication.getName());//获取id需要查询数据库
            printLog(log);
            return logMapper.insertLog(log);
        }
        return 0;
    }

    @Override
    public int delLog(Long id) {
        int result = logMapper.deleteById(id);
        addLog(new Log(LogService.DELETE, "日志"+id, result, "管理员删除日志"));
        return result;
    }

    @Override
    public int delAll() {
        int result = logMapper.deleteAll()>0?1:0;
        addLog(new Log(LogService.DELETE, "日志", result, "管理员清空日志"));
        return result;
    }

    private void printLog(Log consoleLog) {
        final String[] TYPE = {"创建","删除","修改","读取"};
        log.info("====================日志信息====================");
        log.info("{}:{}-{}-{}-{}",
                consoleLog.getUsername(),
                TYPE[consoleLog.getType()],
                consoleLog.getObj(),
                consoleLog.getResult()==1?"成功":"失败",
                consoleLog.getDescription());
        log.info("===============================================");
    }
}
