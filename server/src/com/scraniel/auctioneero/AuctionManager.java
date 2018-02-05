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
		HibernateContext context = HibernateContext.getInstance();
		SessionFactory sessionFactory = context.getSessionFactory();
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		
		session.load(AuctionItem.class, itemId, LockOptions.UPGRADE);
		
		
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
		return item.insert();
	}
	
	// Removes an item currently up for auction
	public AuctionResponse cancelAuction(UUID itemId)
	{
		throw new NotYetImplementedException();
	}
	
	
	/**
	 * Adds a new user. Currently we don't use username as primary key, think about changing this.
	 * 
	 * TODO: Add password (salt + hash) 
	 * 
	 * @param userName The username of the new user 
	 * @return AuctionResponse containing UUID of new user or error message if it fails
	 */
	public AuctionResponse addUser(String userName)
	{		
		User newUser = new User();
		UUID id = UUID.randomUUID();		
	   
		newUser.setId(id);
		newUser.setUserName(userName);
		
		return newUser.insert();
	}
	
}
