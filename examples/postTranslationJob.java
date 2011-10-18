package examples;

import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.enums.Tier;
import com.mygengo.client.exceptions.MyGengoException;
import com.mygengo.client.payloads.TranslationJob;

public class postTranslationJob
{

    public postTranslationJob() throws MyGengoException
    {
        MyGengoClient myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        TranslationJob job = new TranslationJob("stub", "Quick brown fox", "en", "es", Tier.STANDARD);
        job.setAutoApprove(false);
        job.setCustomData("custom data");
        JSONObject response = myGengo.postTranslationJob(job);
    }
    
}
