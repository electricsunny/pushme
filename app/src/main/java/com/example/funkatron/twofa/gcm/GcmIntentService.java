package com.example.funkatron.twofa.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.funkatron.twofa.MainActivity;
import com.example.funkatron.twofa.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

/**
 * Created by electricSunny on 13/10/2014.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
                handleNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
               /* sendNotification("Deleted messages on server: " +
                        extras.toString());*/
                handleNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                String messageJson = extras.getString("message");
                String message;
                int id;

                if (messageJson == null) {
                    message = "Notification message text was empty.";
                    id = 99;
                } else {
                    NotificationWrapper notificationWrapper = new Gson().fromJson(messageJson, NotificationWrapper.class);
                    id = notificationWrapper.getId();
                    message = notificationWrapper.getMessage();
                }
                Log.d("MESSAGE", message);
                showNotification(id, message);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void handleNotification(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    /*private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DemoActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_gcm)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }*/

     private void showNotification(int id, String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

         Bitmap bigLogoBm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
         Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
         long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};

         NotificationCompat.Builder builder =
                 new NotificationCompat.Builder(this)
                         .setSmallIcon(R.drawable.ic_launcher)
                         .setLargeIcon(bigLogoBm)
                         .setContentTitle("2FA")
                         .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                         .setContentText(msg)
                         //.setSound(alarmSound)
                         .setTicker(msg)
                         .setAutoCancel(true)
                         .setVibrate(pattern);

         builder.setContentIntent(contentIntent);
         mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}