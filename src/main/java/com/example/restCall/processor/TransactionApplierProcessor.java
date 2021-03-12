package com.example.restCall.processor;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import com.example.restCall.dao.TransactionDao;
import com.example.restCall.model.AccountSummary;
import com.example.restCall.model.Transaction;

public class TransactionApplierProcessor implements ItemProcessor<AccountSummary, AccountSummary> {
	
	private TransactionDao transactionDao;

	public TransactionApplierProcessor(TransactionDao transactionDao) {
		this.transactionDao = transactionDao;
	}

	public AccountSummary process(AccountSummary summary) throws Exception {
		List<Transaction> transactions = transactionDao.
				getTransactionsByAccountNumber(summary.getAccountNumber());
		for (Transaction transaction : transactions) {
			summary.setCurrentBalance(summary.getCurrentBalance() + transaction.getAmount());
		}
		return summary;
	}
}
