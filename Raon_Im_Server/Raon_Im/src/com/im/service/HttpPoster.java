package com.im.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

/**
 * Http class for the data sending to company
 * @since 	2016. 3. 21.
 * @version	1.0
 * @author 	Yoon JiSoo
 */
public class HttpPoster {
	private HttpClient client = HttpClientBuilder.create().build();
	private HttpPost httpPost = null;

	public HttpPoster(String url) {
		httpPost = new HttpPost(url);
	}

	/**
	 * the result(accept/deny) and if accept, send the user's personal data
	 * @Method	post
	 * @param	res		the result
	 * @param	userID		the user's ID
	 * @param	jsonObject	the personal data
	 */
	public void post(String res, String userID, JSONObject jsonObject) {
		try {
			List<NameValuePair> nameValuePairs = new ArrayList();
			nameValuePairs.add(new BasicNameValuePair("res", res));
			nameValuePairs.add(new BasicNameValuePair("userID", userID));
			
			/* if accept, send the data to the company
			 * if deny, send the result only */
			if(res.equals("accept") || res.equals("modify"))
				nameValuePairs.add(new BasicNameValuePair("requestData", jsonObject.toString()));

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			client.execute(httpPost);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
