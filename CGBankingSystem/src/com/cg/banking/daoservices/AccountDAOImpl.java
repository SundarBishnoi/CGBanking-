package com.cg.banking.daoservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cg.banking.beans.Account;
import com.cg.banking.beans.Transaction;
import com.cg.banking.util.ConnectionProvider;


public class AccountDAOImpl implements AccountDAO {
	private Connection conn=ConnectionProvider.getDBConnection();
	@Override
	public long saveAccountOpen(String accountType, float initBalance) {
		// TODO Auto-generated method stub
		try {
			conn.setAutoCommit(false);
			PreparedStatement pstmt= conn.prepareStatement("INSERT INTO accountdetails(accountType,accountBalance,status,accountNo,pinNumber)values(?,?,?,account.nextval,?)");
			pstmt.setString(1, accountType);
			pstmt.setFloat(2,initBalance);
			pstmt.setString(3, "Active");
			pstmt.setInt(4,(int)(Math.random()*9000)+1000);
			pstmt.executeUpdate();
			
			PreparedStatement pstmt2 = conn.prepareStatement("SELECT max(accountNo) from accountdetails");
			ResultSet rs= pstmt2.executeQuery();
			rs.next();
			int accountNo=rs.getInt(1);
			conn.commit();
			return accountNo;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public Account findAccount(long accountNo) {
		// TODO Auto-generated method stub
		try{
			conn.setAutoCommit(false);
			PreparedStatement pstmt1= conn.prepareStatement("SELECT * from accountdetails where accountNo="+accountNo);
			ResultSet rs= pstmt1.executeQuery();
			if(rs.next()){
				       String accountType = rs.getString("accountType");
				       float accountBalance =rs.getFloat("accountBalance");
				       int pinNumber = rs.getInt("pinNumber");
				       String status =rs.getString("status");
						Account account =  new Account(pinNumber, accountType, status, accountBalance, accountNo);
						conn.commit();
						return account;
			}						
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean updateAccount(long accountNo, float accountBalance,String status,int pinNumber){
		try{
			conn.setAutoCommit(false);
			PreparedStatement pstmt1 = conn.prepareStatement("UPDATE accountdetails SET accountBalance=?,status=?,pinNumber=? where accountNo=?");
			pstmt1.setFloat(1, accountBalance);
			pstmt1.setString(2, status);
			pstmt1.setInt(3, pinNumber);
			pstmt1.setLong(4,accountNo);
			pstmt1.executeUpdate();
			conn.commit();
			return true;
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public Transaction saveTransaction(float amount, String transactionType, long accountNo) {
		// TODO Auto-generated method stub
		try{
			conn.setAutoCommit(false);
			PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO Transactions(transactionId,amount,transactionType,accountNo) values(transactionsId.nextval,?,?,?)");
			pstmt1.setFloat(1, amount);
			pstmt1.setString(2, transactionType);
			pstmt1.setLong(3, accountNo);
			pstmt1.executeUpdate();
			
			PreparedStatement pstmt2 = conn.prepareStatement("SELECT max(transactionId) from Transactions");
			ResultSet rs= pstmt2.executeQuery();
			rs.next();
			int transactionId = rs.getInt(1);
			
			
			Transaction transaction = new Transaction(transactionId, amount, transactionType);
			conn.commit();
			return transaction;
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public List<Account> findAllAccount() {
		// TODO Auto-generated method stub
		try{
			conn.setAutoCommit(false);
			PreparedStatement pstmt1= conn.prepareStatement("SELECT * from accountdetails");
			ResultSet rs= pstmt1.executeQuery();
			List<Account> accountList = new ArrayList<Account>();
			while(rs.next()){
				   String accountType = rs.getString("accountType");
			       float accountBalance =rs.getFloat("accountBalance");
			       int pinNumber = rs.getInt("pinNumber");
			       String status =rs.getString("status"); 
			       long accountNo = rs.getLong("accountNo");
			       Account account = new Account(pinNumber, accountType, status, accountBalance, accountNo);
			       accountList.add(account);
			}						
			return accountList;
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public List<Transaction> findAllTransaction(long accountNo) {
		// TODO Auto-generated method stub
		try{
			conn.setAutoCommit(false);
			PreparedStatement pstmt1= conn.prepareStatement("SELECT * from TRANSACTIONS where accountNo="+accountNo);
			ResultSet rs= pstmt1.executeQuery();
			List<Transaction> transactionList = new ArrayList<Transaction>();
			while(rs.next()){
					int transactionId = rs.getInt(1);
					float amount = rs.getFloat(2);
					String transactionType = rs.getString(3);
					Transaction transaction =new Transaction(transactionId, amount, transactionType);
					transactionList.add(transaction);
			}						
			return transactionList;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}