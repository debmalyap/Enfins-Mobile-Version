package com.qbent.enfinsapp;
//---Edited by Debmalya---//
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.global.AuthHelper;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassword extends MainActivity implements ApiCallback
{
    EditText currentPassField;
    EditText newPassField;
    EditText confirmPassField;

    TextInputLayout newPassLayout,confirmPassLayout;

    Button passwordSaveButton,passwordCancelButton;

    private AuthHelper _authHelper;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_change_password_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_changepassword);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_change_password, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_changepassword);
        }

        final AlertDialogue alertDialogue = new AlertDialogue(ChangePassword.this);

        currentPassField = (EditText) findViewById(R.id.currentPasswordId);
        newPassField = (EditText) findViewById(R.id.newPasswordId);
        confirmPassField = (EditText) findViewById(R.id.confirmPasswordId);

        newPassLayout = (TextInputLayout) findViewById(R.id.newPasswordLayoutId);
        confirmPassLayout = (TextInputLayout) findViewById(R.id.confirmPasswordLayoutId);

        passwordSaveButton = (Button) findViewById(R.id.passSaveButton);
        passwordCancelButton = (Button) findViewById(R.id.passCancelButton);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        passwordSaveButton.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        passwordCancelButton.setBackground(rippledBg1);


        currentPassField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        newPassField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        confirmPassField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        newPassField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                newPassLayout.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                activityNewPassword();
            }
        });

        confirmPassField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                confirmPassLayout.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                activityConfirmPassword();
            }
        });


        passwordSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(currentPassField.getText().toString().length() == 0)
                {
                    alertDialogue.showAlertMessage("Please enter the current password");
                    return;
                }
                if(newPassField.getText().toString().length() == 0)
                {
                    alertDialogue.showAlertMessage("Please enter a new valid password");
                    return;
                }
                if(confirmPassField.getText().toString().length() == 0)
                {
                    alertDialogue.showAlertMessage("Please confirm the password");
                    return;
                }
                if(!newPassField.getText().toString().isEmpty() && !confirmPassField.getText().toString().isEmpty())
                {
                    if(!confirmPassField.getText().toString().equals(newPassField.getText().toString()))
                    {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                        builder.setTitle("ENFINS Admin");
                        builder.setMessage("Password Not Match");
                        builder.setCancelable(true);
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                newPassField.setText(null);
                                confirmPassField.setText(null);
                                passwordSaveButton.setEnabled(true);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return;
                    }
                }

                ApiRequest apiRequest = new ApiRequest("changePassword");
                try
                {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.accumulate("Password",(!currentPassField.getText().toString().isEmpty())?currentPassField.getText().toString():null);
                    jsonObject.accumulate("NewPassword", (!newPassField.getText().toString().isEmpty())?newPassField.getText().toString():null);


                    apiRequest.set_t(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new ApiHandler.PostAsync(ChangePassword.this).execute(apiRequest);
            }
        });

        passwordCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });

        fab.setVisibility(View.GONE);



    }



    private void activityNewPassword()
    {
        if (!newPassField.getText().toString().matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,20}"))
        {
            newPassField.setError("Password policy not matched");
            newPassField.setFocusableInTouchMode(false);
            currentPassField.setFocusableInTouchMode(false);
            newPassLayout.setPasswordVisibilityToggleEnabled(false);
            passwordSaveButton.setEnabled(false);
            return;
        }
        else
        {
            passwordSaveButton.setEnabled(true);
            return;
        }

    }

    private void activityConfirmPassword()
    {
        if (!confirmPassField.getText().toString().matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,20}"))
        {
            confirmPassField.setError("Password policy not matched");
            confirmPassField.setFocusableInTouchMode(false);
            confirmPassLayout.setPasswordVisibilityToggleEnabled(false);
            passwordSaveButton.setEnabled(false);
            return;
        }
        else
        {
            passwordSaveButton.setEnabled(true);
            return;
        }
    }

    @Override
    public void onApiRequestStart() throws IOException {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if(key.contains("changePassword"))
        {
            setNewPasswordAdapter(result);
        }
    }

    private void setNewPasswordAdapter(String result)
    {
        if(result.equals("false"))
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(ChangePassword.this);
            builder1.setTitle("ENFINs Admin");
            builder1.setMessage("Your Current Password Does not Match");
            builder1.setCancelable(true);
            builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface1, int i)
                {
                    currentPassField.setText(null);
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
            return;
        }
        else
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(ChangePassword.this);
            builder1.setTitle("ENFINs Admin");
            builder1.setMessage("Password Successfully Saved");
            builder1.setCancelable(true);
            builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface1, int i)
                {
                    finish();
                    startActivity(getIntent());
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
            return;
        }
    }
    //---Ended by Debmalya---//
}
