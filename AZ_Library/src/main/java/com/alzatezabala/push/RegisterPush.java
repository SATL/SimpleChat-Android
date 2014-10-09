package com.alzatezabala.push;

import java.io.IOException;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.alzatezabala.libreria.LibSharedPreferences;
import com.alzatezabala.libreria.Network;
import com.alzatezabala.libreria.NotifyPost;
import com.alzatezabala.libreria.Post;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class RegisterPush {
    //--DEfault
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "GCS";

    private Context ctx;
    private String url;
    public String SharedName;
    private Activity act;
    private HashMap<String, String> params;
    LibSharedPreferences sp;

    String SENDER_ID = "";
    GoogleCloudMessaging gcm;
    String regId;

    NotifyPost notifyPost = null;


    public RegisterPush(Context ctx, String name, String url, Activity act, HashMap<String, String> params, String senderId) {
        this.ctx = ctx;
        this.url = url;
        this.act = act;
        SharedName = name;
        this.params = params;
        this.SENDER_ID = senderId;
        sp = new LibSharedPreferences(ctx, name);
    }

    public RegisterPush(Context ctx, String name, String url, Activity act, String senderId) {
        this.ctx = ctx;
        this.url = url;
        this.SENDER_ID = senderId;
        this.act = act;
        SharedName = name;
        sp = new LibSharedPreferences(ctx, name);
        params = new HashMap<String, String>();
    }

    public void setNotifyPost(NotifyPost notifyPost) {
        this.notifyPost = notifyPost;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    public void register() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(ctx);
            regId = getRegistrationId(ctx);

            Log.i("TAG", regId);
            if (regId.isEmpty() || regId.equals("0")) {
                registerInBackground();
            } else {
                if (sp.getValue("idPush").isEmpty() || sp.getValue("idPush").equals("0")) {
                    Log.i("TAG", "spValue");
                    sendRegistrationIdToBackend(regId);
                }
            }
        } else {
            makeToast("No valid google play");
        }

    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(ctx);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, act,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                makeToast("Device not supported");
            }
            return false;
        } else {
            return true;
        }
    }

    // ---Google clases

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.length() == 0) {
            Log.i(TAG, "Registration not found");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences,
        // but
        // how you store the regID in your app is up to you.
        return ctx.getSharedPreferences(this.SharedName,
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(ctx);
                    }
                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;

                    // You should send the registration ID to your server over
                    // HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your
                    // app.
                    // The request to your server should be authenticated if
                    // your app
                    // is using accounts.
                    sendRegistrationIdToBackend(regId);

                    // For this demo: we don't need to send it because the
                    // device
                    // will send upstream messages to a server that echo back
                    // the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(ctx, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            protected void onPostExecute(String msg) {
                makeLog(msg);
                //makeToast(msg);
            }

        }.execute();
    }

    private void sendRegistrationIdToBackend(String key) {
        Post post = new Post(this.url);
        params.put("os", "and");
        params.put("key", key);
        post.setParams(params);

        Log.d("AzPush", key);


        if (Network.isNetworkAvailable(ctx)) {
            if (notifyPost != null) {
                new uploadId(ctx, this.SharedName, notifyPost).execute(post);
            } else {
                new uploadId(ctx, this.SharedName).execute(post);
            }
        } else {
            makeToast("No hay conexion a internet");
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public void makeToast(String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public void makeLog(String msg) {
        Log.d(TAG, msg);
    }

}


class uploadId extends AsyncTask<Post, Void, String> {
    private Context ctx;
    private String spName;
    private NotifyPost notifyPost = null;

    public uploadId(Context context, String spName) {
        this.ctx = context;
        this.spName = spName;
    }

    uploadId(Context ctx, String spName, NotifyPost notifyPost) {
        this.ctx = ctx;
        this.spName = spName;
        this.notifyPost = notifyPost;
    }

    @Override
    protected String doInBackground(Post... params) {
        // TODO Auto-generated method stub
        return params[0].request();
    }

    @Override
    protected void onPostExecute(String msg) {
        Log.i("Hash uploaded", msg);
        if (notifyPost != null) {
            notifyPost.postFinished(msg);
        }

        if (msg != "0") {
            LibSharedPreferences sp = new LibSharedPreferences(ctx, spName);
            sp.setValue("idPush", msg);
        }

    }

}