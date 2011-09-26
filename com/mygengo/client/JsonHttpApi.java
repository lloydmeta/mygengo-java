package com.mygengo.client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

import com.mygengo.client.enums.HttpMethod;
import com.mygengo.client.exceptions.ErrorResponseException;
import com.mygengo.client.exceptions.MyGengoException;

/**
 * The basis of an authenticated API built on JSON over HTTP
 */
public class JsonHttpApi
{
	public static final String VERSION = "2.0";
	
	/** Whether authentication is required by default for API method calls. */
	private static final boolean AUTHENTICATION_REQUIRED_DEFAULT = true;
	
	protected String publicKey;
	protected String privateKey;

	/**
	 * Initialize a new API instance with the specified keys.
	 * @param publicKey Public API key
	 * @param privateKey Private API key
	 */
	public JsonHttpApi(String publicKey, String privateKey)
	{
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	/**
	 * @return A timestamp string representing the current time.
	 */
    private String getTimestamp()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MILLISECOND, -cal.get(Calendar.DST_OFFSET) - cal.get(Calendar.ZONE_OFFSET));
        return String.valueOf(cal.getTimeInMillis() / 1000);
    }
	
    /**
     * @param url The API method URL
     * @param method An HTTP method
     * @return A JSONObject containing the response from the server
     * @throws MyGengoException if something went wrong :(
     */
	protected JSONObject call(String url, HttpMethod method) throws MyGengoException
	{
		return call(url, method, null, AUTHENTICATION_REQUIRED_DEFAULT);
	}

	/**
     * @param url The API method URL
     * @param method An HTTP method
     * @param data JSON data to send in request
     * @return A JSONObject containing the response from the server
     * @throws MyGengoException if something went wrong :(
	 */
	protected JSONObject call(String url, HttpMethod method, JSONObject data) throws MyGengoException
	{
		return call(url, method, data, AUTHENTICATION_REQUIRED_DEFAULT);
	}
	
	/**
     * @param url The API method URL
     * @param method An HTTP method
     * @param requiresAuthentication true iff authentication is required for this call
     * @return A JSONObject containing the response from the server
     * @throws MyGengoException if something went wrong :(
	 */
	protected JSONObject call(String url, HttpMethod method, boolean requiresAuthentication) throws MyGengoException
	{
		return call(url, method, null, requiresAuthentication);
	}

	private void handleData(Map<String, String> parameters, JSONObject data, HttpMethod method)
	{
	    if (null != data)
	    {
            if (HttpMethod.GET == method)
            {
                @SuppressWarnings("rawtypes") // This flows in from JSONJava
                Iterator dataKeys = data.keys();
                while (dataKeys.hasNext())
                {
                    String k = dataKeys.next().toString();
                    try
                    {
                        parameters.put(k, data.getString(k));
                    } catch (JSONException e)
                    {
                        // Skip keys that don't map to strings
                    }
                }
            }
            else// if (HttpMethod.POST == method || HttpMethod.PUT == method)
            {
                parameters.put("data", data.toString());
            }
	    }
	}
        
	/**
     * @param url The API method URL
     * @param method An HTTP method
     * @param data JSON data to send in request
     * @param requiresAuthentication true iff authentication is required for this call
     * @return A JSONObject containing the response from the server
     * @throws MyGengoException if something went wrong :(
	 */
	protected JSONObject call(String url, HttpMethod method, JSONObject data, boolean requiresAuthentication) throws MyGengoException
	{
		if (requiresAuthentication && (null == this.publicKey) || (null == this.privateKey))
		{
			throw new MyGengoException("This API requires authentication. Both a public and private key must be specified.");
		}

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("api_key", this.publicKey);

		if (requiresAuthentication)
		{
			parameters.put("ts", getTimestamp());
			parameters.put("api_sig", hmac(parameters.get("ts")));
		}
	    handleData(parameters, data, method);

		String queryString = makeQueryString(parameters);
		String rsp = sendRequest(method, url, queryString);

		JSONObject doc;
		try
		{
			doc = new JSONObject(rsp);
			if ("error".equals(doc.getString("opstat")))
			{
				JSONObject error = doc.getJSONObject("err");
				throw new ErrorResponseException(error.getString("msg"), error.getInt("code"));
			}
		}
		catch (JSONException e)
		{
			throw new MyGengoException("The response from the server is not valid JSON: " + rsp, e);
		}
		return doc;
	}

	/**
	 * Builds a query string from a dictionary of parameters and values
	 * @param data Maps parameters to values
	 * @return A properly encoded URL query string
	 * @throws MyGengoException
	 */
	private String makeQueryString(Map<String, String> data) throws MyGengoException
	{
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String, String> e : data.entrySet())
		{
			try {
				sb.append(String.format(
						"&%s=%s",
						URLEncoder.encode(e.getKey(), "UTF-8"),
						URLEncoder.encode(e.getValue(), "UTF-8")
				));
			}
			catch (UnsupportedEncodingException ex)
			{
				throw new MyGengoException("URLEncoder does not support UTF-8", ex);
			}
		}
		//sb.deleteCharAt(0); // Remove the initial ampersand

		return sb.toString();
	}
	
    /**
     * Create a signature for data
     * Note: both data and private_key are assumed to be ISO-8859-1
     * @param data The data to be hashed
     * @param private_key The key used to create the MAC
     * @return The HMAC in Hex format
     */
    private String hmac(String data) throws MyGengoException
    {
        try
        {
            SecretKeySpec signingKey = new SecretKeySpec(this.privateKey.getBytes("iso-8859-1"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(data.getBytes("iso-8859-1"));
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < rawHmac.length; i++)
            {
                int num = (rawHmac[i] & 0xff);
                String hex = Integer.toHexString(num);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                buf.append(hex);
            }
            return buf.toString();
        }
        catch (Exception ex)
        {
            throw new MyGengoException("Signing failed", ex);
        }
    }	

    /**
     * Perform an HTTP POST
     * @param con An open, configured HTTP connection
     * @param query An encoded query string
     * @return The response from the server
     * @throws MyGengoException if something went wrong :(
     */
    private String httpPost(HttpURLConnection con, String query) throws MyGengoException
    {
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		try
		{
			String length = Integer.toString(query.getBytes("UTF-8").length);
	        con.setRequestProperty("Content-Length", length);
	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	        out.writeUTF(query);
	        out.flush();
	        out.close();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
	        String line;
	        StringBuffer buf = new StringBuffer();
	        while (null != (line = reader.readLine()))
	        {
	            buf.append(line);
	        }
	        if (HttpURLConnection.HTTP_OK != con.getResponseCode()
	        	&& HttpURLConnection.HTTP_CREATED != con.getResponseCode())
	        {
	        	throw new MyGengoException(String.format("Unexpected HTTP response: %d", con.getResponseCode()));
	        }
	        return buf.toString();
		}
		catch (Exception e)
		{
			throw new MyGengoException("HTTP POST failed", e);
		}       
    }
    
    /**
     * Perform an HTTP GET
     * @param con An open, configured HTTP connection
     * @return The response from the server
     * @throws MyGengoException if something went wrong :(
     */
    private String httpGet(HttpURLConnection con) throws MyGengoException
    {
		try
		{
	        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
	        String line;
	        StringBuffer buf = new StringBuffer();
	        while (null != (line = reader.readLine()))
	        {
	            buf.append(line);
	        }
	        if (HttpURLConnection.HTTP_OK != con.getResponseCode())
	        {
	        	throw new MyGengoException(String.format("Unexpected HTTP response: %i", con.getResponseCode()));
	        }
	        return buf.toString();
		}
		catch (Exception e)
		{
			throw new MyGengoException("HTTP GET failed", e);
		}
    }
    
    /**
     * Perform an HTTP DELETE
     * @param con An open, configured HTTP connection
     * @return The response from the server
     * @throws MyGengoException if something went wrong :(
     */
    private String httpDelete(HttpURLConnection con) throws MyGengoException
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
            String line;
            StringBuffer buf = new StringBuffer();
            while (null != (line = reader.readLine()))
            {
                buf.append(line);
            }
            if (HttpURLConnection.HTTP_OK != con.getResponseCode())
            {
                throw new MyGengoException(String.format("Unexpected HTTP response: %i", con.getResponseCode()));
            }
            return buf.toString();
        }
        catch (Exception e)
        {
            throw new MyGengoException("HTTP DELETE failed", e);
        }
    }
    
    private String httpPut(HttpURLConnection con, String query) throws MyGengoException
    {
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "text/plain");
		try
		{
			String length = Integer.toString(query.getBytes("UTF-8").length);
	        con.setRequestProperty("Content-Length", length);
	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	        out.writeBytes(query);
	        out.flush();
	        out.close();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
	        String line;
	        StringBuffer buf = new StringBuffer();
	        while (null != (line = reader.readLine()))
	        {
	            buf.append(line);
	        }
	        if (HttpURLConnection.HTTP_OK != con.getResponseCode())
	        {
	        	throw new MyGengoException(String.format("Unexpected HTTP response: %i", con.getResponseCode()));
	        }
	        return buf.toString();
		}
		catch (Exception e)
		{
			throw new MyGengoException("HTTP PUT failed", e);
		}
    }


    /**
     * Configure and open an HTTP connection
     * @param method The HTTP method to use
     * @param url The URL to connect to (without query string)
     * @param queryString The query string
     * @return The HTTP connection
     * @throws MyGengoException if something went wrong :(
     */
    private HttpURLConnection createHttpConnection(HttpMethod method, String url, String queryString) throws MyGengoException
    {
    	HttpURLConnection con;
    	try
    	{
    		if (HttpMethod.GET == method || HttpMethod.DELETE == method)
    		{
    			url += "?" + queryString;
    		}
			URL u = new URL(url);
			con = (HttpURLConnection)u.openConnection();
			con.setRequestMethod(method.toString());
			con.setDoInput(true);
	        con.setUseCaches(false);
	        con.setRequestProperty("User-Agent", "myGengo Java library v" + VERSION);
	        con.setRequestProperty("accept", "application/json");
    	}
    	catch (Exception e)
    	{
    		throw new MyGengoException("Failed to create HTTP connection", e);
    	}		
    	return con;
    }
    
    /**
     * Send an HTTP request
     * @param method The HTTP method to use
     * @param url The URL to connect to (without query string)
     * @param queryString The query string
     * @return The response from the server
     * @throws MyGengoException
     */
	private String sendRequest(HttpMethod method, String url, String queryString) throws MyGengoException
	{
		HttpURLConnection con = createHttpConnection(method, url, queryString);
		switch (method)
		{
		case GET:
			con.setRequestProperty("Content-Length", Integer.toString(queryString.length()));
			return httpGet(con);
		case PUT:
			return httpPut(con, queryString);
		case POST:
			return httpPost(con, queryString);
		case DELETE:
		    return httpDelete(con);
		default:
			throw new MyGengoException("HTTP method " + method.toString() + " is not supported");
		}
	}
	
	/**
	 * Get an image from a specified URL with authentication and timestamp.
	 * @param url The URL to which authentication and timestamp parameters will be added.
	 * @return an image downloaded from the specified URL
	 * @throws MyGengoException
	 */
	protected BufferedImage getImage(String url) throws MyGengoException
	{
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("api_key", this.publicKey);
        parameters.put("ts", getTimestamp());
        parameters.put("api_sig", hmac(parameters.get("ts")));
        String queryString = makeQueryString(parameters);
        try
        {
            URL imgUrl = new URL(url + "?" + queryString);
            BufferedImage image = ImageIO.read(imgUrl);
            return image;
        } catch (MalformedURLException e)
        {
            throw new MyGengoException("Bad URL for image", e);
        } catch (IOException e)
        {
            // Call the JSON way to generate a more meaningful exception
            // The server side doesn't give meaningful errors yet.
            call(url, HttpMethod.GET);
            throw new MyGengoException("IO Exception loading image", e);
        }
	}
}
