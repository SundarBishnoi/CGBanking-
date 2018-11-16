package com.cg.banking.util;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
public class EntityManagerFactoryProvider {
	private static EntityManagerFactory factory=null;
	public static EntityManagerFactory goEntityManagerFactory(){
		if(factory==null)
			factory=Persistence.createEntityManagerFactory("JPA-PU");
		else 
			System.out.println("Connect");
		return factory;
	}
}
