package com.cg.banking.services;
import java.sql.SQLException;
import java.util.List;


import com.cg.banking.beans.Account;
import com.cg.banking.beans.Transaction;
import com.cg.banking.daoservices.AccountDAO;
import com.cg.banking.daoservices.AccountDAOImpl;
import com.cg.banking.exceptions.AccountBlockedException;
import com.cg.banking.exceptions.AccountNotFoundException;
import com.cg.banking.exceptions.BankingServicesDownException;
import com.cg.banking.exceptions.InsufficientAmountException;
import com.cg.banking.exceptions.InvalidAccountTypeException;
import com.cg.banking.exceptions.InvalidAmountException;
import com.cg.banking.exceptions.InvalidPinNumberException;

public class BankingServicesImpl implements BankingServices {
	Account account = new Account();
	AccountDAO accountDAO = new AccountDAOImpl();
	@Override
	public long openAccount(String accountType, float initBalance)
			throws InvalidAmountException, InvalidAccountTypeException, BankingServicesDownException {
		// TODO Auto-generated method stub
		long accountId=accountDAO.saveAccountOpen(accountType, initBalance);
		
		return accountId;
	}
	
	@Override
	public float depositAmount(long accountNo, float amount)
			throws AccountNotFoundException, BankingServicesDownException, AccountBlockedException {
		Account account = accountDAO.findAccount(accountNo);
		if(account!=null){
			float accountBalance= account.getAccountBalance();
			String status = account.getStatus();
			int pinNumber = account.getPinNumber();
			accountBalance=accountBalance+amount;
			account.setAccountBalance(accountBalance);
			boolean t = accountDAO.updateAccount(accountNo, accountBalance, status, pinNumber);
			if(t) return accountBalance;
		}
		return 0;
	}

	@Override
	public float withdrawAmount(long accountNo, float amount, int pinNumber) throws InsufficientAmountException,
			AccountNotFoundException, InvalidPinNumberException, BankingServicesDownException, AccountBlockedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean fundTransfer(long accountNoTo, long accountNoFrom, float transferAmount, int pinNumber)
			throws InsufficientAmountException, AccountNotFoundException, InvalidPinNumberException,
			BankingServicesDownException, AccountBlockedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getAccountDetails(long accountNo) throws AccountNotFoundException, BankingServicesDownException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> getAllAccountDetails() throws BankingServicesDownException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getAccountAllTransaction(long accountNo)
			throws BankingServicesDownException, AccountNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String accountStatus(long accountNo)
			throws BankingServicesDownException, AccountNotFoundException, AccountBlockedException {
		// TODO Auto-generated method stub
		return null;
	}

}
