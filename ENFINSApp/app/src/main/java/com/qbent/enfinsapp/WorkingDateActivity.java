package com.qbent.enfinsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.global.AuthHelper;

import java.security.PrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class WorkingDateActivity extends AppCompatActivity
{

    Button saveSelectDate;
    EditText demandFromDateId;
    private AuthHelper _authHelper;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Date workingDate,selectionDate;
    private Date date;
    private String workingStringDate;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            setContentView(R.layout.activity_working_date_normal);
        }
        else
        {
            setContentView(R.layout.activity_working_date);
        }
        saveSelectDate = (Button) findViewById(R.id.saveSelectDate);
        demandFromDateId = (EditText) findViewById(R.id.demandFromDateId);

        //TODO Edited by Debmalya//
        Drawable image = getDrawable(R.drawable.login_btn_bg);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        saveSelectDate.setBackground(rippledBg);
        //TODO end//

        Configuration config = getResources().getConfiguration();
       System.out.println(config.smallestScreenWidthDp);


        final AlertDialogue alertDialogue = new AlertDialogue(WorkingDateActivity.this);

        _authHelper = AuthHelper.getInstance(this);


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.parse(_authHelper.getIdDate());
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd-yyyy");
            workingStringDate = simpleDateFormat1.format(date);
            try {
                workingDate = simpleDateFormat1.parse(simpleDateFormat1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            selectionDate = workingDate;
            demandFromDateId.setText((String)simpleDateFormat1.format(date));
            demandFromDateId.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            WorkingDateActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            onDateSetListener,
                            year,month,day);
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
                }
            });
            onDateSetListener = new DatePickerDialog.OnDateSetListener()
            {
                @SuppressLint("LongLogTag")
                @Override
                public void onDateSet(DatePicker datePicker, int day, int month, int year)
                {
                    month += 1;
                    String date = month + "-" +year+ "-" +day;
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd-yyyy");
                    try {
                        selectionDate = simpleDateFormat1.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    demandFromDateId.setText(date);
                }
            };
        } catch (ParseException e) {
            e.printStackTrace();
        }




        saveSelectDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(demandFromDateId.getText().toString().length()==0)
                {
                    demandFromDateId.requestFocus();
                    demandFromDateId.setError("Kindly Select Working Date");
                    return;
                }
                if(selectionDate.compareTo(workingDate) > 0)
                {
                    alertDialogue.showAlertMessage("Working Date can not be larger than Branch Working Date.Branch Working Date is "+workingStringDate);
                    return;
                }
                _authHelper.setIdSelectionDate(demandFromDateId.getText().toString());
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }



}
