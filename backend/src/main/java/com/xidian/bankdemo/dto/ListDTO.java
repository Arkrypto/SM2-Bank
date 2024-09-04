package com.xidian.bankdemo.dto;

import com.xidian.bankdemo.entity.Transaction;

import java.util.List;

public class ListDTO { // hint: 可不使用此类
    private Integer total;
    private List<StatementDTO> statementList;
    private List<Transaction> transactionList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<StatementDTO> getStatementList() {
        return statementList;
    }

    public void setStatementList(List<StatementDTO> statementList) {
        this.statementList = statementList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
