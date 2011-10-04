package com.mygengo.client.payloads;

import org.json.JSONException;
import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.enums.Tier;
import com.mygengo.client.exceptions.MyGengoException;

/**
 * A translation job payload
 */
public class TranslationJob extends Payload
{
    /** Maximum comment length in bytes. */
	public static final int MAX_COMMENT_LENGTH = 1024;
	
	public static final String FLAG_TRUE = MyGengoClient.MYGENGO_TRUE;
	
	/* Required fields */
	private String slug;
	private String textToTranslate;
	private String sourceLanguageCode;
	private String targetLanguageCode;
	private Tier tier;

	/* Optional fields */
	private boolean forceNewTranslation;
	private String comment;
	private boolean usePreferredTranslators;
	private String callbackUrl;
	private boolean autoApprove;
	private String customData;	
	
	/**
	 * Create a translation job with a slug string.
	 * @param slug short string to summarize this job
	 * @param textToTranslate text to be translated
	 * @param sourceLanguageCode source language code
	 * @param targetLanguageCode target language code
	 * @param tier translation tier
	 */
	public TranslationJob(String slug, String textToTranslate, String sourceLanguageCode, String targetLanguageCode, Tier tier)
	{
	    this.slug = slug;
		this.textToTranslate = textToTranslate;
		this.sourceLanguageCode = sourceLanguageCode;
		this.targetLanguageCode = targetLanguageCode;
		this.tier = tier;
	}

    /**
     * Create a translation job with no slug string.
     * @param textToTranslate text to be translated
     * @param sourceLanguageCode source language code
     * @param targetLanguageCode target language code
     * @param tier translation tier
     */	
	public TranslationJob(String textToTranslate, String sourceLanguageCode, String targetLanguageCode, Tier tier)
    {
        this("0", textToTranslate, sourceLanguageCode, targetLanguageCode, tier);
    }
	
	/* Getters and setters */
	public String getSlug()
	{
	    return slug;
	}
	public void setSlug(String slug)
	{
	    this.slug = slug;
	}
	public String getTextToTranslate()
	{
		return textToTranslate;
	}
	public void setTextToTranslate(String textToTranslate)
	{
		this.textToTranslate = textToTranslate;
	}
	public String getSourceLanguageCode()
	{
		return sourceLanguageCode;
	}
	public void setSourceLanguageCode(String sourceLanguageCode)
	{
		this.sourceLanguageCode = sourceLanguageCode;
	}
	public String getTargetLanguageCode()
	{
		return targetLanguageCode;
	}
	public void setTargetLanguageCode(String targetLanguageCode)
	{
		this.targetLanguageCode = targetLanguageCode;
	}
	public Tier getTier()
	{
		return tier;
	}
	public void setTier(Tier tier)
	{
		this.tier = tier;
	}
	public boolean isForceNewTranslation()
	{
		return forceNewTranslation;
	}
	public void setForceNewTranslation(boolean forceNewTranslation)
	{
		this.forceNewTranslation = forceNewTranslation;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	public boolean isUsePreferredTranslators()
	{
		return usePreferredTranslators;
	}
	public void setUsePreferredTranslators(boolean usePreferredTranslators)
	{
		this.usePreferredTranslators = usePreferredTranslators;
	}
	public String getCallbackUrl()
	{
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl)
	{
		this.callbackUrl = callbackUrl;
	}
	public boolean isAutoApprove()
	{
		return autoApprove;
	}
	public void setAutoApprove(boolean autoApprove)
	{
		this.autoApprove = autoApprove;
	}	
	public String getCustomData()
	{
		return customData;
	}	
	public void setCustomData(String data) throws MyGengoException
	{
		if (data.length() > MAX_COMMENT_LENGTH)
		{
			throw new MyGengoException("Maximum custom data is " + MAX_COMMENT_LENGTH + " bytes");
		}
		customData = new String(data);
	}

	/** Utility method */
	private boolean isNullOrEmpty(String str)
	{
		return (null == str || str.length() == 0);
	}
	
	/**
	 * Create a JSONObject representing this job
	 * @return the JSONObject created
	 * @throws MyGengoException
	 */
	public JSONObject toJSONObject() throws MyGengoException
	{
		if (isNullOrEmpty(textToTranslate)) throw new MyGengoException("Invalid translation job - textToTranslate is required.");
		if (isNullOrEmpty(this.sourceLanguageCode)) throw new MyGengoException("Invalid translation job - sourceLanguageCode is required.");
		if (isNullOrEmpty(this.targetLanguageCode)) throw new MyGengoException("Invalid translation job - targetLanguageCode is required.");

		JSONObject job = new JSONObject();
		try
		{
			job.put("body_src", this.textToTranslate);
			job.put("lc_src", this.sourceLanguageCode);
			job.put("lc_tgt", this.targetLanguageCode);
			job.put("tier", this.tier.toString().toLowerCase());
			job.put("slug", this.slug);
			
			if (this.forceNewTranslation)
			{
				job.put("force", FLAG_TRUE);
			}
			if (!isNullOrEmpty(comment))
			{
				job.put("comment", this.comment);
			}
			if (this.usePreferredTranslators)
			{
				job.put("use_preferred", FLAG_TRUE);
			}
			if (!isNullOrEmpty(this.callbackUrl))
			{
				job.put("callback_url", this.callbackUrl);
			}
			if (this.autoApprove)
			{
				job.put("auto_approve", FLAG_TRUE);
			}
			if (null != this.customData && this.customData.length() > 0)
			{
				job.put("custom_data", this.customData);
			}
		}
		catch (JSONException e)
		{
			throw new MyGengoException("Could not create JSONObject for TranslationJob", e);
		}
		return job;
	}
}
