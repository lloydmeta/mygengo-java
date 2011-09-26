package com.mygengo.client.examples;

import java.awt.image.BufferedImage;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.exceptions.MyGengoException;

public class getTranslationJobPreview
{

    public getTranslationJobPreview() throws MyGengoException
    {
        MyGengoClient myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        BufferedImage image = myGengo.getTranslationJobPreviewImage(42);
    }
    
}
