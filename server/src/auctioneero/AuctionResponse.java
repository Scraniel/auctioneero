package auctioneero;

public class AuctionResponse 
{
	private boolean isActionSuccess;
	private String outputMessage;
	
	public AuctionResponse(boolean isActionSuccess, String outputMessage)
	{
		this.isActionSuccess = isActionSuccess;
		this.outputMessage = outputMessage;
	}
	
	public boolean isActionSuccess() 
	{
		return isActionSuccess;
	}
	
	public String getOutputMessage() 
	{
		return outputMessage;
	}	
}
