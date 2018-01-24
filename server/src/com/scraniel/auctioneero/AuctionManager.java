package com.scraniel.auctioneero;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.*;
import org.hibernate.cfg.NotYetImplementedException;

import com.scraniel.auctioneero.hbm.*;
import com.scraniel.auctioneero.tables.*;

// How the user interacts with various auction objects
public class AuctionManager 
{
	
	// Attempts to add a new bid on an item.
	// IDEA: * Lock the item row
	//		 * Check the price, abort if too high
	//		 * Update the item
	//		 * Unlock the row
	// This will be implemented once we have MySQL.
	// Should be safe against SQL injection as we force typing
	public AuctionResponse bidOnItem(UUID itemId, UUID bidderId, float bid)
	{		
		int currentBid = 0;
		String message = null;
		boolean isSuccessfulBid = true;
		Timestamp expiry = null;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		// 1. Lock the item row
		// BEGIN;
		// SELECT * FROM items WHERE item_id = '{itemId}' FOR UPDATE;
		//
		
		// 2. Check the expiry and price, abort if too high. Don't want to do fully in SQL so we can return current bid
		//
		// TODO: Get Date.Now() for comparison
		//
		if(now.after(expiry))
		{
			isSuccessfulBid = false;
			message = String.format("Your bid of %f could not be made; auction expired on %f", bid, expiry);
		}
		
		if(currentBid >= bid)
		{
			// TODO: Extract messages to config
			//
			isSuccessfulBid = false;
			message = String.format("Your bid of %f could not be made; the current high bid is %f", bid, currentBid);
			
			// 2.1. Unlock the row
			// END;
			//
		}
		else
		{
			// 3. Update the item, unlock
			// Look up SQL for this, probably UPDATE or ALTER
			// END;
			//
			message = String.format("Your bid of %f is the new highest bid", bid);
		}
		
		throw new NotYetImplementedException();
	}
	
	// Puts a new item up for auction; do we care about conflicts? Probably not, maybe within user's own items
	// Need to scrub name parameter to protect against injection
	public AuctionResponse addItem(String name, String description, UUID userId, float startingBid, Timestamp expiry)
	{
		// 1. Get user's items, make sure there are none with same name (this step is possibly unnecessary, and for now is skipped)
		// Something like:
		// SELECT count(*) FROM items WHERE user_id = '{userId}' and name = {name}
		AuctionItem item = new AuctionItem();
		UUID id = UUID.randomUUID();
		item.setId(id);
		item.setName(name);
		item.setDescription(description);
		item.setOwnerId(userId);
		item.setCurrentBid(startingBid);
		item.setExpiry(expiry);		
		
		// 2. if there is no conflict, insert a new row
		//
		boolean isInserted = sqlInsert(item);
		String responseMessage = null;

		if(isInserted)
		{
			responseMessage = String.format("Your item '%s' has been added with a starting bid of %f", name, startingBid);
		}
		else
		{
			// TODO: Possibly check for error reason, then we can say if, for example, it was due to duplicate
			responseMessage = String.format("An error occured, item '%s' was not added.", name);
			id = null;
		}	
		
		return new AuctionResponse(isInserted, responseMessage, id);
	}
	
	// Removes an item currently up for auction
	public AuctionResponse cancelAuction(UUID itemId)
	{
		throw new NotYetImplementedException();
	}
	
	
	public AuctionResponse addUser(String userName)
	{		
		User newUser = new User();
		UUID id = UUID.randomUUID();
	   
		newUser.setId(id);
		newUser.setUserName(userName);
		boolean isInserted = sqlInsert(newUser);
		String responseMessage = null;

		if(isInserted)
		{
			responseMessage = String.format("User %s was added.", userName);
		}
		else
		{
			// TODO: Possibly check for error reason, then we can say if, for example, it was due to duplicate
			responseMessage = String.format("An error occured, user %s was not added.", userName);
			id = null;
		}
		
		return new AuctionResponse(isInserted, responseMessage, id);
	}
	
	
	/*
	 * Utility functions, mainly common interactions with DB
	 */
	
	/**
	 * Inserts an item into the table.
	 * NOTE: Currently does not have any logic for handling duplicates, will just throw exception.
	 * @param itemToInsert Must be properly annotated and filled in. Will insert into the table specified by annotation.
	 * @return True if the record was inserted properly
	 */
	private boolean sqlInsert(Object itemToInsert)
	{
		HibernateContext context = HibernateContext.getInstance();
		SessionFactory sessionFactory = context.getSessionFactory();
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		boolean insertOkay = true;
		
		try 
		{
			transaction = session.beginTransaction();
			session.save(itemToInsert);
			transaction.commit();
		} 
		catch (HibernateException e) 
		{
		   if (transaction != null)
		   {
			   transaction.rollback();
		   }
		   
		   System.err.println(e.getMessage());
		   e.printStackTrace(); 
		   
		   insertOkay = false;
		} 
		finally 
		{
		   session.close(); 
		}
		
		return insertOkay;
	}
}
