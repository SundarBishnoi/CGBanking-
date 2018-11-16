package com.cg.banking.beans;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@Entity
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long accountNo;
	private int pinNumber;
	private String accountType,status;
	private float accountBalance;
	@OneToMany
	private  List<Transaction> transaction;
	public Account(int pinNumber, String accountType, String status,
			float accountBalance, long accountNo) {
		super();
		this.pinNumber = pinNumber;
		this.accountType = accountType;
		this.status = status;
		this.accountBalance = accountBalance;
		this.accountNo = accountNo;
	}
	public Account(long accountNo, int pinNumber, String status,
			float accountBalance) {
		super();
		this.accountNo = accountNo;
		this.pinNumber = pinNumber;
		this.status = status;
		this.accountBalance = accountBalance;
	}
	public Account(int pinNumber, String accountType, String status,
			float accountBalance) {
		super();
		this.pinNumber = pinNumber;
		this.accountType = accountType;
		this.status = status;
		this.accountBalance = accountBalance;
	}
	public Account() {
		super();
	}
	public int getPinNumber() {
		return pinNumber;
	}
	public void setPinNumber(int pinNumber) {
		this.pinNumber = pinNumber;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public float getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(float accountBalance) {
		this.accountBalance = accountBalance;
	}
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	@Override
	public String toString() {
		return "Account [pinNumber=" + pinNumber + ", accountType=" + accountType + ", status=" + status
				+ ", accountBalance=" + accountBalance + ", accountNo=" + accountNo + "]";
	}
}