package examples;

import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.exceptions.MyGengoException;

public class getTranslationJobFeedback
{

    public getTranslationJobFeedback() throws MyGengoException
    {
        MyGengoClient myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        JSONObject response = myGengo.getTranslationJobFeedback(42);
    }
    
}
