package com.mygengo.client.examples;

import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.enums.Tier;
import com.mygengo.client.exceptions.MyGengoException;
import com.mygengo.client.payloads.TranslationJob;
import com.mygengo.client.payloads.TranslationJobs;

public class postTranslationJobs
{

    public postTranslationJobs() throws MyGengoException
    {
        MyGengoClient myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        TranslationJobs jobList = new TranslationJobs();
        jobList.add(new TranslationJob("short story title", "This is a short story.","en","es",Tier.STANDARD));
        jobList.add(new TranslationJob("short story body", "There once was a man from Nantucket.","en","es",Tier.STANDARD));
        JSONObject response = myGengo.postTranslationJobs(jobList, true, true);
    }
    
}