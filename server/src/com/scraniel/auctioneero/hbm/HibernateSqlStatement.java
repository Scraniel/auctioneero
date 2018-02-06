package com.scraniel.auctioneero.hbm;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.scraniel.auctioneero.AuctionResponse;

public interface HibernateSqlStatement 
{
	public AuctionResponse execute(Session session) throws HibernateException;
}
