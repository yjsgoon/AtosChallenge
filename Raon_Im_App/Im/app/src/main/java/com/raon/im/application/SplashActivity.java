package com.raon.im.application;

import android.app.Activity;
import android.os.Bundle;

import com.raon.lee.im.R;

/**
 * Created by EunBin on 2016-01-30.
 *
 * This activity is the first page of the application which shows our logo.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timerThread.start();
    }
}
