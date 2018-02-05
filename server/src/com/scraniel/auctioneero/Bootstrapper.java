package com.scraniel.auctioneero;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.*;

import com.scraniel.auctioneero.hbm.HibernateContext;
import com.scraniel.auctioneero.tables.User;

public class Bootstrapper 
{

	public static void main(String[] args) 
	{	
		HibernateContext context = HibernateContext.getInstance();
		try
		{
			// Bootstrap hibernate so we can run some queries			
			context.hibernateBootstrap();
			
			AuctionManager manager = new AuctionManager();
			
			// Test adding
			AuctionResponse response =  manager.addUser("Calimbo");
			if(response.isActionSuccess())
			{
				manager.addItem("Cool Item 2", "This item is so, so, SO cool", response.getId(), 32.32f, new Timestamp(100000));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
		finally
		{
			if(context.getSessionFactory().isOpen())
			{
				context.getSessionFactory().close();
			}
		}
	}
	

}
