package com.scraniel.auctioneero.tables;

import java.util.UUID;

import javax.persistence.*;

// TODO: Research how to store passwords; as some hash + salt? Don't want to load plaintext password into memory

@Entity
@Table(name = "user")
public class User 
{
	@Id
	@Column(name = "id")
	private UUID id;
	
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
	
	public UUID getId() 
	{
		return id;
	}
	
	public void setId(UUID id) 
	{
		this.id = id;
	}
}
