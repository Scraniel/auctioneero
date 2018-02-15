package com.scraniel.auctioneero.tables;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.annotations.Type;

import com.scraniel.auctioneero.AuctionResponse;
import com.scraniel.auctioneero.hbm.HibernateContext;
import com.scraniel.auctioneero.hbm.HibernateSqlStatement;
import com.scraniel.auctioneero.hbm.HibernateTable;

@Entity
@Table(name = "auction_item")
public class AuctionItem extends HibernateTable
{
	
	@Column(name = "current_bid")
	private float currentBid;
	
	@Column(name = "name", length = 20)
	private String name;
	
	@Column(name = "description", length = 128)
	private String description;
	
	@Type(type = "UUID")
	@Column(name = "owner_id", nullable = false, length = 36)
	private UUID ownerId;
	
	@Type(type = "UUID")
	@Column(name = "high_bidder_id", length = 36)
	private UUID highBidderId;
	
	// We use custom getter setter to store as VARCHAR (see getCategoryString / setCategoryString)
	@Transient
	private Category category;
	
	@Column(name = "expiry", nullable = false)
	private Timestamp expiry;
	
	///
	/// GETTERS / SETTERS
	///
	public float getCurrentBid() 
	{
		return currentBid;
	}
	
	public void setCurrentBid(float currentBid) 
	{
		this.currentBid = currentBid;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getDescription() 
	{
		return description;
	}
	
	public void setDescription(String description) 
	{
		this.description = description;
	}
	
	public UUID getOwnerId() 
	{
		return ownerId;
	}
	
	public void setOwnerId(UUID ownerId) 
	{
		this.ownerId = ownerId;
	}
	
	public UUID getHighBidderId() 
	{
		return highBidderId;
	}
	
	public void setHighBidderId(UUID highBidderId) 
	{
		this.highBidderId = highBidderId;
	}

	// We don't use this because we want to store as a VARCHAR. If category is used in multiple tables, make a UserType
	@Transient
	public Category getCategory() 
	{
		return category;
	}
	public void setCategory(Category category) 
	{
		this.category = category;
	}
	
	@Column(name="category", length = 20)
	public String getCategoryString()
	{
		return category.toString();
	}
	public void setCategoryString(String value)
	{
		category = Category.valueOf(value);
	}

	public Timestamp getExpiry() 
	{
		return expiry;
	}

	public void setExpiry(Timestamp expiry) 
	{
		this.expiry = expiry;
	}

	@Override
	protected String getSuccessfulInsertMessage() 
	{
		return String.format("The item '%s' has been added with initial bid %.2f", name, currentBid);
	}
	
	@Override
	protected String getSuccessfulDeleteMessage() 
	{
		return "Item deleted successfully.";
	}
	
	/**
	 * Bids on an item. Should only be called with a persistent instance.
	 * 
	 * @param bid The new bid for the item
	 * @param bidderId The bidder
	 * @return
	 */
	public AuctionResponse bidOn(float bid, UUID bidderId)
	{
		if(currentSession == null)
		{
			// TODO: Should be exception
			//
			return new AuctionResponse(false, "Need persistent instance", null);
		}
		
		HibernateSqlStatement statement = new HibernateSqlStatement()
		{
			@Override
			public AuctionResponse execute(Session session) throws HibernateException
			{
				String message = null;
				boolean isSuccessfulBid = true;
				Timestamp now = new Timestamp(System.currentTimeMillis());
				
				// 2. Check the expiry and price, abort if too high. Don't want to do fully in SQL so we can return current bid
				//
				if(now.after(getExpiry()))
				{
					isSuccessfulBid = false;
					message = String.format("Your bid of %f could not be made; auction expired on %f", bid, getExpiry());
				}
				else if(getCurrentBid() >= bid)
				{
					// TODO: Extract messages to config
					//
					isSuccessfulBid = false;
					message = String.format("Your bid of %f could not be made; the current high bid is %f", bid, getCurrentBid());
					
					// 2.1. Unlock the row
					// END;
					//
				}
				else
				{
					// 3. Update the item, unlock
					// END;
					//
					// Note that since we used session.load, the item is in a persistent state and is tied to the session. Thus,
					// the item will be persisted as soon as the transaction is committed, no save / merge / update necessary.
					//
					setCurrentBid(bid);
					setHighBidderId(bidderId);
					message = String.format("Your bid of %f is the new highest bid", bid);
				}	
				
				return new AuctionResponse(isSuccessfulBid, message, getId());
			}
		};
		
		return HibernateContext.getInstance().executeSqlStatement(statement, currentSession); 
	}
}
