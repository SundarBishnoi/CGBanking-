package com.cg.banking.main;

import java.util.List;
import java.util.Scanner;

import com.cg.banking.beans.Account;
import com.cg.banking.beans.Transaction;
import com.cg.banking.exceptions.AccountBlockedException;
import com.cg.banking.exceptions.AccountNotFoundException;
import com.cg.banking.exceptions.BankingServicesDownException;
import com.cg.banking.exceptions.InsufficientAmountException;
import com.cg.banking.exceptions.InvalidAccountTypeException;
import com.cg.banking.exceptions.InvalidAmountException;
import com.cg.banking.exceptions.InvalidPinNumberException;
import com.cg.banking.services.BankingServices;
import com.cg.banking.services.BankingServicesImpl;

public class MainClass {
	public static void main(String[] args) throws InvalidAmountException, InvalidAccountTypeException, BankingServicesDownException, AccountNotFoundException, AccountBlockedException, InsufficientAmountException, InvalidPinNumberException {
		System.out.println("1.Open Account");
		System.out.println("2.Deposit Amount");
		System.out.println("3.Withdraw");
		System.out.println("4.Fund Transfer");
		System.out.println("5.Get Your Account Details");
		System.out.println("6.Get All Account Details");
		System.out.println("8.Get Account Status.");
		
		Scanner obj = new Scanner(System.in);
		int var = obj.nextInt();
		BankingServices bankingServices = new BankingServicesImpl();
		Account account = new Account();
		Transaction transaction = new Transaction();
		if(var==1) {
			System.out.println("Enter Account Type");
			String accountType = obj.next();
			account.setAccountType(accountType);
			System.out.println("Enter Initial Balance");
			float accountBalance = obj.nextFloat();
			account.setAccountBalance(accountBalance);
			long accountNo = bankingServices.openAccount(accountType, accountBalance);
			System.out.println("Account Successfully created. Account Number :"+ accountNo);	
		}
		else if(var==2) {
		    //transaction.setTransactionType("Credit");
			System.out.println("Enter Account Number");
			long accountNo = obj.nextLong();
			System.out.println("Enter amount to be deposited.");
			float amount = obj.nextFloat();
			float accountBalance = bankingServices.depositAmount(accountNo, amount);
			System.out.println("Amount deposited successfully. Your current balance "+ accountBalance);
		}
		else if(var==3) {
			System.out.println("Enter Account Number");
			long accountNo = obj.nextLong();
			System.out.println("Enter Pin Number");
			int pinNumber = obj.nextInt();
			System.out.println("Enter amount to be withdrwan.");
			float amount = obj.nextFloat();
			float accountBalance = bankingServices.withdrawAmount(accountNo, amount, pinNumber);
			System.out.println("Account withdrawn succesfully. Your current balance "+accountBalance);
		}
		else if(var==4) {
			System.out.println("Enter Your Account No");
			long accountNoFrom = obj.nextLong();
			System.out.println("Enter Account No to transfer ");
			long accountNoTo = obj.nextLong();
			System.out.println("Enter Amount");
			float transferAmount = obj.nextFloat();
			System.out.println("Enter your pin");
			int pinNumber = obj.nextInt();
			boolean t = bankingServices.fundTransfer(accountNoTo, accountNoFrom, transferAmount, pinNumber);
			if(t) System.out.println("Fund transfered Succesfully.");
		}
		else if(var==5) {
			System.out.println("Enter Account Number");
			long accountNo = obj.nextLong();
			account = bankingServices.getAccountDetails(accountNo);
			System.out.println("Your accountNumber-"+accountNo+" accountType-"+account.getAccountType()+" pinNumber-"+account.getPinNumber()+" accountStatus-"+account.getStatus()+" accountBalance "+ account.getAccountBalance());
		}
		else if(var==6) {
			List<Account> accountList = bankingServices.getAllAccountDetails();
			
			for (Account account2 : accountList) {
			System.out.println(account2.toString());
			}
		}
		else if(var==8) {
			System.out.println("Enter Account Number");
			long accountNo = obj.nextLong();
			String accountStatus = bankingServices.accountStatus(accountNo);
			System.out.println("Account Status: "+ accountStatus);
		}
	}
}