package com.cg.banking.daoservices;

import java.util.List;

import com.cg.banking.beans.Account;
import com.cg.banking.beans.Transaction;

public interface AccountDAO {
	long saveAccountOpen(String accountType, float initBalance);
	Account findAccount(long accountNo);
	boolean updateAccount(long accountNo, float accountBalance,String status, int pinNumber);
	Transaction saveTransaction(float amount,String transactionType, long accountNo);
	List<Account> findAllAccount();
}