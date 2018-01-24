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
		// Bootstrap hibernate so we can run some queries
		HibernateContext context = HibernateContext.getInstance();
		context.hibernateBootstrap();
		
		AuctionManager manager = new AuctionManager();
		
		// Test adding
		AuctionResponse response =  manager.addUser("Jimbo");
		if(response.isActionSuccess())
		{
			manager.addItem("Cool Item", "This item is so, so cool", response.getId(), 27.32f, new Timestamp(100000));
		}
	}
	

}
