package com.raon.im.connection;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.raon.im.application.CompanyListActivity;
import com.raon.im.application.PermissionListActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public static final String REQUEST_STRING = "connectRequest";
public static final String RESPONSE_STRING = "connectResponse";
public static final String RESPONSE_MESSAGE = "connectResponseMessage";

private String requestString;
private String responseString;
private String responseMessage;

/**
 * Created by JiSoo on 2016-02-05.
 *
 * This class is the Intent Service to communicate with I'm Server and to get service
 */
public class ConnectIntentService extends IntentService {
    private static final String TAG = "ConnectIntentService";

    private HttpClient httpClient = new DefaultHttpClient();
    private HttpPost httpPost = null;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String url;
    private String service;
    private Intent intent;

    public ConnectIntentService() {
        super(TAG);
    }

    // used to set needed variables for communication with server
    // store parameter to send to @param intent server
    @Override
    protected void onHandleIntent(Intent intent) {
        this.service = intent.getStringExtra("service");
        this.url = "http://220.149.236.22:8080/im/" + service; // static IP of the server
        this.intent = intent;

        SystemClock.sleep(100);

        connect();
    }

    // check the service type
    public void connect() {
        try {
            if (service.equals("signup")) {
                requestSignup();
            } else if (service.equals("immodify")) {
                requestImModify();
            } else if (service.equals("imresponse")) {
                requestImresponse();
            } else if (service.equals("expirationreset")) {
                requestExpirationreset();
            } else if (service.equals("imwaitinglist")) {
                requestImWaitingList();
            } else if (service.equals("imcompanylist")) {
                requestImCompanyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // service for the sign up process
    // user's e-mail, password, GCM ID are sent to the server
    private void requestSignup() {
        try {
            httpPost = new HttpPost(url);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("userID", intent.getStringExtra("userID"));
            jsonObject.put("userPW", intent.getStringExtra("userPW"));
            jsonObject.put("gcmID", intent.getStringExtra("gcmID"));

            nameValuePairs.add(new BasicNameValuePair("information", jsonObject.toString()));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // service for the modification process of personal data
    private void requestImModify() {
        try {
            httpPost = new HttpPost(url);

            nameValuePairs.add(new BasicNameValuePair("userID", intent.getStringExtra("userID")));
            nameValuePairs.add(new BasicNameValuePair("modifyData", intent.getStringExtra("modifyData")));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // send the user's response of the company's personal data request to the server
    private void requestImresponse() {
        try {
            httpPost = new HttpPost(url);

            nameValuePairs.add(new BasicNameValuePair("userID", intent.getStringExtra("userID")));
            nameValuePairs.add(new BasicNameValuePair("alias", intent.getStringExtra("alias")));

            // if user accepts on sending his personal data, the decision(accept) and the personal data are sent
            if (intent.getStringExtra("res").equals("accept")) {
                nameValuePairs.add(new BasicNameValuePair("res", "accept"));
                nameValuePairs.add(new BasicNameValuePair("personalData",
                        intent.getStringExtra("personalData")));
            } else {
                nameValuePairs.add(new BasicNameValuePair("res", "deny"));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // send the updated timer to modify the timer for the company
    private void requestExpirationreset() {
        try {
            httpPost = new HttpPost(url);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("year", intent.getIntExtra("year", 2016));
            jsonObject.put("month", intent.getIntExtra("month", 4));
            jsonObject.put("date", intent.getIntExtra("date", 24));

            nameValuePairs.add(new BasicNameValuePair("userID", intent.getStringExtra("userID")));
            nameValuePairs.add(new BasicNameValuePair("alias", intent.getStringExtra("alias")));
            nameValuePairs.add(new BasicNameValuePair("expirationReset", jsonObject.toString()));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // get the list of the companies requesting personal infromation and the lsit of the data fields they requires
    private void requestImWaitingList() {
        try {
            httpPost = new HttpPost(url);

            nameValuePairs.add(new BasicNameValuePair("userID", intent.getStringExtra("userID")));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity httpEntity = response.getEntity();
            InputStream inputStream = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            responseMessage = jsonObject.get("res").toString();

            Log.d(TAG, "imRequest :" + jsonObject.get("msg"));
            Log.d(TAG, jsonObject.get("res").toString());

            responseString = "imwaitinglist";

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(PermissionListActivity.ConnectRequestReceiver.PROCESS_RESPONSE);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(RESPONSE_STRING, responseString);
            broadcastIntent.putExtra(RESPONSE_MESSAGE, responseMessage);
            sendBroadcast(broadcastIntent);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // get the list of the companies which the user is interacting with and the list of the data fields the company requires
    private void requestImCompanyList() {
        try {
            httpPost = new HttpPost(url);

            nameValuePairs.add(new BasicNameValuePair("userID", intent.getStringExtra("userID")));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity httpEntity = response.getEntity();
            InputStream inputStream = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            responseMessage = jsonObject.get("res").toString();

            Log.d(TAG, "imRequest :" + jsonObject.get("msg"));

            responseString = "imcompanylist";

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(CompanyListActivity.ConnectRequestReceiver.PROCESS_RESPONSE);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(RESPONSE_STRING, responseString);
            broadcastIntent.putExtra(RESPONSE_MESSAGE, responseMessage);
            sendBroadcast(broadcastIntent);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}