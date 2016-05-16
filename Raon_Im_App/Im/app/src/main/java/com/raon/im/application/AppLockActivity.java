package com.raon.im.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.raon.im.database.DatabaseSource;
import com.raon.lee.im.R;

import java.sql.SQLException;

/**
 * Created by EunBin on 2016-01-19.
 *
 * This activity lets a user to unlock the application lock.
 */
public class AppLockActivity extends Activity {

    private int[] inputPassword = {-1, -1, -1, -1};  // the numbers user presses
    private int[] appPassword = {0, 0, 0, 0};  // actual password
    private int pwCursor = 0;  // the cursor of the input password
    private String inputPW_String, appPW_String;

    Button[] btn_appPW = new Button[11];  // the buttons from 0 to 9 and back button
    EditText[] btn_edtPW = new EditText[4];

    private DatabaseSource databaseSource;  // get actual password from the database
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        startActivity(new Intent(this, SplashActivity.class));



        btn_appPW[0] = (Button) findViewById(R.id.btn_appPW_0);
        btn_appPW[1] = (Button) findViewById(R.id.btn_appPW_1);
        btn_appPW[2] = (Button) findViewById(R.id.btn_appPW_2);
        btn_appPW[3] = (Button) findViewById(R.id.btn_appPW_3);
        btn_appPW[4] = (Button) findViewById(R.id.btn_appPW_4);
        btn_appPW[5] = (Button) findViewById(R.id.btn_appPW_5);
        btn_appPW[6] = (Button) findViewById(R.id.btn_appPW_6);
        btn_appPW[7] = (Button) findViewById(R.id.btn_appPW_7);
        btn_appPW[8] = (Button) findViewById(R.id.btn_appPW_8);
        btn_appPW[9] = (Button) findViewById(R.id.btn_appPW_9);
        btn_appPW[10] = (Button) findViewById(R.id.btn_appPW_back);

        btn_edtPW[0] = (EditText) findViewById(R.id.edt_PW_1);
        btn_edtPW[1] = (EditText) findViewById(R.id.edt_PW_2);
        btn_edtPW[2] = (EditText) findViewById(R.id.edt_PW_3);
        btn_edtPW[3] = (EditText) findViewById(R.id.edt_PW_4);

        if(isAirplaneModeOn(this)) {
            for (Button button:btn_appPW) {
                button.setEnabled(false);
            }

            Toast.makeText(this,"Sorry, it's airplane mode now.", Toast.LENGTH_LONG).show();
        }

        databaseSource = new DatabaseSource(this);
        try {
            databaseSource.open();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        backPressCloseHandler = new BackPressCloseHandler(this);

        appPW_String = databaseSource.getData_2().getAppPW();
    }

    public void mOnClick(View view) throws InterruptedException {

        Button btn = (Button) view;
        boolean pwCheck = true;

        // Password input
        switch (btn.getId()) {
            case R.id.btn_appPW_back:
                if (pwCursor > 0) {
                    btn_edtPW[pwCursor].setText("");
                    pwCursor--;
                }
                break;
            case R.id.btn_appPW_0:
                inputPassword[pwCursor] = 0;
                btn_edtPW[pwCursor].setText("0");
                pwCursor++;
                break;
            case R.id.btn_appPW_1:
                inputPassword[pwCursor] = 1;
                btn_edtPW[pwCursor].setText("1");
                pwCursor++;
                break;
            case R.id.btn_appPW_2:
                inputPassword[pwCursor] = 2;
                btn_edtPW[pwCursor].setText("2");
                pwCursor++;
                break;
            case R.id.btn_appPW_3:
                inputPassword[pwCursor] = 3;
                btn_edtPW[pwCursor].setText("3");
                pwCursor++;
                break;
            case R.id.btn_appPW_4:
                inputPassword[pwCursor] = 4;
                btn_edtPW[pwCursor].setText("4");
                pwCursor++;
                break;
            case R.id.btn_appPW_5:
                inputPassword[pwCursor] = 5;
                btn_edtPW[pwCursor].setText("5");
                pwCursor++;
                break;
            case R.id.btn_appPW_6:
                inputPassword[pwCursor] = 6;
                btn_edtPW[pwCursor].setText("6");
                pwCursor++;
                break;
            case R.id.btn_appPW_7:
                inputPassword[pwCursor] = 7;
                btn_edtPW[pwCursor].setText("7");
                pwCursor++;
                break;
            case R.id.btn_appPW_8:
                inputPassword[pwCursor] = 8;
                btn_edtPW[pwCursor].setText("8");
                pwCursor++;
                break;
            case R.id.btn_appPW_9:
                inputPassword[pwCursor] = 9;
                btn_edtPW[pwCursor].setText("9");
                pwCursor++;
                break;
        }

        // Password Check
        if (pwCursor == 4) {
            inputPW_String = "";
            for (int i = 0; i < 4; i++)
                inputPW_String += inputPassword[i];

            if (appPW_String == null) {
                appPW_String = "";
                for (int i = 0; i < 4; i++)
                    appPW_String += String.valueOf(appPassword[i]);
                databaseSource.insertAppPW(appPW_String);
            }

            if (!inputPW_String.equals(appPW_String)) {
                Toast.makeText(AppLockActivity.this, "fail!", Toast.LENGTH_SHORT).show();
                pwCursor = 0;
                for (int i = 0; i < 4; i++)
                    btn_edtPW[i].setText("");
                pwCheck = false;
            }
        }

        if (pwCursor == 4 && pwCheck == true) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    /* Gets the state of Airplane Mode. */
    private static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }
}
