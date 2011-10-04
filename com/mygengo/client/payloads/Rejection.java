package com.mygengo.client.payloads;

import org.json.JSONException;
import org.json.JSONObject;

import com.mygengo.client.enums.RejectReason;
import com.mygengo.client.enums.Tier;
import com.mygengo.client.exceptions.MyGengoException;

/**
 * Job rejection payload
 */
public class Rejection extends JobUpdate
{
    private RejectReason reason;
    private String comment;
    private String captcha;
    private boolean requeue;
    
    public Rejection(int jobId, RejectReason reason, String comment, String captcha, boolean requeue)
    {
        super(jobId);
        init(reason, comment, captcha, requeue);        
    }
    
    public Rejection(String lc_src, String lc_tgt, String body_src, Tier tier,
            RejectReason reason, String comment, String captcha, boolean requeue)
    {
        super(lc_src, lc_tgt, body_src, tier);
        init(reason, comment, captcha, requeue);
    }

    private void init(RejectReason reason, String comment, String captcha, boolean requeue)
    {
        this.reason = reason;
        this.comment = comment;
        this.captcha = captcha;
        this.requeue = requeue;
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
            id.put("reason", reason.toString().toLowerCase());
            id.put("comment", comment);
            id.put("captcha", captcha);
            id.put("follow_up", requeue ? "requeue" : "cancel");
            return id;
        }
        catch (JSONException e)
        {
            throw new MyGengoException("Could not create JSONObject", e);
        }
    }

}
