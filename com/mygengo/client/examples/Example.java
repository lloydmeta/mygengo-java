package com.mygengo.client.examples;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.enums.Rating;
import com.mygengo.client.enums.RejectReason;
import com.mygengo.client.enums.Tier;
import com.mygengo.client.exceptions.MyGengoException;
import com.mygengo.client.payloads.TranslationJob;
import com.mygengo.client.payloads.TranslationJobs;

public class Example
{
    public static final int JSON_INDENT_FACTOR = 2;
    
	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
        Example eg = new Example();
	}
	
	MyGengoClient myGengo;
	
	public Example()
	{
		myGengo = new MyGengoClient(
				ApiKeys.PUBLIC_KEY,
				ApiKeys.PRIVATE_KEY,
				true);
		try
		{
			runTests();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void runTests() throws MyGengoException, JSONException, IOException
	{
	    /** No special pre-requisites */
		testAccountBalance();
		testAccountStats();
	    testDetermineTranslationCost();
        testGetServiceLanguages();
        testGetServiceLanguagePairs("de");
        testPostTranslationJobs();
        testGetTranslationJobs();
        testDeleteTranslationJob();
        int id = testPostTranslationJob();        
        testGetTranslationJob(id);	    
        
        /** Tests below require a previously posted job */
		testPostTranslationJobComment(id);
		testGetTranslationJobFeedback(id);
		testGetTranslationJobRevisions(id);
		testGetTranslationJobComments(id);
		
        /** Require a reviewable job */
		testReviseTranslationJob(id);        
		testGetTranslationJobPreview();
		testApproveTranslationJob();
        testRejectTranslationJob();
	}

	private void testAccountBalance() throws MyGengoException
	{
	    System.out.println("*** Testing account balance.");
		JSONObject response = myGengo.getAccountBalance();
		System.out.println(response);	
	}
	
	private void testAccountStats() throws MyGengoException
	{
	    System.out.println("*** Testing account stats.");
		JSONObject response = myGengo.getAccountStats();
		System.out.println(response);
	}
	
	private int testPostTranslationJob() throws MyGengoException, JSONException
	{
	    System.out.println("*** Posting a translation job.");
		TranslationJob job = new TranslationJob(
		        "revision test stub",
				"Revision test job",
				"en",
				"es",
				Tier.STANDARD);
		job.setAutoApprove(false);
		job.setCustomData("custom data");
		System.out.println(job.toJSONObject().toString(2));
		JSONObject response = myGengo.postTranslationJob(job);
		int id = response.getJSONObject("response").getJSONObject("job").getInt("job_id");
		System.out.println(response.toString(JSON_INDENT_FACTOR));
		return id;
	}
	
	private void testGetTranslationJob(int id) throws MyGengoException, JSONException
	{
	    System.out.println("*** Getting a translation job.");
		JSONObject response = myGengo.getTranslationJob(id);
		System.out.println(response.toString(JSON_INDENT_FACTOR));
	}
	
	private void testDeleteTranslationJob() throws MyGengoException, JSONException
	{
        System.out.println("*** Posting and deleting a translation job.");
        TranslationJob job = new TranslationJob(
                "delete test stub",
                "delete test job",
                "en",
                "es",
                Tier.STANDARD);
        System.out.println(job.toJSONObject().toString(2));
        JSONObject response = myGengo.postTranslationJob(job);
        int id = response.getJSONObject("response").getJSONObject("job").getInt("job_id");
	    response = myGengo.deleteTranslationJob(id);
	    System.out.println(response.toString(JSON_INDENT_FACTOR));
	}
	
	private void testGetTranslationJobs() throws MyGengoException, JSONException
	{
	    System.out.println("*** Getting all translation jobs.");
		JSONObject response = myGengo.getTranslationJobs();
		System.out.println(response.toString(JSON_INDENT_FACTOR));
		response = myGengo.getTranslationJobs(Arrays.asList(new Integer[]{44755, 44756}));
		System.out.println(response.toString(JSON_INDENT_FACTOR));
	}	
	
	private void testPostTranslationJobs() throws MyGengoException, JSONException
	{
	    System.out.println("*** Posting multiple translation jobs.");
		TranslationJobs jobList = new TranslationJobs();
		jobList.add(new TranslationJob("short story title", "This is a short story.","en","es",Tier.STANDARD));
		jobList.add(new TranslationJob("short story body", "There once was a man from Nantucket.","en","es",Tier.STANDARD));
		JSONObject response = myGengo.postTranslationJobs(jobList, true, true);
		System.out.println(response.toString(JSON_INDENT_FACTOR));
	}
	
	private void testApproveTranslationJob() throws MyGengoException, JSONException
	{
	    System.out.println("*** Approving translation job.");
		TranslationJob job = new TranslationJob(
				"little job",
		        "This is a nice little job",
				"en",
				"ja",
				Tier.STANDARD);
		job.setAutoApprove(false);
		System.out.println(job.toJSONObject().toString(2));
		JSONObject response = myGengo.postTranslationJob(job);
		System.out.println(response.toString(JSON_INDENT_FACTOR));
		int id = response.getJSONObject("response").getJSONObject("job").getInt("job_id");
		response = myGengo.approveTranslationJob(
				id,
				Rating.FOUR_STARS,
				"Thanks buddy, great job!",
				"Actually this stinks..",
				false
				);
		System.out.println(response.toString(JSON_INDENT_FACTOR));
	}
	
	private void testDetermineTranslationCost() throws MyGengoException, JSONException
	{
	    System.out.println("*** Testing translation quote.");
	    TranslationJobs jobList = new TranslationJobs();
        jobList.add(new TranslationJob("very short job", "A very short job..","en","ja",Tier.STANDARD));
        jobList.add(new TranslationJob("longer job", "Still short but slightly longer job.","en","ja",Tier.STANDARD));
        JSONObject response = myGengo.determineTranslationCost(jobList);
        System.out.println(response.toString(JSON_INDENT_FACTOR));	    
	}
	
	/**
	 * User input method for rejecting translation
	 */
	private String getCaptchaValueForJob(JSONObject job) throws JSONException
	{
		String url = job.getString("captcha_url");
		System.out.println("Tell me what this captcha says: " + url);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String captcha = null;
		try
		{
			captcha = in.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return captcha;
	}
	
	private void testRejectTranslationJob() throws MyGengoException, JSONException
	{
	    System.out.println("*** Reject a translation job");
		TranslationJob job = new TranslationJob(
		        "rejecto job",
				"Rejecto this job",
				"en",
				"es",
				Tier.STANDARD);
		job.setAutoApprove(false);
		System.out.println(job.toJSONObject().toString(2));
		JSONObject response = myGengo.postTranslationJob(job);
		System.out.println(response.toString(JSON_INDENT_FACTOR));
		int id = response.getJSONObject("response").getJSONObject("job").getInt("job_id");
		JSONObject postedJob = myGengo.getTranslationJob(id).getJSONObject("response").getJSONObject("job");
		String captcha = getCaptchaValueForJob(postedJob);		
		response = myGengo.rejectTranslationJob(
				id,
				RejectReason.OTHER,
				"I just don't like it",
				captcha,
				true
				);
		System.out.println(response.toString(JSON_INDENT_FACTOR));
	}
	
	private void testGetTranslationJobComments(int id) throws MyGengoException, JSONException
	{
	    System.out.println("*** Get comments for a job");
		JSONObject response = myGengo.getTranslationJobComments(id);
		System.out.println(response.toString(JSON_INDENT_FACTOR));
	}

	private void testPostTranslationJobComment(int id) throws MyGengoException, JSONException
	{
	    System.out.println("*** Post comment to a job");
		JSONObject response = myGengo.postTranslationJobComment(id, "How's this for a comment.");
		System.out.println(response.toString(JSON_INDENT_FACTOR));
	}
	
	private void testGetTranslationJobFeedback(int id) throws MyGengoException, JSONException
	{
	    System.out.println("*** Get feedback for a job");
	    JSONObject response = myGengo.getTranslationJobFeedback(id);
        System.out.println(response.toString(JSON_INDENT_FACTOR));
	}

    private void testGetTranslationJobRevisions(int id) throws MyGengoException, JSONException
    {
        System.out.println("*** Get all revisions for a job");
        JSONObject response = myGengo.getTranslationJobRevisions(id);
        System.out.println(response.toString(JSON_INDENT_FACTOR));
    }

    private void testGetServiceLanguages() throws MyGengoException, JSONException
    {
        System.out.println("*** Get all service languages.");
        JSONObject response = myGengo.getServiceLanguages();
        System.out.println(response.toString(JSON_INDENT_FACTOR));
    }
    
    private void testGetServiceLanguagePairs(String sourceLanguageCode) throws MyGengoException, JSONException
    {
        System.out.println("*** Get all service language pairs.");
        JSONObject response = myGengo.getServiceLanguagePairs(sourceLanguageCode);
        System.out.println(response.toString(JSON_INDENT_FACTOR));
    }
    
    private void testReviseTranslationJob(int id) throws MyGengoException, JSONException
    {
        System.out.println("*** Set a reviewable job to revising state.");
        JSONObject response = myGengo.reviseTranslationJob(id, "Fix some things for me.");
        System.out.println(response.toString(JSON_INDENT_FACTOR));
        response = myGengo.getTranslationJobRevisions(id);
        System.out.println(response);
    }
    
    private void testGetTranslationJobPreview() throws MyGengoException, JSONException, IOException
    {
        System.out.println("*** Get preview of a reviewable job.");
        TranslationJob job = new TranslationJob(
                "another preview test stub",
                "Another preview test job",
                "en",
                "es",
                Tier.STANDARD);
        job.setAutoApprove(false);
        job.setCustomData("custom data");
        System.out.println(job.toJSONObject().toString(2));
        JSONObject response = myGengo.postTranslationJob(job);
        int id = response.getJSONObject("response").getJSONObject("job").getInt("job_id");
        System.out.println(response.toString(JSON_INDENT_FACTOR));
        
        BufferedImage img = myGengo.getTranslationJobPreviewImage(id);
        File file = new File("preview.jpg");
        ImageIO.write(img, "jpg", file);
        System.out.println(response.toString(JSON_INDENT_FACTOR));
    }
}
