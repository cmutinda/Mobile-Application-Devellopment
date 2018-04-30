package com.evoke.central.evokecentral;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Castrol on 10/20/2017.
 */

public class FireApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
