package com.mygengo.client.payloads;

import org.json.JSONException;
import org.json.JSONObject;

import com.mygengo.client.enums.Tier;
import com.mygengo.client.exceptions.MyGengoException;

/**
 * Job revision payload
 */
public class Revision extends JobUpdate
{
    private String comment;
    
    public Revision(int jobId, String comment)
    {
        super(jobId);
        init(comment);        
    }
    
    public Revision(String lc_src, String lc_tgt, String body_src, Tier tier, String comment)
    {
        super(lc_src, lc_tgt, body_src, tier);
        init(comment);
    }

    private void init(String comment)
    {
        this.comment = comment;
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
            id.put("comment", comment);
            return id;
        }
        catch (JSONException e)
        {
            throw new MyGengoException("Could not create JSONObject", e);
        }
    }
}
