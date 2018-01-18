package auctioneero;

import java.sql.Timestamp;
import java.util.UUID;

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
			message = String.format("Your bid of {0} could not be made; auction expired on {1}", bid, expiry);
		}
		if(currentBid >= bid)
		{
			// Extract messages to config
			//
			isSuccessfulBid = false;
			message = String.format("Your bid of {0} could not be made; the current high bid is {1}", bid, currentBid);
			
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
			message = String.format("Your bid of {0} is the new highest bid", bid);
		}
		
		return new AuctionResponse(isSuccessfulBid, message);
	}
	
	// Puts a new item up for auction; do we care about conflicts? Probably not, maybe within user's own items
	// Need to scrub name parameter to protect against injection
	public AuctionResponse addItem(String name, String description, UUID userId, float startingBid, Timestamp expiry)
	{
		// 1. Get user's items, make sure there are none with same name (this step is possibly unnecessary)
		// Something like:
		// SELECT count(*) FROM items WHERE user_id = '{userId}' and name = {name}
		
		// 2. if there is no conflict, insert a new row
		//
		String message = String.format("Your item '{0}' has been added with a starting bid of {1}", name, startingBid);
		
		return new AuctionResponse(true, message);
	}
	
	// Removes an item currently up for auction
	public boolean cancelAuction(UUID itemId)
	{
		return false; 
	}
}
