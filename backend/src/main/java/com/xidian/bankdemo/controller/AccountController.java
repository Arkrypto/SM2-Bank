package com.xidian.bankdemo.controller;

import com.xidian.bankdemo.common.Result;
import com.xidian.bankdemo.dto.ListDTO;
import com.xidian.bankdemo.dto.QueryDTO;
import com.xidian.bankdemo.dto.TransactionDTO;
import com.xidian.bankdemo.entity.Account;
import com.xidian.bankdemo.entity.Friend;
import com.xidian.bankdemo.service.AccountService;
import net.olymtech.javakgc.base.utils.exception.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/list")
    public Result getList(@RequestParam("id") Long ownerId){
        List<Account> accountList = accountService.getList(ownerId);
        return Result.OK(accountList);
    }
    @GetMapping("/admin/list")
    public Result getAll(){
        List<Account> accountList = accountService.getAll();
        return Result.OK(accountList);
    }
    @GetMapping("/friend")
    public Result getFriendList(@RequestParam("id") Long ownerId){
        List<Friend> friendList = accountService.getFriendList(ownerId);
        return Result.OK(friendList);
    }

    @PostMapping("/admin/new")
    public Result addAccount(@RequestBody Account account) {
        switch (accountService.addAccount(account)) {
            case -1: return Result.ERROR("账号不能为空！");
            case 0: return Result.ERROR("账号已存在！");
            case 1: return Result.OK();
            default: return Result.ERROR("添加账户失败");
        }
    }
    @PostMapping("/admin/edit")
    public Result editAccount(@RequestBody Account account) {
        if (accountService.editAccount(account) == 1) {
            return Result.OK();
        }
        return Result.ERROR("修改账户失败");
    }
    @DeleteMapping("/admin/del")
    public Result delAccount(@RequestParam("id") Long id) {
        if (accountService.delAccount(id) == 1) {
            return Result.OK();
        }
        return Result.ERROR("删除账户失败");
    }

    @Deprecated
    @PostMapping("/withdrawal")
    public Result doWithdrawal(@RequestBody TransactionDTO transactionDTO) {
        int result = accountService.doWithdrawal(transactionDTO);
        if (result == 1) return Result.OK();
        return Result.ERROR("取款失败");
    }
    @PostMapping("/transaction")
    public Result doTransaction(@RequestBody TransactionDTO transactionDTO) throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println( "signature:    "+transactionDTO.getSignature());
        HashMap<String, Object> map = null;
        try {
            map = accountService.doTransaction(transactionDTO);
        } catch (CryptoException e) {
            e.printStackTrace();
        }
        if (!map.containsKey("reason")) {
           return Result.OK(map);
        }
        return Result.ERROR(map);
    }
    @GetMapping("/statement")
    public Result getStatementList(QueryDTO query) {
        ListDTO list = accountService.getStatementList(query);
        if (list.getTotal()<0) return Result.ERROR("未选择账户");
        return Result.OK(list);
    }
    @GetMapping("/admin/statement")
    public Result getTransactionList(QueryDTO query) {
        return Result.OK(accountService.getTransactionList(query));
    }
}
