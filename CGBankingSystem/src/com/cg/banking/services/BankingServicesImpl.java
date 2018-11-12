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
		// TODO Auto-generated method stub
		long accountNo=accountDAO.saveAccountOpen(accountType, initBalance);
		return accountNo;
	}
	@Override
	public float depositAmount(long accountNo, float amount)
			throws AccountNotFoundException, BankingServicesDownException, AccountBlockedException {
		Account account = accountDAO.findAccount(accountNo);
		
		if(account!=null){
			accountDAO.saveTransaction(amount, "Credit", accountNo);
			float accountBalance= account.getAccountBalance();
			String status = account.getStatus();
			int pinNumber = account.getPinNumber();
			accountBalance=accountBalance+amount;
			//account.setAccountBalance(accountBalance);
			boolean t = accountDAO.updateAccount(accountNo, accountBalance, status, pinNumber);
			
			if(t) return accountBalance;
		}
		return 0;
	}
	@Override
	public float withdrawAmount(long accountNo, float amount, int pinNumber) throws InsufficientAmountException,
			AccountNotFoundException, InvalidPinNumberException, BankingServicesDownException, AccountBlockedException {
		// TODO Auto-generated method stub
		Account account = accountDAO.findAccount(accountNo);
		if(account!=null) {
			accountDAO.saveTransaction(amount, "Debit", accountNo);
			float accountBalance = account.getAccountBalance();
			String status = account.getStatus();
			if(amount<=accountBalance) {
				accountBalance = accountBalance - amount ;
				account.setAccountBalance(accountBalance);
				boolean t = accountDAO.updateAccount(accountNo, accountBalance, status, pinNumber);
				if(t) return accountBalance;
			}
		}
		return 0;
	}

	@Override
	public boolean fundTransfer(long accountNoTo, long accountNoFrom, float transferAmount, int pinNumber)
			throws InsufficientAmountException, AccountNotFoundException, InvalidPinNumberException,
			BankingServicesDownException, AccountBlockedException {
		// TODO Auto-generated method stub
		Account account1 = accountDAO.findAccount(accountNoFrom);
		Account account2 = accountDAO.findAccount(accountNoTo);
		if((account1!=null)&&(account2!=null)) {
			float account1Balance= account1.getAccountBalance();
			float account2Balance= account2.getAccountBalance();
			if(transferAmount<=account1Balance) {
				accountDAO.saveTransaction(transferAmount, "Debit", accountNoFrom);
				accountDAO.saveTransaction(transferAmount, "Credit", accountNoTo);
				account1Balance-=transferAmount;
				account2Balance+=transferAmount;
				String status1 =account1.getStatus();
				String status2 = account2.getStatus();
				int pinNumber2 = account2.getPinNumber();
				boolean t1 = accountDAO.updateAccount(accountNoFrom, account1Balance, status1,pinNumber);
				boolean t2 = accountDAO.updateAccount(accountNoTo, account2Balance, status2, pinNumber2);
				if(t1 && t2) return true;
			}		
		}
		return false;
	}

	@Override
	public Account getAccountDetails(long accountNo) throws AccountNotFoundException, BankingServicesDownException {
		// TODO Auto-generated method stub
		Account account = accountDAO.findAccount(accountNo);
		
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
		if(account!=null){
			return accountDAO.findAllTransaction(accountNo);
		}
		return null;
		
	}

	@Override
	public String accountStatus(long accountNo)
			throws BankingServicesDownException, AccountNotFoundException, AccountBlockedException {
		// TODO Auto-generated method stub
		Account account = accountDAO.findAccount(accountNo);
		String status = account.getStatus();
		return status;
	}
}