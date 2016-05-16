package com.raon.im.gcm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.raon.im.connection.ConnectIntentService;
import com.raon.im.database.DataField;
import com.raon.im.database.DatabaseSource;
import com.raon.lee.im.R;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by JiSoo on 2016-02-04.
 *
 * This is the service class to register GCM
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegistrationIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    // creates a token of Instance ID for GCM
    // @param intent
    @SuppressLint("LongLogTag")
    @Override
    protected void onHandleIntent(Intent intent) {

        // get the Instance ID for GCM
        InstanceID instanceID = InstanceID.getInstance(this);
        String gcmID = null;
        try {
            synchronized (TAG) {
                // get the SenderID automatically based on google-serviced.json file which is able to be obtained after registration of GCM application
                String default_senderId = getString(R.string.gcm_defaultSenderId);
                // default scope of GCM is "GCM"
                String scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;
                // create the token for the Instance ID and get the token
                gcmID = instanceID.getToken(default_senderId, scope, null);

                Log.i(TAG, "GCM Registration Token: " + gcmID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestSignup(gcmID);
    }

    // use ConnentIntentServie to send the sign up information  to the server
    // @param gcmID
    private void requestSignup(String gcmID) {
        DatabaseSource databaseSource = new DatabaseSource(this);

        try {
            databaseSource.open();
            DataField dataField = databaseSource.getData_1();

            Intent intent = new Intent(this, ConnectIntentService.class);
            intent.putExtra("service", "signup");
            intent.putExtra("userID", dataField.getEmail());
            intent.putExtra("userPW", dataField.getPassword());
            intent.putExtra("gcmID", gcmID);
            startService(intent);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

