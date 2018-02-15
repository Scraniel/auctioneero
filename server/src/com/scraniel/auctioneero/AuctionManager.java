package com.scraniel.auctioneero;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.*;
import org.hibernate.cfg.NotYetImplementedException;

import com.scraniel.auctioneero.hbm.*;
import com.scraniel.auctioneero.tables.*;

/**
 * Will be the interface used by the REST API to interact with objects in the database.
 * @author Danny Lewis
 *
 */
public class AuctionManager 
{
	/**
	 *  Attempts to add a new bid on an item.
	 *  IDEA:	* Lock the item row
	 *		 	* Check the price, abort if too high
	 *		 	* Update the item
	 *		 	* Unlock the row
	 * @param itemId The item to bid on
	 * @param bidderId The bidder
	 * @param bid The new bid
	 * @return
	 */
	public AuctionResponse bidOnItem(UUID itemId, UUID bidderId, float bid)
	{
		// We need to open our own session so we can load the item
		//
		Session session = HibernateContext.getInstance().getSessionFactory().openSession();
		
		// 1. Lock the item row
		// BEGIN;
		// SELECT * FROM items WHERE item_id = '{itemId}' FOR UPDATE;
		//
		AuctionItem item = session.load(AuctionItem.class, itemId, LockOptions.UPGRADE);
		item.setCurrentSession(session);
		
		return item.bidOn(bid, bidderId);
	}
	
	/**
	 * Puts a new item up for auction.
	 * NOTES: - do we care about conflicts? Probably not, maybe within user's own items
	 * 		  -	Hibernate is quite safe in terms of SQL injection, parameterizes everything
	 * @param name The name of the item
	 * @param description A description of the item
	 * @param userId The user putting the item up for auction
	 * @param startingBid The value to start the bidding at
	 * @param expiry When the auction expires
	 * @return
	 */
	public AuctionResponse addItem(String name, String description, UUID userId, float startingBid, Timestamp expiry)
	{
		// 1. Get user's items, make sure there are none with same name (this step is possibly unnecessary, and for now is skipped)
		// Something like:
		// SELECT count(*) FROM items WHERE user_id = '{userId}' and name = {name}
		//
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
	
	/**
	 * Removes an item currently up for auction
	 * TODO: Eventually should add an event that fires when this happens so we can inform bidders
	 *  
	 * @param itemId Item to cancel
	 * @return AuctionResponse containing either an error successful delete message
	 */
	public AuctionResponse cancelAuction(UUID itemId)
	{
		// We need to open our own session so we can load the item
		//
		Session session = HibernateContext.getInstance().getSessionFactory().openSession();
		AuctionItem item = session.load(AuctionItem.class, itemId);
		item.setCurrentSession(session);
		
		return item.delete();
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
