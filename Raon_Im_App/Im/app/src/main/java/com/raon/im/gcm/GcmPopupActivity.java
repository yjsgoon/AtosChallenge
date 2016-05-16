package com.raon.im.gcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.raon.im.application.AppLockActivity;
import com.raon.lee.im.R;

/**
 * Created by EunBin on 2016-03-18.
 *
 * This is a pop-up activity that is shown when a company requires personal information
 */
public class GcmPopupActivity extends Activity {

    Button btnPopupYes, btnPopupNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_gcmpopup);

        Intent gcmIntent = getIntent();
        String type = gcmIntent.getStringExtra("GCM_TYPE");

        TextView txt = (TextView) findViewById(R.id.txt_gcmpopup);
        switch (type) {
            case "request" :
                txt.setText("A company has requested your personal data. Do you want to check now?");
                break;
            case "expiration" :
                txt.setText("There is a company whose expiration is 7 days left. Do you want to check now?");
                break;
            case "remove" :
                txt.setText("A company's provision has expired. Would you like to check?");
                break;
            default:
                break;
        }

        btnPopupYes = (Button) findViewById(R.id.btn_PopupYes);
        // if "Yes", application starts
        // but if "No", application does not starts
        btnPopupYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
                startActivity(new Intent(GcmPopupActivity.this, AppLockActivity.class));
                finish();
            }
        });
        btnPopupNo = (Button) findViewById(R.id.btn_PopupNo);
        btnPopupNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
