package com.example.rajaongkir;
//import

import android.app.Application;

import com.androidnetworking.AndroidNetworking;


public class OngkirApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        //Get Application Context hierarky: OS/App/Activities
    }
}
