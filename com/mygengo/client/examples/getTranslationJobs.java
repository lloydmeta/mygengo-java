package com.mygengo.client.examples;

import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.exceptions.MyGengoException;

public class getTranslationJobs
{

    public getTranslationJobs(int id) throws MyGengoException
    {
        MyGengoClient myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        JSONObject response = myGengo.getTranslationJobs();
    }
    
}
