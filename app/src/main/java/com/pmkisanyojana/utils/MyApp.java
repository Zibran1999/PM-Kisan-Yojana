package com.pmkisanyojana.utils;

import android.app.Application;
import android.content.Intent;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.pmkisanyojana.activities.MainActivity;

public class MyApp extends Application {

    private static final String ONESIGNAL_APP_ID = "9a77bdda-945f-4f86-bf6a-5c564559c350";
    private static MyApp mInstance;

    public MyApp() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setNotificationOpenedHandler(new ExampleNotificationOpenedHandler());
        OneSignal.setAppId(ONESIGNAL_APP_ID);

    }

    private class ExampleNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {

        @Override
        public void notificationOpened(OSNotificationOpenedResult result) {
            Intent intent = new Intent(MyApp.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

}
