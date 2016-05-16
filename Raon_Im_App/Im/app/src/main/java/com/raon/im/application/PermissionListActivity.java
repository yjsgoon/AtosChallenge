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
import com.raon.im.listview.PermissionListAdapter;
import com.raon.lee.im.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by lee on 2016-03-11.
 *
 * This activity shows the list of the companies requesting the personal data, not yet granted.
 */
public class PermissionListActivity extends Activity {
    private static final String TAG = "PermissionListActivity";

    private ConnectRequestReceiver receiver;
    private BackPressCloseHandler backPressCloseHandler;

    JSONArray jsonArray = new JSONArray();

    ArrayList<CompanyData> datas = new ArrayList<CompanyData>();  // gets the list of the data fields of each company requires
    PermissionListAdapter adapter;
    ListView list_CompanyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        // change column names to Agree and Disagree
        if (this.getClass() == PermissionListActivity.class) {
            TextView txt1 = (TextView) findViewById(R.id.txt_column1);
            TextView txt2 = (TextView) findViewById(R.id.txt_column2);
            txt1.setText("Agree");
            txt1.setWidth(50);
            txt2.setText("Disagree");
            txt2.setWidth(50);
        }

        list_CompanyList = (ListView) findViewById(R.id.listView);
        adapter = new PermissionListAdapter(getLayoutInflater(), datas);
        list_CompanyList.setAdapter(adapter);

        IntentFilter filter = new IntentFilter(ConnectRequestReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ConnectRequestReceiver();
        registerReceiver(receiver, filter);

        DatabaseSource databaseSource = new DatabaseSource(this);

        try {
            databaseSource.open();
            DataField dataField = databaseSource.getData_1();

            Intent connIntent = new Intent(this, ConnectIntentService.class);
            connIntent.putExtra(ConnectIntentService.REQUEST_STRING, TAG);
            connIntent.putExtra("service", "imwaitinglist");
            connIntent.putExtra("userID", dataField.getEmail());
            startService(connIntent);

            databaseSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public void presentation() {
        String companyAlias;
        String[] requestData;

        datas.clear();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                companyAlias = jsonObject.get("alias").toString();
                requestData = new String(jsonObject.get("requestData").toString()).split("/");

                Log.d(TAG, companyAlias);

                for (String str : requestData)
                    Log.d(TAG, str);

                datas.add(new CompanyData(companyAlias, false, requestData));
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

        if(datas.size() != 0) {
            IntentFilter filter = new IntentFilter(ConnectRequestReceiver.PROCESS_RESPONSE);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            receiver = new ConnectRequestReceiver();
            registerReceiver(receiver, filter);

            DatabaseSource databaseSource = new DatabaseSource(this);

            try {
                databaseSource.open();
                DataField dataField = databaseSource.getData_1();

                Intent connIntent = new Intent(this, ConnectIntentService.class);
                connIntent.putExtra(ConnectIntentService.REQUEST_STRING, TAG);
                connIntent.putExtra("service", "imwaitinglist");
                connIntent.putExtra("userID", dataField.getEmail());
                startService(connIntent);

                databaseSource.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

            Log.d(responseString, responseMessage);

            if (responseString.equals("imwaitinglist")) {
                try {
                    jsonArray = new JSONArray(responseMessage);
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