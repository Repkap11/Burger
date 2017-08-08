package com.repkap11.burger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.repkap11.burger.BurgerApplication;

public class ServiceInstanceIdChange extends FirebaseInstanceIdService {
    private static final String TAG = ServiceInstanceIdChange.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.e(TAG, "Token Changed!");
        boolean notificationsEnabled = BurgerApplication.getUserPerferedNotoficationsEnabled(this);
        BurgerApplication.updateDeviceToken(this, notificationsEnabled);
    }
}
