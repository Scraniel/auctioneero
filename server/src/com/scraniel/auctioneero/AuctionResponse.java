package com.scraniel.auctioneero;

import java.util.UUID;

public class AuctionResponse 
{
	private boolean isActionSuccess;
	private String outputMessage;
	private UUID id;
	
	public AuctionResponse(boolean isActionSuccess, String outputMessage, UUID id)
	{
		this.isActionSuccess = isActionSuccess;
		this.outputMessage = outputMessage;
		this.id = id;
	}
	
	public boolean isActionSuccess() 
	{
		return isActionSuccess;
	}
	
	public String getOutputMessage() 
	{
		return outputMessage;
	}

	public UUID getId() 
	{
		return id;
	}
}
