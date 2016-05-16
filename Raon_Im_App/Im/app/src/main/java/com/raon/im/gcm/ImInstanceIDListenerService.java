package com.raon.im.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by JiSoo on 2016-02-04.
 *
 * This class starts RegistrationIntentService when InstanceID is refreshed.
 */
public class ImInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = "InstanceIDLS";

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
