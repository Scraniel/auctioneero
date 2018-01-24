package com.scraniel.auctioneero.hbm;

import org.hibernate.*;
import org.hibernate.boot.*;
import org.hibernate.boot.registry.*;
import org.hibernate.service.*;

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
