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
import com.example.funkatron.twofa.TwoFactorAuthenticationReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

/**
 * Created by electricSunny on 13/10/2014.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;

    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();


        String message = extras.getString("message");

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
                String body;
                int id;

                if (messageJson == null) {
                    body = "Notification message text was empty.";
                    id = 99;
                } else {
                    NotificationWrapper notificationWrapper = new Gson().fromJson(messageJson, NotificationWrapper.class);
                    id = notificationWrapper.getId();
                    body = notificationWrapper.getBody();
                }
                Log.d("BODY", body);
                showNotification(id, body);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void showNotification(int id, String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent authoriseYesIntent = new Intent(this, TwoFactorAuthenticationReceiver.class);
        authoriseYesIntent.putExtra("authorise", "yes");

        Intent authoriseNoIntent = new Intent(this, TwoFactorAuthenticationReceiver.class);
        authoriseNoIntent.putExtra("authorise", "no");

        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 0, authoriseYesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(this, 0, authoriseNoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bigLogoBm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                                //.setLargeIcon(bigLogoBm)
                        .setContentTitle("Telstra ID Plus")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigLogoBm))
                        .setContentText(msg)
                                //.setSound(alarmSound)
                        .setTicker(msg)
                        .setAutoCancel(true)
                        .setVibrate(pattern)
                        .addAction(android.R.drawable.ic_menu_compass, "Yes", pendingIntentYes)
                        .addAction(android.R.drawable.ic_menu_close_clear_cancel, "No", pendingIntentNo);

        // should this be a plain old Intent?
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        builder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void handleNotification(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

   /* @Override
    protected void onHandleIntent(Intent intent) {
        Intent deleteIntent = new Intent(this, CancelUploadReceiver.class);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //building the notification
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_upload)
                .setContentTitle("Uploading Media...")
                .setTicker("Starting uploads")
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Cancel Upload", pendingIntentCancel);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setProgress(100, 0, true);

        startForeground(12345, mBuilder.build());

        for(int i=0;i<10;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }*/

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
}