package examples;

import java.util.Arrays;

import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.exceptions.MyGengoException;

public class deleteTranslationJobs
{

    public deleteTranslationJobs() throws MyGengoException
    {
        MyGengoClient myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        JSONObject response = myGengo.deleteTranslationJobs(Arrays.asList(new Integer[]{42, 99}));
    }
    
}
