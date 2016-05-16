package com.raon.im.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raon.im.database.DatabaseSource;
import com.raon.lee.im.R;

import java.sql.SQLException;

/**
 * Created by Na Young on 2016-03-20.
 *
 * This activity is called when a user clicks "ChangePassword" list on the "Option" tab.
 * Before changing the application lock password, the user has to check his current password.
 */
public class CheckCurrentAppPWActivity extends Activity {

    private int[] inputPassword = {-1, -1, -1, -1};  // the numbers user presses
    private String appPassword; // actual password
    private int pwCursor = 0;  // the cursor of the input password
    private String inputPW_String;

    Button[] btn_appPW = new Button[11];  //the buttons from 0 to 9 and back button
    EditText[] btn_edtPW = new EditText[4];

    private DatabaseSource databaseSource;  // get actual password from the database
    TextView txt_desc; // text to show the description of the activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        txt_desc = (TextView) findViewById(R.id.txt_desc);
        txt_desc.setText("Current Password");
        txt_desc.setTextSize(30);

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

        databaseSource = new DatabaseSource(this);
        try {
            databaseSource.open();

            appPassword = databaseSource.getData_2().getAppPW();
            Log.i("CHECK CURRENT PW", appPassword);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mOnClick(View view) throws InterruptedException {

        Button btn = (Button) view;
        boolean pwCheck = true;

        // Password input
        switch (btn.getId()) {
            case R.id.btn_appPW_back:
                if (pwCursor >= 0) {
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

            Log.i("PW in CHECKCURRENTPW", inputPW_String);

            if (!inputPW_String.equals(appPassword)) {
                Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
                pwCursor = 0;
                pwCheck = false;
                for (int i = 0; i < 4; i++)
                    btn_edtPW[i].setText("");
            }
        }

        if (pwCursor == 4 && pwCheck == true) {
            Intent intent = new Intent(this, SetNewAppPWActivity.class);
            intent.putExtra("OLD_PW", inputPW_String);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        ;
    }
}
