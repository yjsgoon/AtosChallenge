package com.raon.im.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raon.im.database.DatabaseSource;
import com.raon.lee.im.R;

import java.sql.SQLException;

/**
 * Created by Na Young on 2016-01-24.
 * Last Update 2016-02-04.
 *
 * This activity lets the user to log in to I'm application.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    EditText editEmail;
    EditText editPassword;
    Button btnSignIn;
    CheckBox cbAutoLogin;
    TextView btnSignUp;

    SharedPreferences setting;  // used for auto login function
    SharedPreferences.Editor editor; // used for auto login function

    private DatabaseSource databaseSource; // used to check whether the E-mail and Password the user fills in are in the database
    private BackPressCloseHandler backPressCloseHandler;

    private static boolean hasSignedUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText) findViewById(R.id.edit_Login_Email);
        editPassword = (EditText) findViewById(R.id.edit_Login_Password);
        btnSignIn = (Button) findViewById(R.id.btn_SignIn);
        btnSignIn.setOnClickListener(this);
        cbAutoLogin = (CheckBox) findViewById(R.id.cb_Auto_Login);
        btnSignUp = (TextView) findViewById(R.id.txt_SignUp);
        btnSignUp.setOnClickListener(this);

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();
        
        // to check whether the auto login is checked or not.
        // if checked, true is stored to enable auto login
        // if not, false is stored to disable auto login
        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("isNotAutoLoginEnabled", true);
                    editor.commit();
                } else {
                    editor.putBoolean("isNotAutoLoginEnabled", false);
                    editor.commit();
                }
            }
        });

        // if the checkbox has been already checked, the activity jumps to the MainActivity
        if (setting.getBoolean("isNotAutoLoginEnabled", false)) {
            Intent intent = new Intent(LoginActivity.this, TabMainActivity.class);
            startActivity(intent);
            finish();
        }

        databaseSource = new DatabaseSource(this);
        try {
            databaseSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_SignIn:
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                // search through the database to
                if (databaseSource.isRightPassword(email, password)) {
                    Intent intent = new Intent(this, TabMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Wrong Email/Password!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_SignUp:
                hasSignedUp = true;
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                finish();
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
