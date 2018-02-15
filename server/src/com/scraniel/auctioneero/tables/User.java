package com.scraniel.auctioneero.tables;

import javax.persistence.*;

import com.scraniel.auctioneero.hbm.HibernateTable;

// TODO: Research how to store passwords; as some hash + salt? Don't want to load plaintext password into memory

@Entity
@Table(name = "user")
public class User extends HibernateTable
{
	@Column(name = "user_name")
	private String userName;
	
	public String getUserName() 
	{
		return userName;
	}
	
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}

	@Override
	protected String getSuccessfulInsertMessage() 
	{
		return String.format("The user '%s' has been successfully added", userName);
	}
	
	@Override
	protected String getSuccessfulDeleteMessage() 
	{
		return "User deleted successfully.";
	}
}
