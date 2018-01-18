package auctioneero;

import java.util.Date;
import java.util.UUID;

public class AuctionItem 
{
	private float currentPrice;
	private String name;
	private String description;
	private UUID id;
	private UUID ownerId;
	private UUID highBidderId;
	private Category category;
	private Date expiry;
	
	///
	/// GETTERS / SETTERS
	///
	public float getCurrentPrice() 
	{
		return currentPrice;
	}
	
	public void setCurrentPrice(float currentPrice) 
	{
		this.currentPrice = currentPrice;
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
	
	public UUID getId() 
	{
		return id;
	}
	
	public void setId(UUID id) 
	{
		this.id = id;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	
	
}
