package com.cg.banking.daoservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.cg.banking.beans.Account;
import com.cg.banking.beans.Transaction;
import com.cg.banking.util.EntityManagerFactoryProvider;


public class AccountDAOImpl implements AccountDAO {
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA-PU");
	@Override
	public Account saveAccountOpen(Account account) {
		EntityManager em =emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(account);																				//Data to be saved into the table of database..
		
		em.getTransaction().commit();
		em.close();
		return account;
		}
	@Override
	public Account findAccount(long accountNo) {
		// TODO Auto-generated method stub
		EntityManager em =emf.createEntityManager();
		return em.find(Account.class, accountNo);
	}

	@Override
	public boolean updateAccount(Account account){
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Account account1 = em.find(Account.class, account.getAccountNo());
		/*account.setAccountBalance(accountBalance);
		account.setStatus(status);
		account.setPinNumber(pinNumber);*/
		account1.setAccountBalance(account.getAccountBalance());
		account1.setStatus(account.getStatus());
		em.getTransaction().commit();
		em.close();
		return true;
	}
	
	@Override
	public Transaction saveTransaction(Transaction transaction) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(transaction);
		em.getTransaction().commit();
		em.close();
		return transaction ;
	}
	@Override
	public List<Account> findAllAccount() {
		// TODO Auto-generated method stub
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		List<Account> accountDetails = em.createQuery("SELECT p FROM Account p").getResultList();
	    em.getTransaction().commit();
		return accountDetails;
	}
	@Override
	public List<Transaction> findAllTransaction(long accountNo) {
		EntityManager em =emf.createEntityManager();
		em.getTransaction().begin();
		List<Transaction> transactions = em.createQuery("SELECT t FROM Transaction t  WHERE account.accountNo="+accountNo).getResultList();
		em.getTransaction().commit();
		return transactions;
	}
}