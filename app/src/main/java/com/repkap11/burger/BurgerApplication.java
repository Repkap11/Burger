package com.repkap11.burger;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.repkap11.burger.activities.LunchGroupsFractivity;

import java.util.UUID;

/**
 * Created by paul on 8/1/17.
 */


public class BurgerApplication extends Application {
    public static final String BURGER_USER_GROUP_PREF = "users_prefered_group";

    private static final String TAG = BurgerApplication.class.getSimpleName();

    public static String readUserPerferedGroup(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(BurgerApplication.BURGER_USER_GROUP_PREF, Context.MODE_PRIVATE);
        String pref = prefs.getString(BurgerApplication.BURGER_USER_GROUP_PREF, null);
        return pref;
    }


    public static void writeUserPerferedGroup(Context context, String value) {
        SharedPreferences prefs = context.getSharedPreferences(BurgerApplication.BURGER_USER_GROUP_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BurgerApplication.BURGER_USER_GROUP_PREF, value);
        editor.apply();
    }

    public static String getAppVersionName(Context context) {
        String versionString = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0 /* basic info */);
            versionString = info.versionName;
        } catch (Exception e) {
        }
        return versionString;
    }

    public static void showUpdateDialogIfNecessary(Activity activity) {
        try {
            final SharedPreferences prefs = activity.getSharedPreferences("CHANGELOG", Context.MODE_PRIVATE);
            final int currentVersionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
            boolean hasShownPrevious = prefs.getBoolean("has-shown-prefs-" + (currentVersionCode - 1), false);
            boolean hasShownCurrent = prefs.getBoolean("has-shown-prefs-" + currentVersionCode, false);
            //Log.e(TAG, "Neg previous:" + hasShownPrevious + " current:" + hasShownCurrent);
            //Log.e(TAG, "hasShownPrevious:" + hasShownPrevious + " hasShownCurrent:" + hasShownCurrent);
            SharedPreferences.Editor editor = prefs.edit();
            if (!hasShownPrevious) {
                //If we didn't show them last time, don't show them now.
                //that means the user just insatlled the app, and doesn't need a change log
                editor.putBoolean("has-shown-prefs-" + currentVersionCode, true);
            }
            editor.apply();


            if ((hasShownPrevious && !hasShownCurrent)) {
                final AlertDialog d = new AlertDialog.Builder(activity)
                        .setTitle("Changelog: App Version " + currentVersionCode)
                        .setMessage(activity.getResources().getString(R.string.changelog_message))
                        .setCancelable(false)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                writePrefOnDismiss(prefs, currentVersionCode);
                            }
                        }).setOnKeyListener(new Dialog.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
                                // TODO Auto-generated method stub
                                if (keyCode == KeyEvent.KEYCODE_BACK &&
                                        event.getAction() == KeyEvent.ACTION_UP) {
                                    writePrefOnDismiss(prefs, currentVersionCode);
                                    dialogInterface.dismiss();
                                    return true;
                                }
                                return false;
                            }
                        }).show();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void writePrefOnDismiss(SharedPreferences prefs, int currentVersionCode) {
        SharedPreferences.Editor editor = prefs.edit();
        Log.e(TAG, "Negative button clicked");
        editor.putBoolean("has-shown-prefs-" + currentVersionCode, true);
        editor.apply();
    }

    public static void uploadUserToken(Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String instanceToken = FirebaseInstanceId.getInstance().getToken();
        String lunch_group = readUserPerferedGroup(context);
        if (user == null || instanceToken == null) {
            Log.e(TAG, "Unable to upload user token user:" + user + " instanceId:" + instanceToken);
            return;
        }
        //TODO write the user token somewhere in the database where it can be found
        //database.getReference(user)
    }

    public static String getUserKey(Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String group = readUserPerferedGroup(context);
        if (group == null || user == null) {
            return null;
        }
        return group + "/users/" + user.getUid();
    }
}
