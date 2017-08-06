package com.repkap11.burger.activities.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.repkap11.burger.BurgerApplication;

public class ServiceInstanceIdChange extends FirebaseInstanceIdService {
    public ServiceInstanceIdChange() {
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        BurgerApplication.updateDeviceToken(this, true);
    }
}
