package com.raon.im.application;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by EunBin on 2016-02-18.
 *
 * This class is called when the user presses the back button of the device.
 * Press twice to exit.
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;  // used to check the number of the pressed time
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    private void showGuide() {
        toast = Toast.makeText(activity, "Press back button twice to exit", Toast.LENGTH_SHORT);
        toast.show();
    }
}
