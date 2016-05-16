package com.raon.im.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.raon.im.application.AppLockActivity;
import com.raon.lee.im.R;

/**
 * Created by JiSoo on 2016-02-05.
 *
 * This is the service clsss that deals with the GCM alarm
 */
public class ImGcmListenerService extends GcmListenerService {
    private static final String TAG = "ImGcmListenerService";


     // @param from get SenderID
     // the payload of the data, in the type of @param data set, from GCM
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String type = data.getString("type");
        String message = data.getString("message");


        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "type: " + type);
        Log.d(TAG, "Message: " + message);

        // call sendNotification() method which alerts the device of the message from GCM
        sendNotification(title, type, message);
    }

    // used to let the device the message from GCM
    // appears on Notification Center of the device
    // @param title
    // @param type
    // @param message
    private void sendNotification(String title, String type, String message) {
        Intent intent = new Intent(this, AppLockActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.im_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


        Intent gcmIntent = new Intent(this, GcmPopupActivity.class);
        gcmIntent.putExtra("GCM_TYPE",type);
        gcmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(gcmIntent);
    }
}
