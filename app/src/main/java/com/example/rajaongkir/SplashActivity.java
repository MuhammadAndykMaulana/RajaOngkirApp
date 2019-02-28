package com.example.rajaongkir;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        this.checkSession();
    }

//    private void checkSession() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                RajaOngkirUser user="ok";
//                String user="OK";
//                    if (user !=null){
//                        openActivity(MainActivity.class);
//                    }
//                    else {
//                        user=null;
//                    }
////                    else {openActivity(loginAcitivity.class);}
//            }
//        })
//    }

//    private void openActivity(Class<MainActivity> mainActivityClass) {
//        startActivities(new Intent[]{SplashActivity.c});
//
//    }

}
