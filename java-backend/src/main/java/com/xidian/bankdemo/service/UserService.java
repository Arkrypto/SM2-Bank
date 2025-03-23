package com.xidian.bankdemo.service;

import com.xidian.bankdemo.dto.LoginInfoDTO;
import com.xidian.bankdemo.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    String doLogin(String username, String password);
    String doLogin(LoginInfoDTO user);
    int doRegister(User user);
    int editUser(User user);
    int delUser(Long id);
    User getInfo(String token);
    int setInfo(User info);
    int setPwd(LoginInfoDTO user);
    int setPpwd(LoginInfoDTO user);
    int genCode(Long id);
    boolean verifyCode(String name, int code);
    boolean verifyPwd(LoginInfoDTO user);
    List<User> getList(String token);
}
