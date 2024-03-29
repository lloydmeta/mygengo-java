myGengo Java Library (for the [myGengo API](http://mygengo.com/))
==========================================================================================================================
Translating your tools and products helps people all over the world access them; this is, of course, a
somewhat tricky problem to solve. **[myGengo](http://mygengo.com/)** is a service that offers human-translation
(which is often a higher quality than machine translation), and an API to manage sending in work and watching
jobs. This is a Java interface to make using the API simpler (some would say incredibly easy). 


Building
---------------------------------------------------------------------------------------------------------------------------

Ant
---
Simply run `ant` from the base directory after checking out. This will download dependencies, compile classes and package the
library in `lib/mygengo.jar`. Add this jar file to your classpath to start working with the library.

Maven
-----
Run `mvn package` from the base directory. Package is built into the `target` directory.

Eclipse
-------
Quick start:
* Checkout code from github
* File menu > New Java Project
* Uncheck "Use default location"
* Set project layout to Use project folder as root for sources and class files
* Browse for location checked out code
* Hit next
* Hit finish
* Build!



Dependencies
---------------------------------------------------------------------------------------------------------------------------
The **[JSON in Java](http://json.org/java/)** package by Douglas Crockford.


Example Code
---------------------------------------------------------------------------------------------------------------------------

``` java  
import org.json.JSONException;
import org.json.JSONObject;

import com.mygengo.client.MyGengoClient;
import com.mygengo.client.enums.Tier;
import com.mygengo.client.exceptions.MyGengoException;
import com.mygengo.client.payloads.TranslationJob;

public class ShortExample
{
    private void ShortExample() throws MyGengoException, JSONException
    {
        MyGengoClient myGengo = new MyGengoClient(ApiKeys.PUBLIC_KEY, ApiKeys.PRIVATE_KEY, true);
        TranslationJob job = new TranslationJob("Stub", "Here's a job", "en", "es", Tier.STANDARD);
        JSONObject response = myGengo.postTranslationJob(job);
    }    
}

```

Question, Comments, Complaints, Praise?
---------------------------------------------------------------------------------------------------------------------------
If you have questions or comments and would like to reach us directly, please feel free to do
so at the following outlets. We love hearing from developers!

Email: ryan [at] mygengo dot com  
Twitter: **[@mygengo_dev](http://twitter.com/mygengo_dev)**  

If you come across any issues, please file them on the **[Github project issue tracker](https://github.com/myGengo/mygengo-java/issues)**. Thanks!


Credits & License
---------------------------------------------------------------------------------------------------------------------------
This library is based on the excellent C# interface by Saqib Shaikh

The library itself is licensed under a BSD-style license. See the enclosed LICENSE.txt file for more information.
