package com.cg.banking.daoservices;

import java.util.List;

import com.cg.banking.beans.Account;
import com.cg.banking.beans.Transaction;

public interface AccountDAO {
	Account saveAccountOpen(Account account);
	Account findAccount(long accountNo);
	boolean updateAccount(Account account);
	Transaction saveTransaction(Transaction transaction);
	List<Account> findAllAccount();
	List<Transaction> findAllTransaction(long accountNo);
}