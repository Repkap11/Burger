package com.repkap11.burger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.repkap11.burger.activities.LunchGroupsFractivity;

import java.util.UUID;

/**
 * Created by paul on 8/1/17.
 */


public class BurgerApplication extends Application {
    public static final String BURGER_USER_GROUP_PREF = "users_prefered_group";
    private static final String BURGER_GUID = "burger-user-guid";

    private static final String TAG = BurgerApplication.class.getSimpleName();

    private String mUniqueID;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = this.getSharedPreferences(BURGER_GUID, Context.MODE_PRIVATE);
        mUniqueID = prefs.getString(BURGER_GUID, null);
        if (mUniqueID == null) {
            mUniqueID = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(BURGER_GUID, mUniqueID);
            editor.commit();
            Log.e(TAG, "Creating new guid:" + mUniqueID);
        } else {
            Log.e(TAG, "Re-using guid:" + mUniqueID);
        }
    }

    public static String readUserPerferedGroup(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(BurgerApplication.BURGER_USER_GROUP_PREF, Context.MODE_PRIVATE);
        String pref = prefs.getString(BurgerApplication.BURGER_USER_GROUP_PREF, null);
        return pref;
    }


    public static void writeUserPerferedGroup(Context context, String value){
        SharedPreferences prefs = context.getSharedPreferences(BurgerApplication.BURGER_USER_GROUP_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BurgerApplication.BURGER_USER_GROUP_PREF, value);
        editor.apply();
    }

    public String getUserGuid() {
        return mUniqueID;
    }
}
