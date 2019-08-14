package com.qbent.enfinsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class OverdueReportActvity extends MainActivity implements ApiCallback
{
    private DatePickerDialog.OnDateSetListener fromDateSetListener;
    private DatePickerDialog.OnDateSetListener toDateSetListener;

    EditText fromDateField,toDateField;

    private String date1,date2;

    private Date fromDate,toDate;

    Button generateOverdueLoan,cancelOverdueLoan;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_overdue_report_actvity);


        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_overdue_report_activity_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_overduereport);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_overdue_report_actvity, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_overduereport);
        }

        final AlertDialogue alertDialogue = new AlertDialogue(OverdueReportActvity.this);

        fromDateField = (EditText) findViewById(R.id.fromDate);
        toDateField = (EditText) findViewById(R.id.toDate);

        fromDateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(false);
                return false;
            }
        });

        toDateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(false);
                return false;
            }
        });

        generateOverdueLoan = (Button) findViewById(R.id.generateButtonOverdue);
        cancelOverdueLoan = (Button) findViewById(R.id.cancelButtonOverdue);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        generateOverdueLoan.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        cancelOverdueLoan.setBackground(rippledBg1);

        //---From-date picker---//
        fromDateField.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        OverdueReportActvity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        fromDateSetListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }


        });
        fromDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @SuppressLint("LongLogTag")
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year)
            {
                month += 1;
                date1 = month + "-" +year+ "-" +day;
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd-yyyy");
                try {
                    fromDate = simpleDateFormat1.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fromDateField.setText(simpleDateFormat1.format(fromDate));


            }
        };
        //---End of From-date picker---//

        //---To-date picker---//
        toDateField.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        OverdueReportActvity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        toDateSetListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        toDateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @SuppressLint("LongLogTag")
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year)
            {
                month += 1;
                date2 = month + "-" +year+ "-" +day;
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM-dd-yyyy");
                try {
                    toDate = simpleDateFormat2.parse(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                toDateField.setText(simpleDateFormat2.format(toDate));
            }
        };
        //---End of to-date picker---//

        generateOverdueLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(fromDateField.getText().toString().length() == 0)
                {
                    alertDialogue.showAlertMessage("Please select a start date");
                    return;
                }
                else if(toDateField.getText().toString().length() == 0)
                {
                    alertDialogue.showAlertMessage("Please select an end date");
                    return;
                }
                else if(fromDate.compareTo(toDate) > 0)
                {
                    alertDialogue.showAlertMessage("Starting date can't be larger than end date,please select correct date");
                    return;
                }
                //---Post call JSON---//
                else if(fromDateField.getText().toString().length() != 0 && toDateField.getText().toString().length() != 0)
                {

                    ApiRequest apiRequest = new ApiRequest("generateOverDueLoanReport");
                    try{
                        JSONObject jsonObject = new JSONObject();
                        //---Post JSON Data here---//
                        jsonObject.accumulate("fromDate",fromDateField.getText().toString());
                        jsonObject.accumulate("toDate",toDateField.getText().toString());

                        apiRequest.set_t(jsonObject);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    new ApiHandler.PostAsync(OverdueReportActvity.this).execute(apiRequest);
                    return;
                }
            }
        });

        cancelOverdueLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });

        fab.setVisibility(View.GONE);
    }

    //---End of onCreate() method---//
    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if(key.contains("generateOverDueLoanReport"))
        {
            setGenerateOverdueLoanAdapter(result);
        }
    }




    private void setGenerateOverdueLoanAdapter(String result)
    {
        //---Download excel sheet---//
        String test = "http://"+result.substring(1, result.length()-1);
        test = test.replace("\\\\","/");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(test));
        request.setDescription("Overdue Loan");
        request.setTitle("Overdue Loan Report");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "OverdueLoanReport"+" "+".xlsx");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        final AlertDialog.Builder builder = new AlertDialog.Builder(OverdueReportActvity.this);
        builder.setTitle("ENFIN Admin");
        builder.setMessage("Data Download Successfully.");
        builder.setCancelable(true);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                fromDateField.setText(null);
                toDateField.setText(null);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
