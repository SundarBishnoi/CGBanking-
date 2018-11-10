package com.cg.banking.daoservices;

import com.cg.banking.beans.Account;

public interface AccountDAO {
	long saveAccountOpen(String accountType, float initBalance);
	Account findAccount(long accountNo);
	boolean updateAccount(long accountNo, float accountBalance,String status, int pinNumber);
	
}
