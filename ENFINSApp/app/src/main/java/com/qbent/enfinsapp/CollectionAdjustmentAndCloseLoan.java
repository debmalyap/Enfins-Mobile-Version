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
import android.support.design.widget.TextInputEditText;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class CollectionAdjustmentAndCloseLoan extends MainActivity implements ApiCallback
{
    private DatePickerDialog.OnDateSetListener fromDateSetListener,toDateSetListener;

    private String user_branch,emptyGuid = "00000000-0000-0000-0000-000000000000",date1,date2;

    private Date fromDate,toDate;

    Spinner adjusmentBranchSpinner;

    private String adjusmentBranchId = " ";

    private List<Branch> adjusmentBranchLists;

    HashMap<String, String> spinnerAdjusmentBranchMap = new HashMap<String, String>();

    EditText fromDateField,toDateField;

    Button generateButton,summaryButton,cancelButton;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_collection_adjusment_and_close_loan_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_adjusment);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_collection_adjustment_and_close_loan, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_adjusment);
        }

        final AlertDialogue alertDialogue = new AlertDialogue(CollectionAdjustmentAndCloseLoan.this);

        adjusmentBranchSpinner = (Spinner) findViewById(R.id.adjBranchId);

        adjusmentBranchLists = new ArrayList<Branch>();

        fromDateField = (EditText) findViewById(R.id.adjFromId);
        toDateField = (EditText) findViewById(R.id.adjToId);

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

        generateButton = (Button) findViewById(R.id.reportId);
        summaryButton = (Button) findViewById(R.id.summaryId);
        cancelButton = (Button) findViewById(R.id.cancelId);

        Drawable image1 = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        generateButton.setBackground(rippledBg1);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        summaryButton.setBackground(rippledBg);

        Drawable image2 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg2 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image2, null);
        cancelButton.setBackground(rippledBg2);

        //---Branch Name Spinner---//
        adjusmentBranchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_branch!=null && !user_branch.equals("null")&& !user_branch.equals(emptyGuid))
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerAdjusmentBranchMap.keySet());
                    int test = indexes1.indexOf(user_branch);
                    String test2 = (new ArrayList<String>(spinnerAdjusmentBranchMap.values())).get(test);
                    adjusmentBranchSpinner.setSelection(((ArrayAdapter<String>)adjusmentBranchSpinner.getAdapter()).getPosition(test2));
                    adjusmentBranchId = user_branch;
                }
                else
                {
                    String name = adjusmentBranchSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerAdjusmentBranchMap.values());
                    adjusmentBranchId = (new ArrayList<String>(spinnerAdjusmentBranchMap.keySet())).get(indexes.indexOf(name));
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
                        CollectionAdjustmentAndCloseLoan.this,
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
                        CollectionAdjustmentAndCloseLoan.this,
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
        //---End of from date field activity---//

        //---Generate report activity---//
        generateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(adjusmentBranchSpinner.getSelectedItemPosition() == 0)
                {
                    alertDialogue.showAlertMessage("Please select a branch");
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

                else if(adjusmentBranchSpinner.getSelectedItemPosition() != 0 && fromDateField.getText().toString().length() != 0
                        && toDateField.getText().toString().length() != 0)
                {

                    ApiRequest apiRequest = new ApiRequest("generateAdjustmentReport");
                    try{
                        JSONObject jsonObject = new JSONObject();
                        //---Post JSON Data here---//
                        jsonObject.accumulate("branchId",adjusmentBranchId);
                        jsonObject.accumulate("fromDate",fromDateField.getText().toString());
                        jsonObject.accumulate("toDate",toDateField.getText().toString());

                        apiRequest.set_t(jsonObject);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    new ApiHandler.PostAsync(CollectionAdjustmentAndCloseLoan.this).execute(apiRequest);
                    return;
                }

            }
        });


        summaryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(adjusmentBranchSpinner.getSelectedItemPosition() == 0)
                {
                    alertDialogue.showAlertMessage("Please select a branch");
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

                else if(adjusmentBranchSpinner.getSelectedItemPosition() != 0 && fromDateField.getText().toString().length() != 0
                        && toDateField.getText().toString().length() != 0)
                {
                    ApiRequest apiRequest = new ApiRequest("generateCloseLoanReport");
                    try{
                        JSONObject jsonObject = new JSONObject();
                        //---Post JSON Data here---//
                        jsonObject.accumulate("branchId",adjusmentBranchId);
                        jsonObject.accumulate("fromDate",fromDateField.getText().toString());
                        jsonObject.accumulate("toDate",toDateField.getText().toString());

                        apiRequest.set_t(jsonObject);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    new ApiHandler.PostAsync(CollectionAdjustmentAndCloseLoan.this).execute(apiRequest);
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

    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException,RuntimeException
    {
        if(key.contains("getUserWiseBranch"))
        {
            setAdjusmentBranchAdapter(result);
        }
        else if(key.contains("generateAdjustmentReport"))
        {
            setGenerateReportAdapter(result);
        }

        else if(key.contains("generateCloseLoanReport"))
        {
            setGenerateCloseLoanReportAdapter(result);
        }
    }

    private void setAdjusmentBranchAdapter(String result)
    {
        try {

            adjusmentBranchLists = new ArrayList<Branch>();
            JSONArray jsonArray = new JSONArray(result);
            Branch branch1 = new Branch(emptyGuid,"Select branch");
            adjusmentBranchLists.add(branch1);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                Branch branch = new Branch(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                adjusmentBranchLists.add(branch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerAdjusmentBranchArray = new String[adjusmentBranchLists.size()];
        for (int i = 0; i < adjusmentBranchLists.size(); i++)
        {
            spinnerAdjusmentBranchMap.put(adjusmentBranchLists.get(i).getId(),adjusmentBranchLists.get(i).getName());
            spinnerAdjusmentBranchArray[i] = adjusmentBranchLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionAdjustmentAndCloseLoan.this, android.R.layout.simple_spinner_item,spinnerAdjusmentBranchArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        adjusmentBranchSpinner.setAdapter(adapter);
    }

    private void populateTrialBranchNames()
    {
        new ApiHandler.GetAsync(CollectionAdjustmentAndCloseLoan.this).execute("getUserWiseBranch");
    }

    private void setGenerateReportAdapter(String result)
    {
        String test = "http://"+result.substring(1, result.length()-1);
        test = test.replace("\\\\","/");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(test));
        request.setDescription("Adjustment Collection");
        request.setTitle("Adjusment Report");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "AdjusmentReport"+" "+".xlsx");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        final AlertDialog.Builder builder = new AlertDialog.Builder(CollectionAdjustmentAndCloseLoan.this);
        builder.setTitle("ENFIN Admin");
        builder.setMessage("Data Download Successfully.");
        builder.setCancelable(true);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                adjusmentBranchSpinner.setSelection(0);
                fromDateField.setText(null);
                toDateField.setText(null);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void setGenerateCloseLoanReportAdapter(String result1)
    {
        String test2 = "http://"+result1.substring(1, result1.length()-1);
        test2 = test2.replace("\\\\","/");
        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(test2));
        request1.setDescription("Close Loan Collection");
        request1.setTitle("Close Loan Report");
        request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "CloseLoanReport"+" "+".xlsx");
        request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager manager1 = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        manager1.enqueue(request1);

        final AlertDialog.Builder builder = new AlertDialog.Builder(CollectionAdjustmentAndCloseLoan.this);
        builder.setTitle("ENFIN Admin");
        builder.setMessage("Data Download Successfully.");
        builder.setCancelable(true);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                adjusmentBranchSpinner.setSelection(0);
                fromDateField.setText(null);
                toDateField.setText(null);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
