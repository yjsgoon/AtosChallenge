package com.raon.im.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.raon.im.connection.ConnectIntentService;
import com.raon.im.database.DatabaseSource;
import com.raon.lee.im.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by Na Young on 2016-03-22.
 *
 * This activity lets a user to modify the information on the database.
 */
public class ModifyDataActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "ModifyDataActivity";

    EditText edit_UserName, edit_UserCity, edit_UserAddress, edit_UserPhone, edit_BirthdayYear, edit_BirthdayMonth, edit_BirthdayDay;
    TextView btn_UserCountry;
    Button btn_Confirm;

    private DatabaseSource databaseSource; // used to get the data from the database and set the data to the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        edit_UserName = (EditText) findViewById(R.id.edit_UserName);
        btn_UserCountry = (TextView) findViewById(R.id.txt_UserCountry);
        btn_UserCountry.setOnClickListener(this);
        edit_UserCity = (EditText) findViewById(R.id.edit_UserCity);
        edit_UserAddress = (EditText) findViewById(R.id.edit_UserAddress);
        edit_UserPhone = (EditText) findViewById(R.id.edit_UserPhone);
        edit_BirthdayYear = (EditText) findViewById(R.id.edit_BirthdayYear);
        edit_BirthdayMonth = (EditText) findViewById(R.id.edit_BirthdayMonth);
        edit_BirthdayDay = (EditText) findViewById(R.id.edit_BirthdayDay);

        btn_Confirm = (Button) findViewById(R.id.btn_confirm);
        btn_Confirm.setOnClickListener(this);

        // open the database and get the data in the database
        databaseSource = new DatabaseSource(this);
        try {
            databaseSource.open();
            edit_UserName.setText(databaseSource.getData_1().getName());
            btn_UserCountry.setText(databaseSource.getData_1().getCountry());
            edit_UserCity.setText(databaseSource.getData_1().getCity());
            edit_UserAddress.setText(databaseSource.getData_1().getAddress());
            edit_UserPhone.setText(databaseSource.getData_1().getPhone());
            edit_BirthdayYear.setText(databaseSource.getData_1().getBirthday_year());
            edit_BirthdayMonth.setText(databaseSource.getData_1().getBirthday_month());
            edit_BirthdayDay.setText(databaseSource.getData_1().getBirthday_day());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // if the user presses "Yes", the data modified are updated in the database
            // if "No", the data are not changed and the page goes to the MainActivity
            case R.id.btn_confirm:
                new AlertDialog.Builder(this)
                        .setTitle("Save data?")
                        .setIcon(R.drawable.im_logo)
                        .setMessage("Do you want to save the updated data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
            /* User clicked OK so do some stuff */
                                String inputName = edit_UserName.getText().toString();
                                String inputCounty = btn_UserCountry.getText().toString();
                                String inputCity = edit_UserCity.getText().toString();
                                String inputAddress = edit_UserAddress.getText().toString();
                                String inputPhone = edit_UserPhone.getText().toString();
                                String inputBirthday_year = edit_BirthdayYear.getText().toString();
                                String inputBirthday_month = edit_BirthdayMonth.getText().toString();
                                String inputBirthday_day = edit_BirthdayDay.getText().toString();
                                String email = databaseSource.getData_1().getEmail();
                                databaseSource.updateData(inputName, inputCounty, inputCity, inputAddress, inputPhone, inputBirthday_year, inputBirthday_month, inputBirthday_day, email);
                                databaseSource.close();

                                // let the server know so that the server can send the updated data to the companies the user is having interaction with
                                try {
                                    JSONObject jsonObject = new JSONObject();

                                    jsonObject.put("name", inputName);
                                    jsonObject.put("country", inputCounty);
                                    jsonObject.put("city", inputCity);
                                    jsonObject.put("address", inputAddress);
                                    jsonObject.put("phone", inputPhone);
                                    jsonObject.put("birthdayYear", inputBirthday_year);
                                    jsonObject.put("birthdayMonth", inputBirthday_month);
                                    jsonObject.put("birthdayDay", inputBirthday_day);

                                    Intent connIntent = new Intent(ModifyDataActivity.this, ConnectIntentService.class);
                                    connIntent.putExtra(ConnectIntentService.REQUEST_STRING, TAG);
                                    connIntent.putExtra("service", "immodify");
                                    connIntent.putExtra("userID", email);
                                    connIntent.putExtra("modifyData", jsonObject.toString());
                                    startService(connIntent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(ModifyDataActivity.this, TabMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(ModifyDataActivity.this, TabMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .create().show();
                break;
            // shows the user list of the countries to select
            case R.id.txt_UserCountry:
                final ArrayList<String> nations = getNationList();
                final String[] items = nations.toArray(new String[nations.size()]);
                new AlertDialog.Builder(ModifyDataActivity.this)
                        .setTitle("Select your nation")
                        .setIcon(R.drawable.im_logo)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                btn_UserCountry.setText(items[item]);
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
        }
    }

    // used to get the country list from the excel spreadsheet
    public ArrayList<String> getNationList() {
        ArrayList<String> nationList = new ArrayList<String>();

        try {
            AssetManager am = getAssets();
            InputStream is = am.open("nation_name.xls");
            Workbook wb = Workbook.getWorkbook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            int col = s.getColumns();

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    Cell c = s.getCell(j, i);
                    nationList.add(c.getContents());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nationList;
    }

    // If ther user presses back button user is asked to whether to store the data or not
    // if the user presses "Yes", the data modified are updated in the database
    // if "No", the data are not changed and the page goes to the MainActivity
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Save data?")
                .setIcon(R.drawable.im_logo)
                .setMessage("You didn't save your updated data. Do you want to save the updated data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    /* User clicked OK so do some stuff */
                        String inputName = edit_UserName.getText().toString();
                        String inputCounty = btn_UserCountry.getText().toString();
                        String inputCity = edit_UserCity.getText().toString();
                        String inputAddress = edit_UserAddress.getText().toString();
                        String inputPhone = edit_UserPhone.getText().toString();
                        String inputBirthday_year = edit_BirthdayYear.getText().toString();
                        String inputBirthday_month = edit_BirthdayMonth.getText().toString();
                        String inputBirthday_day = edit_BirthdayDay.getText().toString();
                        String email = databaseSource.getData_1().getEmail();
                        databaseSource.updateData(inputName, inputCounty, inputCity, inputAddress, inputPhone, inputBirthday_year, inputBirthday_month, inputBirthday_day, email);
                        databaseSource.close();

                        Intent intent = new Intent(ModifyDataActivity.this, TabMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(ModifyDataActivity.this, TabMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .create().show();
    }
}
