package com.scraniel.auctioneero.hbm;

import org.hibernate.*;
import org.hibernate.boot.*;
import org.hibernate.boot.registry.*;
import org.hibernate.service.*;

import com.scraniel.auctioneero.AuctionResponse;
import com.scraniel.auctioneero.tables.*;

public class HibernateContext 
{
	private SessionFactory sessionFactory;
	private static boolean initialized;
	private final static HibernateContext instance = new HibernateContext();
	
	/**
	 * Private as this is a singleton.
	 */
	private HibernateContext() {}
	
	/**
	 * Sets up hibernate and creates the SessionFactory for interacting with the DB.
	 */
	public void hibernateBootstrap()
	{
		// TODO: Throw exception, we should never try to bootstrap more than once
		if(initialized)
		{
			return;
		}
		
		// An example using an implicitly built BootstrapServiceRegistry
		ServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
				.configure()
				.build();

		// This will add our annotated classes for use later
		MetadataSources sources = new MetadataSources(standardRegistry);
		sources.addAnnotatedClass(AuctionItem.class);
		sources.addAnnotatedClass(User.class);
		
		// We need this to register the custom usertype for UUID
		MetadataBuilder metadataBuilder = sources.getMetadataBuilder();
		metadataBuilder.applyBasicType(new UUIDUserType(), "UUID");
		
		sessionFactory = metadataBuilder.build().buildSessionFactory();
		HibernateContext.initialized = true;
	}
	
	/**
	 * Executes a generic sql statement of the form:
	 * 
	 * 1. Begin transaction
	 * 2. Execute statement
	 * 3. Commit transaction
	 * 
	 * Will catch exceptions rollback if required. 
	 * @param toExecute The sql statement to execute.
	 * @return AuctionResponse with success message + ID or error message.
	 */
	public AuctionResponse executeSqlStatement(HibernateSqlStatement toExecute, Session session)
	{		
		Transaction transaction = null;
		AuctionResponse response;
		
		try 
		{
			transaction = session.beginTransaction();
			response = toExecute.execute(session);
			transaction.commit();
		} 
		catch (HibernateException e) 
		{
		   if (transaction != null)
		   {
			   transaction.rollback();
		   }
		   
		   response = new AuctionResponse(false, e.getMessage(), null);		   
		} 
		finally 
		{
		   session.close(); 
		}
		
		return response;
	}
	
	/**
	 * 
	 * @param toExecute
	 * @return
	 */
	public AuctionResponse executeSqlStatement(HibernateSqlStatement toExecute)
	{		
		return executeSqlStatement(toExecute, sessionFactory.openSession());
	}
	
	/**
	 * Gets the instance for this singleton.
	 * @return instance for the singleton.
	 */
	public static HibernateContext getInstance()
	{
		return instance;
	}

	/**
	 * Gets the SessionFactory to be used to interact with the DB
	 * @return the SessionFactory object
	 */
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	
	/**
	 * Whether or not Hibernate has been bootstrapped.
	 * @return true if hibernate has been initialized
	 */
	public static boolean isInitialized() 
	{
		return initialized;
	}
}
