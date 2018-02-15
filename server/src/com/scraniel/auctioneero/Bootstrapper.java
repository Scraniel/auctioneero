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
			UUID ownerId = response.getId();
			response =  manager.addUser("Jimmy");
			UUID bidderId = response.getId();
			if(response.isActionSuccess())
			{
				response = manager.addItem("Cool Item 3", "This item is neat", ownerId, 33.32f, new Timestamp(System.currentTimeMillis() + 100000));
				
				if(response.isActionSuccess())
				{
					response = manager.bidOnItem(response.getId(), bidderId, 30);
					
					System.out.println(response.getOutputMessage());
					
					response = manager.cancelAuction(response.getId());
					
					System.out.println(response.getOutputMessage());
				}
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
