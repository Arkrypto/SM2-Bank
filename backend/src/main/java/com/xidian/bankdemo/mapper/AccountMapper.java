package com.xidian.bankdemo.mapper;

import com.xidian.bankdemo.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface AccountMapper {
    boolean existsById(@Param("id") Long id);
    List<Account> findByOwnerId(@Param("ownerId") Long ownerId);
    List<Account> findAll();
    int insertAccount(Account account);
    int updateAccount(Account account);
    int deleteAccount(@Param("id") Long id);
    int updateBalanceById(@Param("id")Long id, @Param("balance")BigDecimal balance);
}
