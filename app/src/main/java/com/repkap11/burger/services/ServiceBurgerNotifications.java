package com.repkap11.burger.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.SignInFractivity;

import java.util.Map;

public class ServiceBurgerNotifications extends FirebaseMessagingService {

    private static final String TAG = ServiceBurgerNotifications.class.getSimpleName();

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> map = remoteMessage.getData();
        sendNotification(map.get("title"), map.get("body"));
    }

    private void sendNotification(String title, String body) {
        Log.e(TAG, "Notification Message Title: " + title + " body:" + body);

        Intent intent = new Intent(this, SignInFractivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.burger_notification_icon)
                .setContentTitle(title == null ? getResources().getString(R.string.app_name) : title)
                //.setSubText(body)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{0, 300, 300, 300})
                .setLights(Color.RED, 1000, 1000)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorAccent, getTheme()));
        } else {
            notificationBuilder.setColor(getResources().getColor(R.color.colorAccent));
        }
        //

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}
