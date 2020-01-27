package com.robotraccoons.debtnote.objects;

public class Transaction
{
	private static int numTransactions = 0;
	private final int transID;
	private final String transName;
	private final String transDescription;
	private final String payer;


	public Transaction(final int transId, final String newTransName, final String newTransDescription, final String payer)
	{
		this.payer = payer;
		this.transID = transId;
		transName = newTransName;
		transDescription = newTransDescription;
	}

	public Transaction(final String newTransName, final String newTransDescription){
		this.transDescription = newTransDescription;
		this.transName = newTransName;
		numTransactions++;
		this.payer = null;
		this.transID = numTransactions;
	}

	public int getTransID()
	{
		return (transID);
	}

	public String getTransName()
	{
		return (transName);
	}

	public String getTransDescription()
	{
		return (transDescription);
	}

	public String getPayer() {return payer;}
}