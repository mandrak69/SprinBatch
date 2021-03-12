package com.example.restCall.dao;

import java.util.List;

import com.example.restCall.model.Transaction;

public interface TransactionDao {
	 List<Transaction> getTransactionsByAccountNumber(String accountNumber);
}
