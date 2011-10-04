package com.mygengo.client.payloads;

import org.json.JSONException;
import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.enums.Rating;
import com.mygengo.client.enums.Tier;
import com.mygengo.client.exceptions.MyGengoException;

/**
 * Job approval payload
 */
public class Approval extends JobUpdate
{
    private String feedbackTranslator;
    private String feedbackMyGengo;
    private boolean isFeedbackPublic;
    private Rating rating;
    
    public Approval(int jobId,
            String feedbackTranslator, String feedbackMyGengo, boolean isFeedbackPublic, Rating rating)
    {
        super(jobId);
        init(feedbackTranslator, feedbackMyGengo, isFeedbackPublic, rating);        
    }
    
    public Approval(String lc_src, String lc_tgt, String body_src, Tier tier,
            String feedbackTranslator, String feedbackMyGengo, boolean isFeedbackPublic, Rating rating)
    {
        super(lc_src, lc_tgt, body_src, tier);
        init(feedbackTranslator, feedbackMyGengo, isFeedbackPublic, rating);
    }

    private void init(String feedbackTranslator, String feedbackMyGengo, boolean isFeedbackPublic, Rating rating)
    {
        this.feedbackTranslator = feedbackTranslator;
        this.feedbackMyGengo = feedbackMyGengo;
        this.isFeedbackPublic = isFeedbackPublic;
        this.rating = rating;
    }
    
    /**
     * Create a JSONObject representing this rejection
     * @return the JSONObject created
     * @throws MyGengoException
     */
    public JSONObject toJSONObject() throws MyGengoException
    {
        JSONObject id = super.toJSONObject();
        try
        {
            id.put("action", "approve");
            id.put("for_translator", feedbackTranslator);
            id.put("for_mygengo", feedbackMyGengo);
            id.put("public", isFeedbackPublic ? MyGengoClient.MYGENGO_TRUE : MyGengoClient.MYGENGO_FALSE);
            id.put("rating", rating.toString());
            return id;
        }
        catch (JSONException e)
        {
            throw new MyGengoException("Could not create JSONObject", e);
        }
    }
}
