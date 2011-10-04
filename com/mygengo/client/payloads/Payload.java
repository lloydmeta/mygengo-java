package com.mygengo.client.payloads;

import org.json.JSONObject;

import com.mygengo.client.exceptions.MyGengoException;

public abstract class Payload
{

    /**
     * Create a JSONObject representing this payload object
     * @return the JSONObject created
     * @throws MyGengoException
     */
    public abstract JSONObject toJSONObject() throws MyGengoException;
    
}
