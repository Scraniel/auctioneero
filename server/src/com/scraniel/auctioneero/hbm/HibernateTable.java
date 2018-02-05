package com.scraniel.auctioneero.hbm;

import java.util.UUID;

import javax.persistence.*;

import org.hibernate.*;
import org.hibernate.annotations.*;

import com.scraniel.auctioneero.AuctionResponse;

@MappedSuperclass
public abstract class HibernateTable 
{
	@Id
	@Type(type = "UUID")
	@Column(name = "id", length = 36)
	protected UUID id;
	
	public UUID getId() 
	{
		return id;
	}
	
	public void setId(UUID id) 
	{
		this.id = id;
	}
	
	/**
	 * Returns an informational message when this object gets inserted to it's table
	 * 
	 * TODO: May want to have these format string (among other strings) to a config file
	 * 
	 * @return String with informational message upon successful insert
	 */
	protected abstract String getSuccessfulInsertMessage();
	
	/**
	 * Performs a simple insert of the current object (ie. start transaction, save, commit transaction
	 * 
	 * @return AuctionResponse object either containing error or the UUID of the newly inserted object
	 */
	public AuctionResponse insert()
	{
		// Required so anonymous class uses subclass instead of parent
		HibernateTable current = this;
		
		HibernateSqlStatement statement = new HibernateSqlStatement()
		{			
			@Override
			public AuctionResponse execute(Session session) throws HibernateException
			{
				UUID newId = (UUID)(session.save(current));
				
				return new AuctionResponse(true, getSuccessfulInsertMessage(), newId);
			}
		};
		
		return HibernateContext.getInstance().executeSqlStatement(statement);
	}

}
