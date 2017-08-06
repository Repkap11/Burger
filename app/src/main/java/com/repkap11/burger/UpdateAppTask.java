package com.repkap11.burger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by paul on 9/20/15.
 */
public class UpdateAppTask extends AsyncTask<Void, Void, Integer> {
    private static final String TAG = UpdateAppTask.class.getSimpleName();
    private final boolean mNotifyUserOfNoUpdate;
    private Context mContext;

    public UpdateAppTask(Context applicationContext, boolean notifyUserOfNoUpdate) {
        mContext = applicationContext;
        mNotifyUserOfNoUpdate = notifyUserOfNoUpdate;
    }

    @Override
    protected Integer doInBackground(Void... arg0) {
        File outputFile = null;
        try {
            URL url = new URL(mContext.getResources().getString(R.string.update_url));
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            String username = "guest";
            String password = "guest";
            String userPassword = username + ":" + password;
            String encoding = Base64.encodeToString(userPassword.getBytes(), Base64.NO_WRAP);
            c.setRequestProperty("Authorization", "Basic " + encoding);
            c.setUseCaches(false);
            c.connect();
            outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Burger.apk");
            outputFile.getParentFile().mkdirs();
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            PackageInfo newInfo = mContext.getPackageManager().getPackageArchiveInfo(outputFile.getAbsolutePath(), PackageManager.GET_SIGNATURES);
            PackageInfo oldPackageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_SIGNATURES);

            boolean sameSignature = Arrays.equals(newInfo.signatures[0].toByteArray(), oldPackageInfo.signatures[0].toByteArray());
            if (!sameSignature) {
                return R.string.update_app_task_signatures_dont_match;
            }
            if (newInfo.versionCode > oldPackageInfo.versionCode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", outputFile);
                    //Uri apkUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", outputFile);
                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mContext.startActivity(intent);
                } else {
                    Uri apkUri = Uri.fromFile(outputFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            } else {
                outputFile.delete();
                Log.e(TAG, "Not installing because apk is older or the same");
                if (mNotifyUserOfNoUpdate) {
                    if (newInfo.versionCode == oldPackageInfo.versionCode) {
                        return R.string.update_app_task_no_update_avaliable;
                    } else {
                        return R.string.update_app_task_you_are_newer;
                    }

                } else {
                    return null;
                }

            }
        } catch (
                Exception e)

        {
            if (outputFile != null) {
                outputFile.delete();
            }
            e.printStackTrace();
            return R.string.update_app_task_error_updating_burger;

        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer message_id) {
        super.onPostExecute(message_id);

        if (message_id != null) {
            String message = mContext.getResources().getString(message_id);
            Log.e(TAG, message);
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }
}
