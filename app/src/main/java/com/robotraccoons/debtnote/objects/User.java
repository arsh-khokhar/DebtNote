package com.robotraccoons.debtnote.objects;

public class User
{
	private static int numUsers = 0;
	private final int userID;
	private final String userName;
	private final String userPassword;

	public User(final String newUserName, final String newUserPassword) {
		this.userName = newUserName;
		this.userPassword = newUserPassword;
		numUsers++;
		this.userID = numUsers;
	}

	public User(final int userID, final String newUserName, final String newUserPassword)
	{
		numUsers++;
		this.userID = userID;
		this.userName = newUserName;
		this.userPassword = newUserPassword;
	}
	public int getUserID() {	return (userID); }

	public String getUserName()
	{
		return (userName);
	}

	public String getUserPassword()
	{
		return (userPassword);
	}

}
