package com.mygengo.client.examples;

import java.util.Arrays;

import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.enums.Rating;
import com.mygengo.client.enums.RejectReason;
import com.mygengo.client.exceptions.MyGengoException;
import com.mygengo.client.payloads.Approval;
import com.mygengo.client.payloads.Rejection;
import com.mygengo.client.payloads.Revision;

public class updateTranslationJobs
{

    MyGengoClient myGengo;
    
    public updateTranslationJobs() throws MyGengoException
    {
        myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        approveTranslationJobs();
        rejectTranslationJobs();
        reviseTranslationJobs();
    }
    
    private void approveTranslationJobs() throws MyGengoException
    {
        JSONObject response = myGengo.approveTranslationJobs(
            Arrays.asList(new Approval[]{
                new Approval(42, "Thanks, great job!", "Give this translator a medal..", false, Rating.FIVE_STARS),
                new Approval(99, "Really good.", "Very nice.", true, Rating.FOUR_STARS)
            })
        );
    }
    
    private void rejectTranslationJobs() throws MyGengoException
    {
        JSONObject response = myGengo.rejectTranslationJobs(
            Arrays.asList(new Rejection[]{
                new Rejection(42, RejectReason.OTHER, "Nice specific feedback.", "captcha text", true),
                new Rejection(99, RejectReason.INCOMPLETE, "Please do this bit.", "captcha text", false)
            })
        );
    }
    
    private void reviseTranslationJobs() throws MyGengoException
    {
        JSONObject response = myGengo.reviseTranslationJobs(
            Arrays.asList(new Revision[]{
                new Revision(42, "Please fix this and that."),
                new Revision(99, "Change this word.")
            })
        );
    }
    
}
