package com.scraniel.auctioneero;

import java.util.UUID;

import org.hibernate.*;

import com.scraniel.auctioneero.hbm.HibernateContext;
import com.scraniel.auctioneero.tables.User;

public class Bootstrapper 
{

	public static void main(String[] args) 
	{		
		while(!HibernateContext.isInitialized())
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		HibernateContext context = HibernateContext.getInstance();
		
		SessionFactory sessionFactory = context.getSessionFactory();
		
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Integer employeeID = null;
		
		try 
		{
		   tx = session.beginTransaction();
		   User testUser = new User();
		   UUID id = UUID.randomUUID();
		   
		   testUser.setId(id);
		   testUser.setUserName("TESTUSER");
		   session.save(testUser);
		   
		   tx.commit();
		} 
		catch (HibernateException e) 
		{
		   if (tx!=null) tx.rollback();
		   e.printStackTrace(); 
		} 
		finally 
		{
		   session.close(); 
		}		
	}
	

}
