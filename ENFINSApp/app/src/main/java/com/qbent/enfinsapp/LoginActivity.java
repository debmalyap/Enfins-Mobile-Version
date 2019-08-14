package com.qbent.enfinsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.global.AuthHelper;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Login;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity
        implements ApiCallback {

    private AuthHelper _authHelper;
    String userName,imei;
    TelephonyManager mTelephonyManager;
    int PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    final AlertDialogue alertDialogue = new AlertDialogue(LoginActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _authHelper = AuthHelper.getInstance(this);

        final EditText usernameEditText = findViewById(R.id.userName);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.blue));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                getDeviceImei();
            }
        }
        else
        {
            getDeviceImei();
        }

//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(LoginActivity.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        imei =  telephonyManager.getDeviceId();

        Drawable image = getDrawable(R.drawable.login_btn_bg);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        loginButton.setBackground(rippledBg);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usernameEditText.getText().toString().length()==0)
                {
                    usernameEditText.requestFocus();
                    usernameEditText.setError("USER NAME CANNOT BE EMPTY");
                    return;
                }
                if(passwordEditText.getText().toString().length()==0)
                {
                    passwordEditText.requestFocus();
                    passwordEditText.setError("PASSWORD CANNOT BE EMPTY");
                    return;
                }
                ApiRequest apiRequest = new ApiRequest("authenticate-lo");
                try{
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("userName", usernameEditText.getText().toString());
                    userName = usernameEditText.getText().toString();
                    jsonObject.accumulate("password", passwordEditText.getText().toString());
                    jsonObject.accumulate("imei", imei);
                    apiRequest.set_t(jsonObject);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


                //apiRequest.set_t(new Login(usernameEditText.getText().toString(), passwordEditText.getText().toString()));
                new ApiHandler.PostAsync(LoginActivity.this).execute(apiRequest);
            }
        });
    }

    private void getDeviceImei()
    {
        mTelephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        imei = mTelephonyManager.getDeviceId();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceImei();
        }
        else
        {
            finish();
            System.exit(0);
        }
    }



    @Override
    public void onApiRequestStart() throws IOException {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException {
        try {
            if (key.equals("authenticate-lo")) {
                System.out.println(result);
                JSONObject jsonObject = new JSONObject(result);
                if (!jsonObject.getString("name").isEmpty())
                {
                    if(!jsonObject.getString("version").equals("v2"))
                    {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("ENFIN Admin");
                        builder.setMessage("Kindly update DCCL App?");
                        builder.setCancelable(true);
                        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                new ApiHandler.UpdateApp(LoginActivity.this).execute("http://192.168.0.65:80/resources/apk/app-debug.apk");

                            }
                        });
                        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                finish();
                                System.exit(0);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return;
                    }
                    _authHelper.setIdToken(jsonObject.getString("token"));
                    _authHelper.setIdDate(jsonObject.getString("workingDate"));
                    _authHelper.setIdName(userName);
                    Intent intent = new Intent(getApplicationContext(), WorkingDateActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    alertDialogue.showAlertMessage(jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
