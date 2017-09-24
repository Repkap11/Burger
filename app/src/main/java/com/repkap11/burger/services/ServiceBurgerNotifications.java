package com.repkap11.burger.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.repkap11.burger.BurgerApplication;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.SettingsActivity;
import com.repkap11.burger.activities.SignInFractivity;

import java.util.Map;
import java.util.Random;

public class ServiceBurgerNotifications extends FirebaseMessagingService {

    private static final String TAG = ServiceBurgerNotifications.class.getSimpleName();

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> map = remoteMessage.getData();
        boolean notificationsEnabled = BurgerApplication.getUserPerferedNotoficationsEnabled(this);
        if (notificationsEnabled) {
            String title = map.get("title");
            String body = map.get("body");
            if (title == null && body == null) {
                RemoteMessage.Notification notification = remoteMessage.getNotification();
                if (notification != null) {
                    title = notification.getTitle();
                    body = notification.getBody();
                }
            }
            sendNotification(title, body);
        }
    }

    private void sendNotification(String title, String body) {
        Log.e(TAG, "Notification Message Title: " + title + " body:" + body);

        Intent intent = new Intent(this, SignInFractivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enableLED = prefs.getBoolean(SettingsActivity.PREF_NOTIFICATIONS_LED, true);
        String ringToneURI = prefs.getString(SettingsActivity.PREF_NOTIFICATIONS_RINGTONE, getResources().getString(R.string.pref_notifications_ringtone_default));
        boolean enableVibrate = prefs.getBoolean(SettingsActivity.PREF_NOTIFICATIONS_VIBRATE, true);
        Uri ringtoneUri = Uri.parse(ringToneURI);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.burger_notification_icon)
                .setContentTitle(title == null ? getResources().getString(R.string.app_name) : title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(ringtoneUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH);
        if (enableVibrate) {
            notificationBuilder.setVibrate(new long[]{0, 300, 300, 300});
        }
        if (enableLED) {
            notificationBuilder.setLights(Color.RED, 1000, 1000);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorAccent, getTheme()));
        } else {
            notificationBuilder.setColor(getResources().getColor(R.color.colorAccent));
        }
        //

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int randomId = new Random().nextInt();
        notificationManager.notify(randomId, notificationBuilder.build());
    }


}
