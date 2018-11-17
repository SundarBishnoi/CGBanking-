package com.cg.banking.services;
import java.sql.SQLException;
import java.util.ArrayList;
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
	Transaction transaction = new Transaction();

	@Override
	public long openAccount(String accountType, float initBalance)
			throws InvalidAmountException, InvalidAccountTypeException, BankingServicesDownException {
		float accountBalance = initBalance;
		String status = "Active";
		int pinNumber=(int)(Math.random()*9000)+1000;
		Account account =new Account(pinNumber, accountType, status, accountBalance);
		Account account1 =accountDAO.saveAccountOpen(account);
		Transaction transaction = new Transaction(accountBalance, "Credit", account1);
		accountDAO.saveTransaction(transaction);
		return account1.getAccountNo();
	}
	@Override
	public float depositAmount(long accountNo, float amount)
			throws AccountNotFoundException, BankingServicesDownException, AccountBlockedException {
		    Account account = accountDAO.findAccount(accountNo);
		    if(account==null) throw new AccountNotFoundException("Invalid Account Number or Account Type");		   
		    Transaction transaction1 = new Transaction(amount, "Credit", account);
			accountDAO.saveTransaction(transaction1);
			float accountBalance= account.getAccountBalance();
			String status = account.getStatus();
			int pinNumber = account.getPinNumber();
			accountBalance=accountBalance+amount;
			//account.setAccountBalance(accountBalance);
			Account account1 = new Account(accountNo, pinNumber, status, accountBalance);
			boolean t = accountDAO.updateAccount(account1);
			if(t==false)  throw new BankingServicesDownException();
		    return accountBalance;
	}
	@Override
	        public float withdrawAmount(long accountNo, float amount, int pinNumber) throws InsufficientAmountException,
			AccountNotFoundException, InvalidPinNumberException, BankingServicesDownException, AccountBlockedException {
		    Account account = accountDAO.findAccount(accountNo);
		    if(account==null) throw new AccountNotFoundException("Invalid Account Number or Pin Number");
		    float accountBalance = account.getAccountBalance();
		    String status = account.getStatus();
		    if(amount>accountBalance) throw new InsufficientAmountException("Insufficient Balance");
		    if(status=="Deactive") throw new AccountBlockedException("Account Blocked");
		    Transaction transaction = new Transaction(amount,"Debit", account);
			accountDAO.saveTransaction(transaction);
			accountBalance = accountBalance - amount ;
			account.setAccountBalance(accountBalance);
			Account account1 = new Account(accountNo, pinNumber, status, accountBalance);
			boolean t = accountDAO.updateAccount(account1);
			if(t==false) throw new BankingServicesDownException("Service Down");
			return accountBalance;
	}
	@Override
	public boolean fundTransfer(long accountNoTo, long accountNoFrom, float transferAmount, int pinNumber)
			throws InsufficientAmountException, AccountNotFoundException, InvalidPinNumberException,
			BankingServicesDownException, AccountBlockedException {
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
				Transaction transaction1 = new Transaction(transferAmount, "Debit", account1);
				Transaction transaction2 = new Transaction(transferAmount, "Credit", account2);
				accountDAO.saveTransaction(transaction1);
				accountDAO.saveTransaction(transaction2);
				account1Balance-=transferAmount;
				account2Balance+=transferAmount;
				
				int pinNumber2 = account2.getPinNumber();
				Account account3 = new Account(accountNoFrom, pinNumber, status1, account1Balance);
				Account account4 = new Account(accountNoTo, pinNumber2, status2, account2Balance);
				boolean t1 = accountDAO.updateAccount(account3);
				boolean t2 = accountDAO.updateAccount(account4);
				if(t1==false && t2==false) throw new  BankingServicesDownException("Service Down");
				return true;

	}
	@Override
	public Account getAccountDetails(long accountNo) throws AccountNotFoundException, BankingServicesDownException {
		Account account = accountDAO.findAccount(accountNo);
		if(account==null) throw new AccountNotFoundException("Invalid Account Number");
		return account;
	}

	@Override
	public List<Account> getAllAccountDetails() throws BankingServicesDownException {
		return accountDAO.findAllAccount();
	}

	@Override
	public List<Transaction> getAccountAllTransaction(long accountNo)
			throws BankingServicesDownException, AccountNotFoundException {
		Account account = accountDAO.findAccount(accountNo);
		if(account==null) throw new AccountNotFoundException("Invalid Account  no");
		return accountDAO.findAllTransaction(accountNo);
	}
	@Override
	public String accountStatus(long accountNo)
			throws BankingServicesDownException, AccountNotFoundException, AccountBlockedException {
		Account account = accountDAO.findAccount(accountNo);
		if(account==null) throw new AccountNotFoundException("Invalid Account  no");
		String status = account.getStatus();
		return status;
	}
}