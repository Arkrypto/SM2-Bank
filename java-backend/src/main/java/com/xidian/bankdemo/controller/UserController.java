package com.xidian.bankdemo.controller;

import com.koal.security.pki.ess.Hash;
import com.xidian.bankdemo.common.Result;
import com.xidian.bankdemo.common.TimeStampInfo;
import com.xidian.bankdemo.dto.LoginInfoDTO;
import com.xidian.bankdemo.entity.Account;
import com.xidian.bankdemo.entity.User;
import com.xidian.bankdemo.security.signverify.OlymSignature;
import com.xidian.bankdemo.security.signverify.SignVerifyUtil;
import com.xidian.bankdemo.security.signverify.pojo.SignDataReq;
import com.xidian.bankdemo.security.signverify.pojo.VerifySignedDataReq;
import com.xidian.bankdemo.security.timestamp.ZaykTimeStamp;
import com.xidian.bankdemo.service.UserService;
import net.olymtech.javakgc.base.utils.exception.CryptoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.*;


@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    UserService userService;

    @CrossOrigin
    @GetMapping("/hello") // 在config.SecurityConfig设置白名单访问，便于curl测试
    public Object sayHello() throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("nmsl", "wdnmd");
        // 签名的是map.toString()
//        String signature = OlymSignature.generateSignature(map);
        String iniData = "{\"username\":\"wx\",\"password\":\"123456xW\",\"key\":\"12345678\",\"code\":\"fiN2\",\"signature\":null,\"iniData\":null}";
        String signature = OlymSignature.signature(iniData);
        // cert为null时调用写死的证书

        return OlymSignature.verifySignature(null, iniData, signature);
    }


    @GetMapping("/helloAgain")
    public Object sayHelloAgain(){
        System.out.println("进来了时间戳测试函数");
        try{
            return ZaykTimeStamp.getTimeStampInfo();
        }catch (Exception e){
            return e;
        }

    }




    @PostMapping("/login")
    public Result doLogin(@RequestBody LoginInfoDTO user,HttpServletRequest req) throws CryptoException, UnsupportedEncodingException {

        System.out.println("signature: "+ user.getSignature());

        HttpSession session = req.getSession();
        String gencode = (String) session.getAttribute("index_code");
        if(StringUtils.isEmpty(user.getCode())){
            return Result.ERROR("验证码不能为空");
        }
        System.out.println("验证码： " + gencode);
        if (!gencode.toLowerCase().equals(user.getCode().toLowerCase())){
            return Result.ERROR("验证码错误");
        }

        //验证用户登录签名信息
        if(StringUtils.isEmpty(user.getSignature())){
            return Result.ERROR("签名信息不能为空");
        }


//        String genCaptcha = (String) request.getSession().getAttribute("index_code");
//        String jwtToken = userService.doLogin(user.getUsername(), user.getPassword());// 处理密码
        String jwtToken = userService.doLogin(user);
        if (jwtToken == null) {
            return Result.ERROR("用户名或密码错误!!!!!!");
        }
        System.out.println("用户登录");
        return Result.OK(jwtToken);
    }

    @PostMapping({"/register", "/admin/new"})
    public Result doRegister(@RequestBody User user) {
        switch (userService.doRegister(user)) {
            case 0: return Result.ERROR("注册失败！");
            case 1: return Result.OK();
            case 2: return Result.ERROR("用户已存在！");
            default: return Result.ERROR("服务器内部错误！");
        }
    }
    @GetMapping("/info")
    public Result getInfo(@RequestParam("token") String token) {
        User info = userService.getInfo(token);
        if (info == null) {
            return Result.ERROR("未找到用户!");
        }
        return Result.OK(info);
    }
    @PostMapping("/info")
    public Result updateInfo(@RequestBody User userInfo) {
        if (1 == userService.setInfo(userInfo)) return Result.OK();
        return Result.ERROR("修改失败!");
    }
    @PostMapping("/pwd")
    public Result updatePwd(@RequestBody LoginInfoDTO user) {
        if (1 == userService.setPwd(user)) return Result.OK();
        return Result.ERROR("修改失败!");
    }
    @PostMapping("/ppwd")
    public Result updatePpwd(@RequestBody LoginInfoDTO user) {
        int result = userService.setPpwd(user);
        if(result==1){
           return Result.OK("success");
        }else {
            return Result.ERROR("faile");
        }

    }
    @GetMapping("/code")
    public Result genCode(@RequestParam("id") Long id) {
        int code = userService.genCode(id);
        if (code == 0) {
            return Result.ERROR("服务器内部错误，生成验证码失败！");
        }
        return Result.OK(code); // TODO:实际使用短信发送验证码
    }
    @GetMapping("/verify/code")
    public Result verifyCode(@RequestParam("name") String name, @RequestParam("verifyCode") int verifyCode) {
        return Result.OK(userService.verifyCode(name, verifyCode));
    }
    @PostMapping("/verify/pwd")
    public Result verifyPwd(@RequestBody LoginInfoDTO user) {
        return Result.OK(userService.verifyPwd(user));
    }
    @PostMapping("/logout")
    public Result doLogout() {
        return Result.OK();
    }
    @GetMapping("/admin/list")
    public Result getList(String token){
        List<User> userList = userService.getList(token);
        return Result.OK(userList);
    }
    @PostMapping("/admin/edit")
    public Result editUser(@RequestBody User user) {
        if (userService.editUser(user) == 1) {
            return Result.OK();
        }
        return Result.ERROR("修改用户失败");
    }
    @DeleteMapping("/admin/del")
    public Result delUser(@RequestParam("id") Long id) {
        if (userService.delUser(id) == 1) {
            return Result.OK();
        }
        return Result.ERROR("删除用户失败");
    }
}
