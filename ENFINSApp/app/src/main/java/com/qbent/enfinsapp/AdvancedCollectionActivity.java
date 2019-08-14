package com.qbent.enfinsapp;

//---Developed by Debmalya---//
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qbent.enfinsapp.adapter.AutoCompleteAdapter;
import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.global.AuthHelper;
import com.qbent.enfinsapp.model.AdvancedCollection;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Branch;
import com.qbent.enfinsapp.model.CollPoint;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
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

public class AdvancedCollectionActivity extends MainActivity implements ApiCallback
{
    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    String user_branch,user_colcpoint;

    Boolean editBranchChanged = false;
    Boolean editCollectionChanged = false;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    Spinner branchSpinner;
    Spinner advancedCollectionPointSpinner;
    Spinner instalmentSpinner;

    private List<CollPoint> advancedCollectionPointLists;
    private List<Branch> userWiseBranchLists;

    Button cancelAdvancedButton,saveAdvancedButton;

    EditText advancedDateField;
    EditText advancedLoanBondNoField;
    EditText installmentAmountField;
    EditText interestField;
    EditText collectedAmountField;

    HashMap<String, String> spinnerAdvancedCollectionPointNamesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerUserWiseBranchMap = new HashMap<String, String>();

    private String branchId = " ";
    private String collectionPointId = " ";
    private String loanId = " ";
    private int instalAmount;
    private int outstandingAmount;

    private Handler advaHandler;
    private AutoCompleteAdapter advAdapter;

    private AppCompatAutoCompleteTextView autoCompleteAdvancedLoanBondNo;

    private List<AdvancedCollection> advancedCollectionList;
    private List<String> advancedSearchText;

    private AuthHelper _authHelper;
    private Date workingDate,selectionDate;
    private Date date;
    private String workingStringDate;

    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_advanced_collection);


        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //inflate your activity layout here!
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_advanced_collection_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_advancecollection);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //inflate your activity layout here!
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_advanced_collection, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_advancecollection);
        }

        final AlertDialogue alertDialogue = new AlertDialogue(AdvancedCollectionActivity.this);

        branchSpinner = (Spinner) findViewById(R.id.advancedBranchNameId);
        advancedCollectionPointSpinner = (Spinner) findViewById(R.id.advancedColcPointNameId);
        instalmentSpinner = (Spinner) findViewById(R.id.advancedInstalmentId);

        advancedCollectionPointLists = new ArrayList<CollPoint>();
        userWiseBranchLists = new ArrayList<Branch>();

        advancedDateField = (EditText) findViewById(R.id.advancedPaymentDateId);

        advancedLoanBondNoField = (EditText) findViewById(R.id.advancedLoanBondNoId);
        advancedLoanBondNoField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        installmentAmountField = (EditText) findViewById(R.id.advancedInstallmentAmountId);

        collectedAmountField = (EditText) findViewById(R.id.collectedAmountId);

        collectedAmountField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        installmentAmountField.setEnabled(false);

        saveAdvancedButton = (Button) findViewById(R.id.saveAdvancedButton);
        cancelAdvancedButton = (Button) findViewById(R.id.cancelAdvancedButton);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        saveAdvancedButton.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        cancelAdvancedButton.setBackground(rippledBg1);

        //---Instalment period spinner---//
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.instalment, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        instalmentSpinner.setAdapter(arrayAdapter);

        instalmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(!installmentAmountField.getText().toString().isEmpty())
                {
                    arithmeticOperation();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //---End of instalment spinner---//

        //---Date field---//
        _authHelper = AuthHelper.getInstance(this);
        advancedDateField.setEnabled(false);

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
            advancedDateField.setText((String)simpleDateFormat1.format(date));
            advancedDateField.setOnClickListener(new View.OnClickListener()
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
                            AdvancedCollectionActivity.this,
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
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        selectionDate = simpleDateFormat1.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    advancedDateField.setText(date);
                }
            };
        } catch (ParseException e) {
            e.printStackTrace();
        }

        collectedAmountField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                collectedAmountWatcher(alertDialogue);
            }
        });
        //---End of Outstanding amount validation---//



        //---Branch Name Spinner---//
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_branch!=null && !user_branch.equals("null")&& !user_branch.equals(emptyGuid) && editBranchChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerUserWiseBranchMap.keySet());
                    int test = indexes1.indexOf(user_branch);
                    String test2 = (new ArrayList<String>(spinnerUserWiseBranchMap.values())).get(test);
                    branchSpinner.setSelection(((ArrayAdapter<String>)branchSpinner.getAdapter()).getPosition(test2));
                    collectionPointId = user_branch;
                }
                else
                {
                    String name = branchSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerUserWiseBranchMap.values());
                    int a = indexes.indexOf(name);
                    collectionPointId = (new ArrayList<String>(spinnerUserWiseBranchMap.keySet())).get(indexes.indexOf(name));
                }
                populateAdvancedCollectionPointNames(collectionPointId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//
            }
        });
        branchSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editBranchChanged = true;
                }
                return false;
            }
        });
        //---End of Branch Name Spinner---//

        //---Collection Point Spinner---//
        advancedCollectionPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_colcpoint!=null && !user_colcpoint.equals("null")&& !user_colcpoint.equals(emptyGuid) && !collectionPointId.equals(emptyGuid) && editCollectionChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerAdvancedCollectionPointNamesMap.keySet());
                    int test = indexes1.indexOf(user_colcpoint);
                    String test2 = (new ArrayList<String>(spinnerAdvancedCollectionPointNamesMap.values())).get(test);
                    advancedCollectionPointSpinner.setSelection(((ArrayAdapter<String>)advancedCollectionPointSpinner.getAdapter()).getPosition(test2));
                    collectionPointId = user_colcpoint;
                }
                else
                {
                    String name = advancedCollectionPointSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerAdvancedCollectionPointNamesMap.values());
                    int a = indexes.indexOf(name);
                    collectionPointId = (new ArrayList<String>(spinnerAdvancedCollectionPointNamesMap.keySet())).get(indexes.indexOf(name));
                }

                if(!collectionPointId.equals(loanId))
                {
                    advancedLoanBondNoField.setText(null);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//
            }
        });
        advancedCollectionPointSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editCollectionChanged = true;
                }
                return false;
            }
        });

        //---End of Collection Point Spinner---//


        cancelAdvancedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });



        populateUserWiseBranchNames();

        //---AutoComplete of Advanced Collection Loan Bond code---//
        autoCompleteAdvancedLoanBondNo = (AppCompatAutoCompleteTextView) findViewById (R.id.advancedLoanBondNoId);
        advAdapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteAdvancedLoanBondNo.setThreshold(3);
        autoCompleteAdvancedLoanBondNo.setAdapter (advAdapter);
        autoCompleteAdvancedLoanBondNo.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id)
                    {
                        advancedLoanBondNoField.setText(advAdapter.getObject(position));
                        AdvancedCollection advancedCollection = null;

                        for (int i = 0; i < advancedCollectionList.size(); i++)
                        {
                            if(advancedCollectionList.get(i).getLoanBondNo() == advAdapter.getObject(position))
                            {
                                advancedCollection = advancedCollectionList.get(i);
                            }
                        }
                        loanId = advancedCollection.getLoanId();
                        instalAmount = advancedCollection.getInstallmentAmount();
                        outstandingAmount = advancedCollection.getOutstandingAmount();
                        installmentAmountField.setText(String.valueOf(instalAmount));

                    }
                });

        autoCompleteAdvancedLoanBondNo.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
                advaHandler.removeMessages(TRIGGER_AUTO_COMPLETE);
                advaHandler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                editFields(alertDialogue);
            }
        });
        advaHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteAdvancedLoanBondNo.getText()))
                    {
                        ApiRequest apiRequest = new ApiRequest("getLoanBondNoByCP");
                        try
                        {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.accumulate("collectionPointId", collectionPointId);
                            jsonObject.accumulate("searchText", (!advancedLoanBondNoField.getText().toString().isEmpty())?advancedLoanBondNoField.getText().toString():null);

                            apiRequest.set_t(jsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(AdvancedCollectionActivity.this).execute(apiRequest);

                    }
                }
                return false;
            }
        });
        //---End of AutoComplete of Advanced Collection Loan Bond code---//

//        advancedLoanBondNoField.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable)
//            {
//                loanBondNumberWatcher(alertDialogue);
//            }
//        });


        saveAdvancedButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(advancedDateField.getText().toString().length() == 0)
                {
                    advancedDateField.requestFocus();
                    alertDialogue.showAlertMessage("Payment date can't be empty");
                    return;
                }

                if(branchSpinner.getSelectedItemPosition() == 0)
                {
                    branchSpinner.requestFocus();
                    alertDialogue.showAlertMessage("Please select a branch");
                    return;
                }

                if(advancedCollectionPointSpinner.getSelectedItemPosition() == 0)
                {
                    advancedCollectionPointSpinner.requestFocus();
                    alertDialogue.showAlertMessage("Please select a collection point");
                    return;
                }

                if(advancedLoanBondNoField.getText().toString().length() == 0)
                {
                    advancedLoanBondNoField.requestFocus();
                    alertDialogue.showAlertMessage("Loan bond number can't be empty");
                    return;
                }

//                if(installmentAmountField.getText().toString().length() == 0)
//                {
//                    installmentAmountField.requestFocus();
//                    alertDialogue.showAlertMessage("Instalment amount can't be empty");
//                    return;
//                }

//                if(instalmentSpinner.getSelectedItemPosition() == 0)
//                {
//                    instalmentSpinner.requestFocus();
//                    alertDialogue.showAlertMessage("Please select an instalment period");
//                    return;
//                }

                if(collectedAmountField.getText().toString().length() == 0)
                {
                    collectedAmountField.requestFocus();
                    alertDialogue.showAlertMessage("Collected total amount can't be empty");
                    return;
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedCollectionActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are You sure To Collect?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        ApiRequest apiRequest = new ApiRequest("advanceCollection");
                        try{
                            JSONObject jsonObject = new JSONObject();
                            //---Post JSON Data here---//
                            jsonObject.accumulate("collectionDate",advancedDateField.getText().toString());
                            jsonObject.accumulate("loanId",loanId);
                            jsonObject.accumulate("installmentAmount",installmentAmountField.getText().toString());
                            jsonObject.accumulate("noOfInstallment",instalmentSpinner.getSelectedItemPosition()+1);
                            jsonObject.accumulate("totalPaidAmount",collectedAmountField.getText().toString());

                            apiRequest.set_t(jsonObject);

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(AdvancedCollectionActivity.this).execute(apiRequest);
                    }
                });
                builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        fab.setVisibility(View.GONE);


    }





    private void editFields(AlertDialogue alertDialogue)
    {
        if(advancedLoanBondNoField.getText().toString().length() == 0)
        {
            installmentAmountField.setText(null);
            collectedAmountField.setText(null);
            instalmentSpinner.setSelection(0);
        }
        else if(advancedDateField.getText().toString().length() == 0)
        {
            alertDialogue.showAlertMessage("Please select a payment date");
            return;
        }
        else if(branchSpinner.getSelectedItemPosition() == 0)
        {
            alertDialogue.showAlertMessage("Please select a branch");
            return;
        }
        else if(advancedCollectionPointSpinner.getSelectedItemPosition() == 0)
        {
            alertDialogue.showAlertMessage("Please select a collection point");
            return;
        }

    }



    //---User defined text field validation---//
    private void collectedAmountWatcher(AlertDialogue alertDialogue)
    {
        if(collectedAmountField.getText().toString().length() != 0)
        {
            arithmeticOperation();

//            if(instalmentSpinner.getSelectedItemPosition() == 0)
//            {
//                alertDialogue.showAlertMessage("Please select an instalment period");
//                return;
//            }
        }
    }

    private void arithmeticOperation()
    {
        String collectedAmount = collectedAmountField.getText().toString();
        Integer totalCollection = 0;
        Integer totalCollection2 = 0;

        try
        {
            totalCollection = Integer.parseInt(String.valueOf(outstandingAmount));
            totalCollection2 = Integer.parseInt(installmentAmountField.getText().toString()) * (instalmentSpinner.getSelectedItemPosition()+1);

            if(Integer.parseInt(collectedAmountField.getText().toString()) > totalCollection )
            {
                collectedAmountField.setError("Total paid amount must be less than or equals to the outstanding amount " +totalCollection);
                saveAdvancedButton.setEnabled(false);
                //return;
            }

            else if(!collectedAmount.equals(totalCollection2.toString()))
            {
                collectedAmountField.setError("Total paid amount must be equals to the instalment amount " +totalCollection2);
                saveAdvancedButton.setEnabled(false);
                //return;
            }
            else
            {
                saveAdvancedButton.setEnabled(true);
            }
        }

        catch (NumberFormatException nfe)
        {
            nfe.printStackTrace();
        }
        catch (NullPointerException np)
        {
            np.printStackTrace();
        }
    }
    //---End of user defined text field validation---//



    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if (key.equals("collection-points"))
        {
            //setCollectionPointNamesAdapter(result);
        }
        else if(key.contains("advanceCollection"))
        {
            setAdvancedCollectionAdapter(result);
        }
        else if(key.contains("getUserWiseBranch"))
        {
            setUserWiseBranchAdapter(result);
        }
        else if(key.contains("getBranchWiseCP"))
        {
            setBranchWiseCollectionPointAdapter(result);
        }
        else if(key.contains("getLoanBondNoByCP"))
        {
            setAdvancedLoanBondNoAdapter(result);
        }
    }

    //---Branch names adapter---//
    private void setUserWiseBranchAdapter(String result)
    {
        try {

            JSONArray jsonArray = new JSONArray(result);
            Branch branch1 = new Branch(emptyGuid, "Select Branch");
            userWiseBranchLists.add(branch1);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                Branch branch = new Branch(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                userWiseBranchLists.add(branch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerUserWiseBranchArray = new String[userWiseBranchLists.size()];
        for (int i = 0; i < userWiseBranchLists.size(); i++)
        {
            spinnerUserWiseBranchMap.put(userWiseBranchLists.get(i).getId(),userWiseBranchLists.get(i).getName());
            spinnerUserWiseBranchArray[i] = userWiseBranchLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdvancedCollectionActivity.this, android.R.layout.simple_spinner_item,spinnerUserWiseBranchArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        branchSpinner.setAdapter(adapter);
    }
    //---End of branch names adapter---//

    //---Branch wise collection point adapter---//
    private void setBranchWiseCollectionPointAdapter(String result)
    {
        try {
            JSONArray jsonArray = new JSONArray(result);
            CollPoint collPoint1 = new CollPoint(emptyGuid, "Select Collection Point");
            advancedCollectionPointLists.add(collPoint1);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                CollPoint collPoint = new CollPoint(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                advancedCollectionPointLists.add(collPoint);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerAdvancedCollectionPointNamesArray = new String[advancedCollectionPointLists.size()];
        for (int i = 0; i < advancedCollectionPointLists.size(); i++)
        {
            spinnerAdvancedCollectionPointNamesMap.put(advancedCollectionPointLists.get(i).getId(),advancedCollectionPointLists.get(i).getName());
            spinnerAdvancedCollectionPointNamesArray[i] = advancedCollectionPointLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdvancedCollectionActivity.this, android.R.layout.simple_spinner_item,spinnerAdvancedCollectionPointNamesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        advancedCollectionPointSpinner.setAdapter(adapter);
    }
    //---End of Branch wise collection point adapter---//

    private void setAdvancedCollectionAdapter(String result)
    {
        AlertDialogue alertDialogue = new AlertDialogue(AdvancedCollectionActivity.this);
        if(result.equals("0"))
        {
            alertDialogue.showAlertMessage("Advance collection failed");
        }
        else
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(AdvancedCollectionActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("Advance Collection Save successfully");
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
        }
    }

    private void populateUserWiseBranchNames()
    {
        new ApiHandler.GetAsync(AdvancedCollectionActivity.this).execute("getUserWiseBranch");
    }
    private void populateAdvancedCollectionPointNames(String collectionPointId)
    {
        new ApiHandler.GetAsync(AdvancedCollectionActivity.this).execute("getBranchWiseCP/{"+ collectionPointId +"}");
    }

    private void setAdvancedLoanBondNoAdapter(String result)
    {
        try
        {
            advancedCollectionList = new ArrayList<AdvancedCollection>();
            advancedSearchText = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(result);
            for (int k = 0; k < jsonArray.length(); k++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(k);
                AdvancedCollection advancedCollection = new AdvancedCollection(
                        jsonObject.getString("searchText"),
                        jsonObject.getString("collectionPointId"),
                        jsonObject.getInt("installmentAmount"),
                        jsonObject.getInt("outstandingAmount"),
                        jsonObject.getString("loanId"),
                        jsonObject.getString("loanBondNo"),
                        jsonObject.getInt("loanAmount"),
                        jsonObject.getString("disburseDate"),
                        jsonObject.getString("paymentSchedule")
                );
                advancedCollectionList.add(advancedCollection);
                advancedSearchText.add(jsonObject.getString("loanBondNo"));

            }
            advAdapter.setData(advancedSearchText);
            advAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
//---Ended by Debmalya---//
