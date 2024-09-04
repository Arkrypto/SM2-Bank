package com.xidian.bankdemo.service.impl;

import com.xidian.bankdemo.common.TimeStampInfo;
import com.xidian.bankdemo.dto.ListDTO;
import com.xidian.bankdemo.dto.QueryDTO;
import com.xidian.bankdemo.dto.StatementDTO;
import com.xidian.bankdemo.dto.TransactionDTO;
import com.xidian.bankdemo.entity.*;
import com.xidian.bankdemo.mapper.AccountMapper;
import com.xidian.bankdemo.mapper.FriendMapper;
import com.xidian.bankdemo.mapper.TransactionMapper;
import com.xidian.bankdemo.mapper.UserMapper;
import com.xidian.bankdemo.security.signverify.OlymSignature;
import com.xidian.bankdemo.security.XCiphersm;
import com.xidian.bankdemo.security.timestamp.ZaykTimeStamp;
import com.xidian.bankdemo.service.AccountService;
import com.xidian.bankdemo.service.LogService;
import com.xidian.bankdemo.util.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import net.olymtech.javakgc.base.utils.exception.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private LogService logService;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<Account> getList(Long ownerId) {
        return accountMapper.findByOwnerId(ownerId);
    }

    @Override
    public List<Account> getAll() {
        return accountMapper.findAll();
    }

    @Override
    public List<Friend> getFriendList(Long ownerId) {
        return friendMapper.findByOwnerId(ownerId);
    }

    @Override
    public int addAccount(Account account) {
        Date time = ZaykTimeStamp.getTimeStampInfo().getTime();
        account.setGmtCreate(time);
        account.setGmtModified(time);
        if (account.getId() == null) return -1;
        if (accountMapper.existsById(account.getId())) return 0;
        int result = accountMapper.insertAccount(account);
        logService.addLog(new Log(LogService.CREATE, "账户:" + account.getId(), result, "管理员添加账户"));
        return result;
    }

    @Override
    public int editAccount(Account account) {
        int result = accountMapper.updateAccount(account);
        Date time = ZaykTimeStamp.getTimeStampInfo().getTime();
        account.setGmtModified(time);
        logService.addLog(new Log(LogService.UPDATE, "账户:" + account.getId(), result, "管理员修改账户"));
        return result;
    }

    @Override
    public int delAccount(Long id) { // fixme： 同时删除与账号相关的交易记录
        int result = accountMapper.deleteAccount(id);
        logService.addLog(new Log(LogService.DELETE, "账户:" + id, result, "管理员删除账户"));
        return result;
    }

    @Deprecated
    @Transactional
    @Override
    public int doWithdrawal(TransactionDTO transactionDTO) {
        int r = accountMapper.updateBalanceById(transactionDTO.getMyAccount(), transactionDTO.getAmount().negate());
        log.info(transactionDTO.toString());
        Transaction transaction = new Transaction(transactionDTO.getMyAccount(),
                0L,
                transactionDTO.getName(),
                "外部账户",
                transactionDTO.getAmount(),
                "用户取款",
                r == 1 ? 0 : 1);
        return transactionMapper.insertTransaction(transaction);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public HashMap<String, Object> doTransaction(TransactionDTO transactionDTO) throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.debug(transactionDTO.toString());

        boolean flag = false;
        boolean flagPpw = false;
        int result = 0;
        String iniData = transactionDTO.getIniData();
        String signature = transactionDTO.getSignature();
        String name = transactionDTO.getName();
        User dbUser = userMapper.findByName(name);
        String cert = dbUser.getCertificate();
        String payPassword = dbUser.getPayPassword();
        System.out.println("iniData: " + iniData);
        System.out.println("signature: " + signature);
        System.out.println("cert:" + cert);
        System.out.println("payPassword: "+ payPassword);
        flag = OlymSignature.verifySignature(cert, iniData, signature);
        System.out.println(transactionDTO.getPayPassword());
        flagPpw = passwordEncoder.matches(transactionDTO.getPayPassword(),payPassword);
        System.out.println("flagPpw: "+ flagPpw);
        if (flag && flagPpw) {

            /**
             * 下面的代码为交易处理
             */
            Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
            int r1 = accountMapper.updateBalanceById(transactionDTO.getMyAccount(), transactionDTO.getAmount().negate());
            int r2 = accountMapper.updateBalanceById(transactionDTO.getToAccount(), transactionDTO.getAmount());
            result = r1 + r2;
            log.info("{}=>取出{}", transactionDTO.getMyAccount(), r1 == 1 ? "成功" : "失败");
            log.info("{}<=存入{}", transactionDTO.getToAccount(), r2 == 1 ? "成功" : "失败");
            if (result == 2) {
                result = 1;// 成功
                if (!friendMapper.existsByAccount(transactionDTO.getToAccount(), transactionDTO.getId())) {
                    Friend friend = new Friend(transactionDTO.getToAccount(),
                            transactionDTO.getToUsername(),
                            transactionDTO.getToBankName(),
                            transactionDTO.getId());
                    friendMapper.insertFriend(friend);
                }
            } else {
                // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//全部回滚
                TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);//回滚到保存点
                log.warn("用户转账: 事务回滚");
                result = 0;// 失败
            }
        }


        /**
         *
         * 下面的代码为服务端对交易结果进行响应
         *
         */

        HashMap<String, Object> info = new HashMap<>();
        String message = null;
        try {
            message = XCiphersm.sm3_Hash(transactionDTO.toString());
        } catch (CryptoException e) {
            System.out.println("Sm3_Hash: " + e.getMessage());
        }
        TimeStampInfo timeStampInfo = ZaykTimeStamp.getTimeStampInfo(message);
        Date time = timeStampInfo.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatTime = simpleDateFormat.format(time);
        if (result == 1 && flag && flagPpw) {

            info.put("name", transactionDTO.getName());
            info.put("account", CodeUtils.idCardMark(transactionDTO.getMyAccount() + ""));
            info.put("toName", transactionDTO.getToUsername());
            info.put("toAccount", CodeUtils.idCardMark(transactionDTO.getToAccount() + ""));
            info.put("amount", transactionDTO.getAmount());
            info.put("fee", transactionDTO.getFee());
            info.put("time", formatTime);
            info.put("toBank", transactionDTO.getToBankName());
            info.put("serverSignature", OlymSignature.generateSignature(info));
            info.put("timeStamp", timeStampInfo.getTimeStamp());
//            System.out.println("info:   " + info);
        } else if (!flag) {
            info.put("time", formatTime);
            info.put("reason", "交易验签失败！");
            info.put("serverSignature", OlymSignature.generateSignature(info));
            info.put("timeStamp", timeStampInfo.getTimeStamp());
//            System.out.println("info:   " + info);
        }else if(!flagPpw)
        {
            info.put("time", formatTime);
            info.put("reason", "支付密码错误！");
            info.put("serverSignature", OlymSignature.generateSignature(info));
            info.put("timeStamp", timeStampInfo.getTimeStamp());
//            System.out.println("info:   " + info);
        }
        else {
            info.put("time", formatTime);
            info.put("reason", "服务器内部错误");
            info.put("serverSignature", OlymSignature.generateSignature(info));
            info.put("timeStamp", timeStampInfo.getTimeStamp());
            System.out.println("info:   " + info);

        }

        Transaction transaction = new Transaction(transactionDTO.getMyAccount(),
                transactionDTO.getToAccount(),
                transactionDTO.getName(),
                transactionDTO.getToUsername(),
                transactionDTO.getAmount(),
                transactionDTO.getDescription(),
                result);
        transaction.setGmtCreate(time);
        transaction.setGmtModified(time);
        transaction.setSignature(transactionDTO.getSignature());
        transaction.setTimeStamp(timeStampInfo.getTimeStamp());
        transaction.setServer_signature((String) info.get("serverSignature"));

        int ret = transactionMapper.insertTransaction(transaction);
        if (ret != 1) {
            log.info("向数据库中插入交易信息失败");
        }
        logService.addLog(new Log(LogService.CREATE, "转账记录", result, "用户转账"));
        return info;
    }

    @Override
    public ListDTO getStatementList(QueryDTO query) {
        log.debug(query.toString());
        ListDTO list = new ListDTO();
        if (query.getMyAccount() == null) {
            list.setTotal(-1);
            return list;
        }
        List<StatementDTO> statementList = transactionMapper.findIncomeByQuery(query);
        statementList.addAll(transactionMapper.findOutcomeByQuery(query));
        list.setStatementList(statementList);// fixme: sql的时间比较
        list.setTotal(list.getStatementList().size());

        return list;
    }

    @Override
    public ListDTO getTransactionList(QueryDTO query) {
        log.debug(query.toString());
        ListDTO list = new ListDTO();

        List<Transaction> transactionList = transactionMapper.findByQuery(query);
        list.setTransactionList(transactionList);// fixme: sql的时间比较
        list.setTotal(transactionList.size());

        return list;
    }
}
