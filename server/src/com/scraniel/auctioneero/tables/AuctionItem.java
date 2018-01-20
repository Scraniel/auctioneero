package com.scraniel.auctioneero.tables;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "auction_item")
public class AuctionItem 
{
	@Id
	@Type(type = "UUID")
	@Column(name = "id", length = 36)
	private UUID id;
	
	@Column(name = "current_price")
	private float currentPrice;
	
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
	private Category category;
	
	@Column(name = "expiry", nullable = false)
	private Timestamp expiry;
	
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
	
}
