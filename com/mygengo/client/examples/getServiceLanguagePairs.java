package com.mygengo.client.examples;

import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.exceptions.MyGengoException;

public class getServiceLanguagePairs
{
    
    public getServiceLanguagePairs() throws MyGengoException
    {
        MyGengoClient myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        JSONObject response = myGengo.getServiceLanguagePairs();
    }

}
