package com.mygengo.client.exceptions;

@SuppressWarnings("serial")
/**
 * Encapsulates all exceptions thrown by the API 
 */
public class MyGengoException extends Exception
{
	public MyGengoException(String e)
	{
		super(e);
	}
	
	public MyGengoException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
	
}
