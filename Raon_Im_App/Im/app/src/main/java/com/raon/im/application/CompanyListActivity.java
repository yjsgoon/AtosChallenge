package com.raon.im.application;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.raon.im.connection.ConnectIntentService;
import com.raon.im.database.DataField;
import com.raon.im.database.DatabaseSource;
import com.raon.im.listview.CompanyData;
import com.raon.im.listview.CompanyListAdapter;
import com.raon.lee.im.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by EunBin on 2016-04-03.
 *
 * This activity shows the list of the companies
 * which are granted on personal information being provided
 */
public class CompanyListActivity extends Activity {
    private static final String TAG = "CompanyListActivity";

    private ConnectRequestReceiver receiver;
    private BackPressCloseHandler backPressCloseHandler;

    JSONArray jsonArray = new JSONArray();

    ArrayList<CompanyData> datas = new ArrayList<CompanyData>();  // list of the names of companies
    CompanyListAdapter adapter;  // adapter connected to the list
    ListView list_CompanyList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        // change column names to Timer
        if (this.getClass() == CompanyListActivity.class) {
            TextView txt1 = (TextView) findViewById(R.id.txt_column1);
            TextView txt2 = (TextView) findViewById(R.id.txt_column2);
            txt1.setText("Timer");
            txt1.setWidth(100);
            txt2.setText("");
            txt2.setWidth(0);
        }

        list_CompanyList = (ListView) findViewById(R.id.listView);
        adapter = new CompanyListAdapter(getLayoutInflater(), datas);
        list_CompanyList.setAdapter(adapter);

        IntentFilter filter = new IntentFilter(ConnectRequestReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ConnectRequestReceiver();
        registerReceiver(receiver, filter);

        DatabaseSource databaseSource = new DatabaseSource(this);

        try {
            databaseSource.open();
            DataField dataField = databaseSource.getData_1();

            Intent intent = new Intent(this, ConnectIntentService.class);
            intent.putExtra(ConnectIntentService.REQUEST_STRING, TAG);
            intent.putExtra("service", "imcompanylist");
            intent.putExtra("userID", dataField.getEmail());
            startService(intent);

            databaseSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public void presentation() {
        String companyAlias;
        String[] requestData;
        String timer;

        datas.clear();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                companyAlias = jsonObject.get("alias").toString();
                requestData = new String(jsonObject.get("requestData").toString()).split("/");
                timer = jsonObject.get("timer").toString();

                Log.d(TAG, companyAlias);
                Log.d(TAG, timer);
                for (String str : requestData)
                    Log.d(TAG, str);

                datas.add(new CompanyData(companyAlias, false, requestData, timer));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    // Renew the list when activity is used more than one time
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(ConnectRequestReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ConnectRequestReceiver();
        registerReceiver(receiver, filter);

        DatabaseSource databaseSource = new DatabaseSource(this);

        try {
            databaseSource.open();
            DataField dataField = databaseSource.getData_1();

            Intent intent = new Intent(this, ConnectIntentService.class);
            intent.putExtra(ConnectIntentService.REQUEST_STRING, TAG);
            intent.putExtra("service", "imcompanylist");
            intent.putExtra("userID", dataField.getEmail());
            startService(intent);

            databaseSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class ConnectRequestReceiver extends BroadcastReceiver {
        public static final String PROCESS_RESPONSE = "com.raon.im.intent.action.PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseString = intent.getStringExtra(ConnectIntentService.RESPONSE_STRING);
            String responseMessage = intent.getStringExtra(ConnectIntentService.RESPONSE_MESSAGE);

            Log.d(responseString, responseMessage.toString());

            if (responseString.equals("imcompanylist")) {
                try {
                    jsonArray = new JSONArray(responseMessage.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                presentation();
            }
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
