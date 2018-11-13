package com.cg.banking.services;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import org.apache.log4j.Logger;

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
	Transaction transaction = new Transaction();
	private static final Logger logger = Logger.getLogger(BankingServicesImpl.class);
	@Override
	public long openAccount(String accountType, float initBalance)
			throws InvalidAmountException, InvalidAccountTypeException, BankingServicesDownException {
		// TODO Auto-generated method stub
		long accountNo=accountDAO.saveAccountOpen(accountType, initBalance);
		return accountNo;
	}
	@Override
	public float depositAmount(long accountNo, float amount)
			throws AccountNotFoundException, BankingServicesDownException, AccountBlockedException {
		Account account = accountDAO.findAccount(accountNo);
		    if(account==null) throw new AccountNotFoundException("Invalid Account Number or Account Type");
			accountDAO.saveTransaction(amount, "Credit", accountNo);
			float accountBalance= account.getAccountBalance();
			String status = account.getStatus();
			int pinNumber = account.getPinNumber();
			accountBalance=accountBalance+amount;
			//account.setAccountBalance(accountBalance);
			boolean t = accountDAO.updateAccount(accountNo, accountBalance, status, pinNumber);
			if(t==false)  throw new BankingServicesDownException();
		    return accountBalance;
		
	}
	@Override
	public float withdrawAmount(long accountNo, float amount, int pinNumber) throws InsufficientAmountException,
			AccountNotFoundException, InvalidPinNumberException, BankingServicesDownException, AccountBlockedException {
		// TODO Auto-generated method stub
		    Account account = accountDAO.findAccount(accountNo);
		    if(account==null) throw new AccountNotFoundException("Invalid Account Number or Pin Number");
		    float accountBalance = account.getAccountBalance();
		    String status = account.getStatus();
		    if(amount>accountBalance) throw new InsufficientAmountException("Insufficient Balance");
		    if(status=="Deactive") throw new AccountBlockedException("Account Blocked");
			accountDAO.saveTransaction(amount, "Debit", accountNo);
			accountBalance = accountBalance - amount ;
			account.setAccountBalance(accountBalance);
			boolean t = accountDAO.updateAccount(accountNo, accountBalance, status, pinNumber);
			if(t==false) throw new BankingServicesDownException("Service Down");
			return accountBalance;
	}
	@Override
	public boolean fundTransfer(long accountNoTo, long accountNoFrom, float transferAmount, int pinNumber)
			throws InsufficientAmountException, AccountNotFoundException, InvalidPinNumberException,
			BankingServicesDownException, AccountBlockedException {
		// TODO Auto-generated method stub
		Account account1 = accountDAO.findAccount(accountNoFrom);
		Account account2 = accountDAO.findAccount(accountNoTo);
		if(account1==null){
			if(account2==null) throw new AccountNotFoundException("Both Account does not exist.");
			else throw new AccountNotFoundException("Account "+accountNoFrom +" does not exist.");
		}
		else if(account2==null) throw new AccountNotFoundException("Account "+accountNoTo +" does not exist");
			float account1Balance= account1.getAccountBalance();
			float account2Balance= account2.getAccountBalance();
			if(account1Balance<transferAmount) throw new InsufficientAmountException("Not Enough balance");
			String status1 = account1.getStatus();
			String status2 = account2.getStatus();
			if(status1=="Deactive" && status2=="Deactive") throw new AccountBlockedException("Account Blocked");
				accountDAO.saveTransaction(transferAmount, "Debit", accountNoFrom);
				accountDAO.saveTransaction(transferAmount, "Credit", accountNoTo);
				account1Balance-=transferAmount;
				account2Balance+=transferAmount;
				
				int pinNumber2 = account2.getPinNumber();
				boolean t1 = accountDAO.updateAccount(accountNoFrom, account1Balance, status1,pinNumber);
				boolean t2 = accountDAO.updateAccount(accountNoTo, account2Balance, status2, pinNumber2);
				if(t1==false && t2==false) throw new  BankingServicesDownException("Service Down");
				return true;

	}
	@Override
	public Account getAccountDetails(long accountNo) throws AccountNotFoundException, BankingServicesDownException {
		// TODO Auto-generated method stub
		Account account = accountDAO.findAccount(accountNo);
		if(account==null) throw new AccountNotFoundException("Invalid Account Number");
		return account;
	}

	@Override
	public List<Account> getAllAccountDetails() throws BankingServicesDownException {
		// TODO Auto-generated method stub
		return accountDAO.findAllAccount();
		
	}

	@Override
	public List<Transaction> getAccountAllTransaction(long accountNo)
			throws BankingServicesDownException, AccountNotFoundException {
		// TODO Auto-generated method stub
		Account account = accountDAO.findAccount(accountNo);
		if(account==null) throw new AccountNotFoundException("Invalid Account  no");
		return accountDAO.findAllTransaction(accountNo);
	}
	@Override
	public String accountStatus(long accountNo)
			throws BankingServicesDownException, AccountNotFoundException, AccountBlockedException {
		// TODO Auto-generated method stub
		Account account = accountDAO.findAccount(accountNo);
		if(account==null) throw new AccountNotFoundException("Invalid Account  no");
		String status = account.getStatus();
		return status;
	}
}