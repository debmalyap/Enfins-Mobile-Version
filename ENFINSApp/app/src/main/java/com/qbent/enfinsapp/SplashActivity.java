package com.qbent.enfinsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qbent.enfinsapp.global.AuthHelper;

public class SplashActivity extends AppCompatActivity {

//    private AuthHelper _authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
//        _authHelper = AuthHelper.getInstance(this);
//        if(_authHelper.isLoggedIn())
//        {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        else
//        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
//        }

    }
}
