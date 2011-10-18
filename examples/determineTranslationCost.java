package examples;

import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.enums.Tier;
import com.mygengo.client.exceptions.MyGengoException;
import com.mygengo.client.payloads.TranslationJob;
import com.mygengo.client.payloads.Payloads;

public class determineTranslationCost
{

    public determineTranslationCost(int id) throws MyGengoException
    {
        MyGengoClient myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        Payloads jobList = new Payloads();
        jobList.add(new TranslationJob("very short job", "A very short job..","en","ja",Tier.STANDARD));
        jobList.add(new TranslationJob("longer job", "Still short but slightly longer job.","en","ja",Tier.STANDARD));
        JSONObject response = myGengo.determineTranslationCost(jobList);
    }
    
}
