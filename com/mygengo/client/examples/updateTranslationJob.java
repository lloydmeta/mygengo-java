package com.mygengo.client.examples;

import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.enums.Rating;
import com.mygengo.client.enums.RejectReason;
import com.mygengo.client.exceptions.MyGengoException;

public class updateTranslationJob
{

    MyGengoClient myGengo;
    
    public updateTranslationJob() throws MyGengoException
    {
        myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        approveTranslationJob();
        rejectTranslationJob();
        reviseTranslationJob();
    }
    
    private void approveTranslationJob() throws MyGengoException
    {
        JSONObject response = myGengo.approveTranslationJob(
                42,
                Rating.FOUR_STARS,
                "Thanks, great job!",
                "Give this translator a medal..",
                false
                );
    }
    
    private void rejectTranslationJob() throws MyGengoException
    {
        JSONObject response = myGengo.rejectTranslationJob(
                42,
                RejectReason.OTHER,
                "A really good reason",
                "captcha text",
                true
                );
    }
    
    private void reviseTranslationJob() throws MyGengoException
    {
        JSONObject response = myGengo.reviseTranslationJob(42, "Please fix this and that.");     
    }
    
}
