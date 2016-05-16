package com.raon.im.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.raon.lee.im.R;

/**
 * Created by Na Young on 2016-04-08.
 *
 * This activity shows the other functions of this application.
 */
public class OptionActivity extends Activity implements OnItemClickListener {

    private boolean aboutView = false;
    // if true, R.layout.activity_about is on display
    // if false, R.layout.activity_option is on display

    private BackPressCloseHandler backPressCloseHandler;

    private String[] optionList = {
            "About",
            "Change Password",
            "Logout"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionList);

        ListView list = (ListView) findViewById(R.id.list_OptionList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(this);

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String s = optionList[position];

        if (s.equals("About")) {
            aboutView = true;
            setContentView(R.layout.activity_about);
        } else if (s.equals("Change Password")) {
            Intent intent = new Intent(OptionActivity.this, CheckCurrentAppPWActivity.class);
            startActivity(intent);
            finish();
        } else if (s.equals("Logout")) {
            SharedPreferences setting = getSharedPreferences("setting", 0);
            SharedPreferences.Editor editor = setting.edit();

            editor.putBoolean("isNotAutoLoginEnabled", false);
            editor.commit();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // if current view is set to R.layout.activity_about, goes to R.layout.activity_option
        if(aboutView) {
            aboutView = false;
            setContentView(R.layout.activity_option);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionList);

            ListView list = (ListView) findViewById(R.id.list_OptionList);
            list.setAdapter(adapter);

            list.setOnItemClickListener(this);
        } else
            backPressCloseHandler.onBackPressed();
    }
}
