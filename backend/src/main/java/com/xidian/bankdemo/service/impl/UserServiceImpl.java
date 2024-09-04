package com.xidian.bankdemo.service.impl;

import com.xidian.bankdemo.dto.LoginInfoDTO;
import com.xidian.bankdemo.entity.Log;
import com.xidian.bankdemo.entity.User;
import com.xidian.bankdemo.mapper.UserMapper;
import com.xidian.bankdemo.security.signverify.OlymSignature;
import com.xidian.bankdemo.security.signverify.Signature;
import com.xidian.bankdemo.security.XCiphersm;
import com.xidian.bankdemo.service.LogService;
import com.xidian.bankdemo.service.UserService;
import com.xidian.bankdemo.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import net.olymtech.javakgc.base.utils.exception.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LogService logService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String doLogin(String username, String password) { // TODO: 密码加密存储
        Log log = new Log(LogService.READ, "用户名和密码", 1, "用户登录");
        log.setUsername(username);
        if (passwordEncoder.matches(password, userMapper.findPwdByName(username))) {
            logService.addLog(log);
            return TokenUtil.generateToken(username);
        }
        log.setResult(0);
        logService.addLog(log);
        return null;
    }

    @Override
    public String doLogin(LoginInfoDTO user) {
        User uu = userMapper.findByName(user.getUsername());
        String cert = uu.getCertificate();
        String iniData = user.getIniData();
        String signData = user.getSignature();
        System.out.println("iniData: " + iniData);
        System.out.println("signature: " + signData);
//        String signature = "";
//        try{
//            signature = OlymSignature.signature(iniData);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        System.out.println("后端的签名验证结果: " + OlymSignature.verifySignature(null, iniData, signature));
//        System.out.println("一旦验证前端传来的签名就会报错: ");
        boolean flag = OlymSignature.verifySignature(cert, iniData, signData);
        if(flag){
            return doLogin(user.getUsername(), user.getPassword());
        }else {
            return null;
        }


    }

    @Override
    public int doRegister(User user) {
        System.out.println("certificate: "+user.getCertificate());
        log.debug("用户注册# {}:{}:{}：{}：{}", user.getUsername(), user.getPassword(), user.getAge(), user.getGender() == 1 ? "男" : "女", user.getEmail());
        if (userMapper.existsByName(user.getUsername())) return 2;

        /***
         * 对用户登陆密码、交易支付密码、idCard进行加密操作处理
         */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPayPassword(passwordEncoder.encode(user.getPayPassword()));
        try {
            user.setIdCard(XCiphersm.sm4Encrypt(user.getIdCard()));
        } catch (CryptoException e) {
            System.out.println("****加密idCard出错*****");
            e.printStackTrace();
        }

        if (user.getRole() == null) {
            user.setRole(0);
            user.setStatus(0);
            user.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        }
        int result = userMapper.insertUser(user);
        Log log = new Log(LogService.CREATE, "用户信息", result, "用户注册");
        log.setUsername(user.getUsername());
//        log.setGmtCreate(TimeStamp.getTimeStampInfo().getTime());
        logService.addLog(log);
        return result;
    }

    @Override
    public int editUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        int result = userMapper.updateUser(user);
        logService.addLog(new Log(LogService.UPDATE, user.getUsername() + "用户信息", result, "管理员修改用户信息"));
        return result;
    }

    @Override
    public int delUser(Long id) {
        int result = userMapper.deleteUser(id);
        logService.addLog(new Log(LogService.DELETE, "用户:" + id, result, "管理员删除用户"));
        return result;
    }

    @Override
    public User getInfo(String token) {
        String username = TokenUtil.validateToken(token);
        return userMapper.getInfoByName(username);
    }

    @Override
    public int setInfo(User info) {
        log.debug("用户更新信息# {}:{}-{}-{}", info.getUsername(), info.getAge(), info.getGender() == 1 ? "男" : "女", info.getEmail());
        int result = userMapper.setInfoByName(info);
        Log log = new Log(LogService.UPDATE, "用户信息", result, "用户修改信息");
        log.setUsername(info.getUsername());
        logService.addLog(log);
        return result;
    }

    @Override
    public int setPwd(LoginInfoDTO user) {
        log.debug("修改密码：{},{}", user.getUsername(), user.getPassword());
        int result = userMapper.setPwdByName(user.getUsername(), passwordEncoder.encode(user.getPassword()));
        Log logger = new Log(LogService.UPDATE, "用户密码", result, "用户修改密码");
        logger.setUsername(user.getUsername());
        if (logService.addLog(logger) < 1) log.error("保存日志失败！");
        return result;
    }
    @Override
    public int setPpwd(LoginInfoDTO user) {
        log.debug("修改支付密码：{},{},{}", user.getUsername(),user.getIdCard(), user.getPayPassword());
        System.out.println("idCard: "+user.getIdCard());
        System.out.println("username: "+ user.getUsername());
        System.out.println("payPassword: "+user.getPayPassword());
        String dbIdCard = null;
        String dbPayPassword = null;
        try {
          dbIdCard = XCiphersm.sm4Encrypt(user.getIdCard());
          dbPayPassword = XCiphersm.sm3_Hash(user.getPayPassword());
        } catch (CryptoException e) {
            e.printStackTrace();
            System.out.println("加密IdCard失败");
            System.out.println("加密payPassword失败");
        }
       int result =  userMapper.setPpwdByNameAndIdCard(user.getUsername(),dbIdCard,dbPayPassword);
        Log logger = new Log(LogService.UPDATE, "用户密码", result, "用户修改支付密码");
        logger.setUsername(user.getUsername());
        if (logService.addLog(logger) < 1) log.error("保存日志失败！");
        return result;
    }

    @Override
    public int genCode(Long id) {
        int code = new Random().nextInt(899999) + 100000;
        int result = userMapper.updateCode(id, code);
        logService.addLog(new Log(LogService.CREATE, "用户Code", result, "用户请求验证码"));
        if (result > 0) return code;
        return 0;
    }

    @Override
    public boolean verifyCode(String name, int code) {
        boolean result = userMapper.existsByNameAndCode(name, code);
        Log log = new Log(LogService.READ, "用户Code", result ? 0 : 1, "用户验证码验证");
        log.setUsername(name);
        logService.addLog(log);
        return result;
    }

    @Override
    public boolean verifyPwd(LoginInfoDTO user) {
        log.debug("验证用户旧密码{}:{}", user.getUsername(), user.getPassword());
        return passwordEncoder.matches(user.getPassword(), userMapper.findPwdByName(user.getUsername()));
    }

    @Override
    public List<User> getList(String token) {
        // todo: 验证token或者security认证
        return userMapper.findAll();
    }

}
