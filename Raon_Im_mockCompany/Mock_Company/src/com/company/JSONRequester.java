package com.company;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Yoo on 2016-03-13.
 */
public class JSONRequester {

    public static HttpResponse executePost(String url, JSONObject jSONObject, String returnAddress, String alias,
                                           String ImID) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        HttpEntity httpEntity = null;

        String param = "requestData" + jSONObject.toString();

        List<NameValuePair> nameValuePairs = new ArrayList();
        nameValuePairs.add(new BasicNameValuePair("returnAddress", returnAddress));
        nameValuePairs.add(new BasicNameValuePair("alias", alias));
        nameValuePairs.add(new BasicNameValuePair("ImID", ImID));
        nameValuePairs.add(new BasicNameValuePair("requestData", jSONObject.toString()));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}