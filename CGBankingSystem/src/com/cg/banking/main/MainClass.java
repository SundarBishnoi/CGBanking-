package com.cg.banking.main;

import java.util.Scanner;

import com.cg.banking.beans.Account;
import com.cg.banking.beans.Transaction;
import com.cg.banking.exceptions.AccountBlockedException;
import com.cg.banking.exceptions.AccountNotFoundException;
import com.cg.banking.exceptions.BankingServicesDownException;
import com.cg.banking.exceptions.InvalidAccountTypeException;
import com.cg.banking.exceptions.InvalidAmountException;
import com.cg.banking.services.BankingServices;
import com.cg.banking.services.BankingServicesImpl;

public class MainClass {
	public static void main(String[] args) throws InvalidAmountException, InvalidAccountTypeException, BankingServicesDownException, AccountNotFoundException, AccountBlockedException {
		System.out.println("1.Open Account");
		System.out.println("2.Deposit Amount");
		Scanner obj = new Scanner(System.in);
		int var = obj.nextInt();
		BankingServices bankingservices = new BankingServicesImpl();
		Account account = new Account();
		Transaction transaction = new Transaction();
		if(var==1) {
			System.out.println("Enter Account Type");
			String accountType = obj.next();
			account.setAccountType(accountType);
			System.out.println("Enter Initial Balance");
			float accountBalance = obj.nextFloat();
			account.setAccountBalance(accountBalance);
			long accountNo = bankingservices.openAccount(accountType, accountBalance);
			System.out.println("Account Successfully created. Account Number :"+ accountNo);	
		}
		else if(var==2) {
		    //transaction.setTransactionType("deposit");
			System.out.println("Enter Account Number");
			long accountNo = obj.nextLong();
			System.out.println("Enter amount to be deposited.");
			float amount = obj.nextFloat();
			float accountBalance = bankingservices.depositAmount(accountNo, amount);
			System.out.println("Amount deposited successfully. Your current balance "+ accountBalance);
		}
	}
}