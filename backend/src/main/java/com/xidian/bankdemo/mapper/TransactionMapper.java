package com.xidian.bankdemo.mapper;

import com.xidian.bankdemo.dto.QueryDTO;
import com.xidian.bankdemo.dto.StatementDTO;
import com.xidian.bankdemo.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransactionMapper {
    List<Transaction> findByQuery(@Param("query") QueryDTO query);
    List<StatementDTO> findIncomeByQuery(@Param("query") QueryDTO query);
    List<StatementDTO> findOutcomeByQuery(@Param("query") QueryDTO query);
    int insertTransaction(Transaction transaction);
}
