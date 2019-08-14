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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Branch;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class TrialBalanceReport extends MainActivity implements ApiCallback
{
    private DatePickerDialog.OnDateSetListener fromDateSetListener,toDateSetListener;

    private String user_branch,emptyGuid = "00000000-0000-0000-0000-000000000000",date1,date2;

    private Date fromDate,toDate;

    Spinner trialBranchSpinner;
    Spinner typeSpinner;

    private List<Branch> trialBranchLists;

    HashMap<String, String> spinnerTrialBranchMap = new HashMap<String, String>();

    EditText fromDateField,toDateField;

    Button generateButton,cancelButton;

    private String typeId = " ",trialBranchId = " ";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_trial_balance_report_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_trialbalance);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_trial_balance_report, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_trialbalance);
        }

        final AlertDialogue alertDialogue = new AlertDialogue(TrialBalanceReport.this);

        trialBranchSpinner = (Spinner) findViewById(R.id.trialBranchId);
        typeSpinner = (Spinner) findViewById(R.id.selectTypeId);

        trialBranchLists = new ArrayList<Branch>();

        fromDateField = (EditText) findViewById(R.id.trialFromId);
        fromDateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(false);
                return false;
            }
        });

        toDateField = (EditText) findViewById(R.id.trialToId);
        toDateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(false);
                return false;
            }
        });

        generateButton = (Button) findViewById(R.id.generateId);
        cancelButton = (Button) findViewById(R.id.cancelId);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        generateButton.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        cancelButton.setBackground(rippledBg1);


        //---Branch Name Spinner---//
        trialBranchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_branch!=null && !user_branch.equals("null")&& !user_branch.equals(emptyGuid))
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerTrialBranchMap.keySet());
                    int test = indexes1.indexOf(user_branch);
                    String test2 = (new ArrayList<String>(spinnerTrialBranchMap.values())).get(test);
                    trialBranchSpinner.setSelection(((ArrayAdapter<String>)trialBranchSpinner.getAdapter()).getPosition(test2));
                    trialBranchId = user_branch;
                }
                else
                {
                    String name = trialBranchSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerTrialBranchMap.values());
                    trialBranchId = (new ArrayList<String>(spinnerTrialBranchMap.keySet())).get(indexes.indexOf(name));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//
            }
        });
        //---End of Branch Name Spinner---//
        populateTrialBranchNames();

        //---Trial types spinner---//
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        typeSpinner.setAdapter(arrayAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                typeId = String.valueOf(typeSpinner.getSelectedItem().toString().charAt(0));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//
            }
        });

        //---End of trail types spinner---//


        //---From date field activity---//
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
                        TrialBalanceReport.this,
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
        //---End of from date field activity---//

        //---To date field activity---//
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
                        TrialBalanceReport.this,
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
        //---End of to date field activity---//


        //---Generate report activity---//
        generateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(trialBranchSpinner.getSelectedItemPosition() == 0)
                {
                    alertDialogue.showAlertMessage("Please select a branch");
                    return;
                }
                else if(typeSpinner.getSelectedItemPosition() == 0)
                {
                    alertDialogue.showAlertMessage("Please select a type");
                    return;
                }
                else if(fromDateField.getText().toString().length() == 0)
                {
                    alertDialogue.showAlertMessage("Please select starting date");
                    return;
                }
                else if(toDateField.getText().toString().length() == 0)
                {
                    alertDialogue.showAlertMessage("Please select ending date");
                    return;
                }
                else if(fromDate.compareTo(toDate) > 0)
                {
                    alertDialogue.showAlertMessage("Starting date can't be larger than end date,please select correct date");
                    return;
                }
                else if(trialBranchSpinner.getSelectedItemPosition() !=0 && typeSpinner.getSelectedItemPosition() !=0
                        && fromDateField.getText().toString().length() !=0 && toDateField.getText().toString().length() != 0)
                {

                    ApiRequest apiRequest = new ApiRequest("getTrialBalanceReport");
                    try{

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.accumulate("branchId",trialBranchId);
                        jsonObject.accumulate("trialBalanceType",typeId);
                        jsonObject.accumulate("fromDate",fromDateField.getText().toString());
                        jsonObject.accumulate("toDate",toDateField.getText().toString());

                        apiRequest.set_t(jsonObject);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    new ApiHandler.PostAsync(TrialBalanceReport.this).execute(apiRequest);
                    return;
                }


            }
        });
        //---End of generate report activity---//

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
        fab.setVisibility(View.GONE);
    }

    private void showAlert(String error)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(TrialBalanceReport.this);
        builder.setTitle("ENFIN Admin");
        builder.setMessage(error);
        builder.setCancelable(true);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException,RuntimeException
    {
        if(key.contains("getUserWiseBranch"))
        {
            setTrialBranchAdapter(result);
        }
        else if(key.contains("getTrialBalanceReport"))
        {
            setGenerateReportAdapter(result);
        }
    }

    private void setTrialBranchAdapter(String result)
    {
        try {

            trialBranchLists = new ArrayList<Branch>();
            JSONArray jsonArray = new JSONArray(result);
            Branch branch1 = new Branch(emptyGuid,"Select branch");
            trialBranchLists.add(branch1);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                Branch branch = new Branch(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                trialBranchLists.add(branch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerTrialBranchArray = new String[trialBranchLists.size()];
        for (int i = 0; i < trialBranchLists.size(); i++)
        {
            spinnerTrialBranchMap.put(trialBranchLists.get(i).getId(),trialBranchLists.get(i).getName());
            spinnerTrialBranchArray[i] = trialBranchLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TrialBalanceReport.this, android.R.layout.simple_spinner_item,spinnerTrialBranchArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        trialBranchSpinner.setAdapter(adapter);
    }

    private void populateTrialBranchNames()
    {
        new ApiHandler.GetAsync(TrialBalanceReport.this).execute("getUserWiseBranch");
    }

    private void setGenerateReportAdapter(String result)
    {

        String test2 = "http://"+result.substring(1, result.length()-1);
        test2 = test2.replace("\\\\","/");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(test2));
        request.setDescription("Trial Balance");
        request.setTitle("Trial Balnace Report");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "TrialBalnaceReport"+" "+".xlsx");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        final AlertDialog.Builder builder = new AlertDialog.Builder(TrialBalanceReport.this);
        builder.setTitle("ENFIN Admin");
        builder.setMessage("Data Download Successfully.");
        builder.setCancelable(true);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                trialBranchSpinner.setSelection(0);
                typeSpinner.setSelection(0);
                fromDateField.setText(null);
                toDateField.setText(null);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
