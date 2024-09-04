package com.xidian.bankdemo.service;

import com.xidian.bankdemo.dto.ListDTO;
import com.xidian.bankdemo.dto.QueryDTO;
import com.xidian.bankdemo.dto.TransactionDTO;
import com.xidian.bankdemo.entity.Account;
import com.xidian.bankdemo.entity.Friend;
import net.olymtech.javakgc.base.utils.exception.CryptoException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;

public interface AccountService {
    List<Account> getList(Long ownerId);
    List<Account> getAll();
    List<Friend> getFriendList(Long ownerId);
    int addAccount(Account account);
    int editAccount(Account account);
    int delAccount(Long id);
    int doWithdrawal(TransactionDTO transactionDTO);
    HashMap<String, Object> doTransaction(TransactionDTO transactionDTO) throws NoSuchAlgorithmException, InvalidKeySpecException, CryptoException;
    ListDTO getStatementList(QueryDTO query);
    ListDTO getTransactionList(QueryDTO query);
}
