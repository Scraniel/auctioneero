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
	
	// The session used to retrieve this object
	//
	@Transient
	protected Session currentSession;
	
	public UUID getId() 
	{
		return id;
	}
	
	public void setId(UUID id) 
	{
		this.id = id;
	}
	
	public Session getCurrentSession() 
	{
		return currentSession;
	}
	
	public void setCurrentSession(Session currentSession) 
	{
		this.currentSession = currentSession;
	}
	
	/**
	 * Returns an informational message when this object gets inserted to its table
	 * 
	 * TODO: May want to have these format string (among other strings) to a config file
	 * 
	 * @return String with informational message upon successful insert
	 */
	protected abstract String getSuccessfulInsertMessage();
	
	/**
	 * Returns an informational message when this object gets deleted from its table
	 * 
	 * TODO: May want to have these format string (among other strings) to a config file
	 * 
	 * @return String with informational message upon successful delete
	 */
	protected abstract String getSuccessfulDeleteMessage();
	
	/**
	 * Performs a simple insert of the current object (ie. start transaction, save, commit transaction)
	 * 
	 * @return AuctionResponse object either containing error or the UUID of the newly inserted object
	 */
	public AuctionResponse insert()
	{
		// Required so anonymous class uses subclass instead of parent
		//
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
	
	/**
	 * Performs a simple delete of the current object (ie. start transaction, delete, commit transaction)
	 * Should only be called with a persistent instance (if you have not-null columns, cannot delete from 
	 * transient instance)
	 * 
	 * @return AuctionResponse object either containing error or the successful delete message
	 */
	public AuctionResponse delete()
	{
		// Required so anonymous class uses subclass instead of parent
		//
		HibernateTable current = this;
		
		HibernateSqlStatement statement = new HibernateSqlStatement()
		{			
			@Override
			public AuctionResponse execute(Session session) throws HibernateException
			{
				session.delete(current);
				
				return new AuctionResponse(true, getSuccessfulDeleteMessage(), null);
			}
		};
		
		return HibernateContext.getInstance().executeSqlStatement(statement, currentSession);
	}
}
