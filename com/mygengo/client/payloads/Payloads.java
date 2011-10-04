package com.mygengo.client.payloads;

import java.util.List;

import org.json.JSONArray;

import com.mygengo.client.exceptions.MyGengoException;

/**
 * A collection of payload objects.
 */
public class Payloads
{
	private JSONArray arr;
	
	/**
	 * Initialize an empty collection.
	 */
	public Payloads()
	{
		arr = new JSONArray();
	}
	
	/**
	 * Initialize a collection of payloads from a List collection
	 * @param payloads
	 * @throws MyGengoException
	 */
	public Payloads(List<Payload> payloads) throws MyGengoException
	{
	    this();
	    for (Payload p : payloads)
	    {
	        add(p);
	    }
	}
	
	/**
	 * Add a job payload to the collection.
	 * @param job the payload to add
	 * @throws MyGengoException
	 */
	public void add(Payload payload) throws MyGengoException
	{
		arr.put(payload.toJSONObject());
	}	
	
	/**
	 * @return a JSON array containing the job payloads in the collection.
	 */
	public JSONArray toJSONArray()
	{
		return arr;
	}

}
