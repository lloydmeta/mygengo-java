package com.mygengo.client.payloads;

import org.json.JSONArray;

import com.mygengo.client.exceptions.MyGengoException;

/**
 * A collection of translation jobs.
 */
public class TranslationJobs
{
	private JSONArray jobs;
	
	/**
	 * Initialize the collection.
	 */
	public TranslationJobs()
	{
		jobs = new JSONArray();
	}
	
	/**
	 * Add a job payload to the collection.
	 * @param job the payload to add
	 * @throws MyGengoException
	 */
	public void add(TranslationJob job) throws MyGengoException
	{
		jobs.put(job.toJSONObject());
	}	
	
	/**
	 * @return a JSON array containing the job payloads in the collection.
	 */
	public JSONArray toJSONArray()
	{
		return jobs;
	}

}
