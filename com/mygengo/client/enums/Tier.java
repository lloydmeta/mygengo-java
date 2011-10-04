package com.mygengo.client.enums;

public enum Tier
{
	MACHINE,
	STANDARD,
	PRO,
	ULTRA;
	
	public String toRequestString()
	{
	    return toString().toLowerCase();
	}
	
	public String toString()
	{
	    return toRequestString();
	}
};
