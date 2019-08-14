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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.qbent.enfinsapp.adapter.AutoCompleteAdapter;
import com.qbent.enfinsapp.global.AuthHelper;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Branch;
import com.qbent.enfinsapp.model.CollPoint;
import com.qbent.enfinsapp.model.PartPayment;
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

public class PartPaymentCollectionActivity extends MainActivity implements ApiCallback
{
    Boolean editBranchNameChanged = false;
    Boolean editCollectionPointChanged = false;

    String branch_user, collection_user;

    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private DatePickerDialog.OnDateSetListener onDateSetListener;


    Spinner userWiseBranchSpinner;
    Spinner partCollectionPointSpinner;

    private List<CollPoint> partCollectionPointLists;
    private List<Branch> userWiseBranchLists;

    HashMap<String, String> spinnerPartCollectionPointNamesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerUserWiseBranchMap = new HashMap<String, String>();

    private String branchId = " ";
    private String collectionPointId = " ";
    private String loanId = " ";

    private Handler partHandler;
    private AutoCompleteAdapter partAdapter;

    private AppCompatAutoCompleteTextView autoCompletePartPAymentLoanBondNo;

    private List<PartPayment> partPaymentCollectionList;
    private List<String> partSearchText;

    EditText partDateField;
    EditText partLoanBondNoField;
    EditText collectedAmountField;

    private int outstandingAmount;
    private String collcDate;

    Button cancelPartButton,savePartButton;

    private AuthHelper _authHelper;
    private Date workingDate,selectionDate;
    private Date date;
    private String workingStringDate;

    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_part_payment_collection_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_partpaymentcollection);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_part_payment_collection, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_partpaymentcollection);
        }

        userWiseBranchSpinner = (Spinner) findViewById(R.id.partBranchNameId);
        partCollectionPointSpinner = (Spinner) findViewById(R.id.partCollectionId);

        partCollectionPointLists = new ArrayList<CollPoint>();
        userWiseBranchLists = new ArrayList<Branch>();

        partPaymentCollectionList = new ArrayList<PartPayment>();


        partDateField = (EditText) findViewById(R.id.partPaymentDateId);

        partLoanBondNoField = (EditText) findViewById(R.id.partLoanBondNoId);
        partLoanBondNoField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(false);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        collectedAmountField = (EditText) findViewById(R.id.partCollectedAmountId);
        collectedAmountField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(false);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        savePartButton = (Button) findViewById(R.id.savePartButton);
        cancelPartButton = (Button) findViewById(R.id.cancelPartButton);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        savePartButton.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        cancelPartButton.setBackground(rippledBg1);


        //---Date field---//
        _authHelper = AuthHelper.getInstance(this);
        partDateField.setEnabled(false);

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
            partDateField.setText((String)simpleDateFormat1.format(date));
            partDateField.setOnClickListener(new View.OnClickListener()
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
                            PartPaymentCollectionActivity.this,
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
                    partDateField.setText(date);
                }
            };
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //---End of date field---//

//        partLoanBondNoField.addTextChangedListener(new TextWatcher() {
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
//                saveActivityByText();
//            }
//        });

        collectedAmountField.addTextChangedListener(new TextWatcher() {
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
                saveActivityByText();
            }
        });

        //---Branch Name Spinner---//
        userWiseBranchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(branch_user!=null && !branch_user.equals("null")&& !branch_user.equals(emptyGuid) && editBranchNameChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerUserWiseBranchMap.keySet());
                    int test = indexes1.indexOf(branch_user);
                    String test2 = (new ArrayList<String>(spinnerUserWiseBranchMap.values())).get(test);
                    userWiseBranchSpinner.setSelection(((ArrayAdapter<String>)userWiseBranchSpinner.getAdapter()).getPosition(test2));
                    collectionPointId = branch_user;
                }
                else
                {
                    String name = userWiseBranchSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerUserWiseBranchMap.values());
                    int a = indexes.indexOf(name);
                    branchId = (new ArrayList<String>(spinnerUserWiseBranchMap.keySet())).get(indexes.indexOf(name));
                }
                populatePartPaymentCollectionPointNames(branchId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//
            }
        });

        userWiseBranchSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editBranchNameChanged = true;
                }
                return false;
            }
        });
        //---End of Branch Name Spinner---//

        //---Collection Point Names Spinner---//
        partCollectionPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(collection_user!=null && !collection_user.equals("null")&& !collection_user.equals(emptyGuid) && !collectionPointId.equals(emptyGuid)&& editCollectionPointChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerPartCollectionPointNamesMap.keySet());
                    int test = indexes1.indexOf(collection_user);
                    String test2 = (new ArrayList<String>(spinnerPartCollectionPointNamesMap.values())).get(test);
                    partCollectionPointSpinner.setSelection(((ArrayAdapter<String>)partCollectionPointSpinner.getAdapter()).getPosition(test2));
                    collectionPointId = branch_user;
                }
                else
                {
                    String name = partCollectionPointSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerPartCollectionPointNamesMap.values());
                    int a = indexes.indexOf(name);
                    collectionPointId = (new ArrayList<String>(spinnerPartCollectionPointNamesMap.keySet())).get(indexes.indexOf(name));
                }
                if(!collectionPointId.equals(loanId))
                {
                    partLoanBondNoField.setText(null);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        partCollectionPointSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editCollectionPointChanged = true;
                }
                return false;
            }
        });
        //---End of Collection Point Names Spinner---//

        //---Cancel button---//
        cancelPartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
        //---End of cancel button---//

        //---AutoComplete of Part-Payment Collection Loan Bond code---//
        autoCompletePartPAymentLoanBondNo = (AppCompatAutoCompleteTextView) findViewById (R.id.partLoanBondNoId);
        partAdapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
        autoCompletePartPAymentLoanBondNo.setThreshold(3);
        autoCompletePartPAymentLoanBondNo.setAdapter (partAdapter);
        autoCompletePartPAymentLoanBondNo.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id)
                    {
                        partLoanBondNoField.setText(partAdapter.getObject(position));
                        PartPayment partPayment = null;

                        for (int i = 0; i < partPaymentCollectionList.size(); i++)
                        {
                            if(partPaymentCollectionList.get(i).getLoanBondNo() == partAdapter.getObject(position))
                            {
                                partPayment = partPaymentCollectionList.get(i);
                            }
                        }
                        loanId = partPayment.getLoanId();
                        outstandingAmount = partPayment.getOutstandingAmount();

                    }
                });

        autoCompletePartPAymentLoanBondNo.addTextChangedListener(new TextWatcher()
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
                partHandler.removeMessages(TRIGGER_AUTO_COMPLETE);
                partHandler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
//                editFields();
            }
        });
        partHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompletePartPAymentLoanBondNo.getText()))
                    {
                        ApiRequest apiRequest = new ApiRequest("getLoanBondNoByCP");
                        try
                        {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.accumulate("collectionPointId", collectionPointId);
                            jsonObject.accumulate("searchText", (!partLoanBondNoField.getText().toString().isEmpty())?partLoanBondNoField.getText().toString():null);

                            apiRequest.set_t(jsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(PartPaymentCollectionActivity.this).execute(apiRequest);

                    }
                }
                return false;
            }
        });
        //---End of AutoComplete of Part-Payment Collection Loan Bond code---//

        //---Save part-payment collection---//

        savePartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(partDateField.getText().toString().length() == 0)
                {
                    partDateField.requestFocus();
                    showAlert("Payment date can't be empty");
                    return;
                }
                else if(userWiseBranchSpinner.getSelectedItemPosition() == 0)
                {
                    userWiseBranchSpinner.requestFocus();
                    showAlert("Please select a branch");
                    return;
                }
                else if(partCollectionPointSpinner.getSelectedItemPosition() == 0)
                {
                    partCollectionPointSpinner.requestFocus();
                    showAlert("Please select a collection point");
                    return;
                }
                else if(partLoanBondNoField.getText().toString().length() == 0)
                {
                    partLoanBondNoField.requestFocus();
                    showAlert("Provides a specific loan bond number");
                    return;
                }
                else if(collectedAmountField.getText().toString().length() == 0)
                {
                    collectedAmountField.requestFocus();
                    showAlert("Collected amount can't be empty");
                    return;
                }
                else if(loanId == null)
                {
                    showAlert("Please select an appropriate loan id");
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(PartPaymentCollectionActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to collect?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        ApiRequest apiRequest = new ApiRequest("partPaymentCollection");
                        try{
                            JSONObject jsonObject = new JSONObject();
                            //---Post JSON Data here---//
                            jsonObject.accumulate("collectionDate",partDateField.getText().toString());//date
                            jsonObject.accumulate("loanId",loanId);//loanid
                            jsonObject.accumulate("totalPaidAmount",collectedAmountField.getText().toString());

                            apiRequest.set_t(jsonObject);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(PartPaymentCollectionActivity.this).execute(apiRequest);
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
        //---End of save part-payment collection---//

        populateUserWiseBranchNames();
        fab.setVisibility(View.GONE);


    }



    private void editFields()
    {
        if(partLoanBondNoField.getText().toString().length() == 0)
        {
            collectedAmountField.setText(null);
        }
        else if(partDateField.getText().toString().length() == 0)
        {
            showAlert("Please select a specific payment date");
        }
        else if(userWiseBranchSpinner.getSelectedItemPosition() == 0)
        {
            showAlert("Please select a branch");
        }
        else if(partCollectionPointSpinner.getSelectedItemPosition() == 0)
        {
            showAlert("Please select a collection point");
        }

//

    }

    //---Alert dialogue method---//
    private void showAlert(String error)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(PartPaymentCollectionActivity.this);
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
    //---End of alert dialogue---//

    private void saveActivityByText()
    {
        if(collectedAmountField.getText().toString().length() != 0 )
        {
            Integer totalCollection = 0;
            try
            {
                totalCollection = Integer.parseInt(String.valueOf(outstandingAmount));

                if(Integer.parseInt(collectedAmountField.getText().toString()) > totalCollection)
                {
                    showAlert("Total paid amount must be less than or equals to the outstanding amount " +totalCollection);
                    savePartButton.setEnabled(false);
                }
                else
                {
                    savePartButton.setEnabled(true);
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

//        String paymentDateText = partDateField.getText().toString();
//        String loanBondText = partLoanBondNoField.getText().toString();
//        String paidText = collectedAmountField.getText().toString();
//
//        Integer finCollectedAmount = null;
//        try
//        {
//            finCollectedAmount = Integer.valueOf(collectedAmountField.getText().toString());
//        }
//
//        catch (NumberFormatException nfe)
//        {
//            nfe.printStackTrace();
//        }
//
//        try
//        {
//            if(paymentDateText.length() == 0 || loanBondText.length() == 0 || paidText.length() == 0 || finCollectedAmount > outstandingAmount)
//            {
//                if(finCollectedAmount > outstandingAmount)
//                {
//                    //---Pop-up window's code---//
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(PartPaymentCollectionActivity.this);
//                    builder.setTitle("ENFIN Admin");
//                    builder.setMessage("Amount should be equal or less than your outstanding : " +outstandingAmount);
//                    builder.setCancelable(true);
//                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i)
//                        {
//
//                        }
//                    });
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    //---End of pop-up window's code---//
//                }
//
//                savePartButton.setEnabled(false);
//            }
//            else
//            {
//                savePartButton.setEnabled(true);
//            }
//        }
//        catch (NullPointerException ne)
//        {
//            ne.printStackTrace();
//        }
    }



    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if(key.contains("getUserWiseBranch"))
        {
            setUserWiseBranchAdapter(result);
        }
        else if(key.contains("partPaymentCollection"))
        {
            setPartPaymentCollectionAdapter(result);
        }
        else if(key.contains("getBranchWiseCP"))
        {
            setBranchWiseCollectionPointAdapter(result);
        }
        else if(key.contains("getLoanBondNoByCP"))
        {
            setPartPaymentLoanBondNoAdapter(result);
        }
    }

    private void setBranchWiseCollectionPointAdapter(String result)
    {
        try {

            partCollectionPointLists = new ArrayList<CollPoint>();
            JSONArray jsonArray = new JSONArray(result);
            CollPoint cp1 = new CollPoint(emptyGuid, "Select Collection Point Names");
            partCollectionPointLists.add(cp1);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                CollPoint collPoint = new CollPoint(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                partCollectionPointLists.add(collPoint);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerPartPaymentCollectionPointNamesArray = new String[partCollectionPointLists.size()];
        for (int i = 0; i < partCollectionPointLists.size(); i++)
        {
            spinnerPartCollectionPointNamesMap.put(partCollectionPointLists.get(i).getId(),partCollectionPointLists.get(i).getName());
            spinnerPartPaymentCollectionPointNamesArray[i] = partCollectionPointLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PartPaymentCollectionActivity.this, android.R.layout.simple_spinner_item,spinnerPartPaymentCollectionPointNamesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        partCollectionPointSpinner.setAdapter(adapter);
    }

    //---User wise branch names adapter---//
    private void setUserWiseBranchAdapter(String result)
    {
        try {

            userWiseBranchLists = new ArrayList<Branch>();
            JSONArray jsonArray = new JSONArray(result);
            Branch br1 = new Branch(emptyGuid,"Select Branches");
            userWiseBranchLists.add(br1);
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PartPaymentCollectionActivity.this, android.R.layout.simple_spinner_item,spinnerUserWiseBranchArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        userWiseBranchSpinner.setAdapter(adapter);
    }
    //---End of user wise branch names adapter---//

    private void populateUserWiseBranchNames()
    {
        new ApiHandler.GetAsync(PartPaymentCollectionActivity.this).execute("getUserWiseBranch");
    }

    private void populatePartPaymentCollectionPointNames(String branchId)
    {
        new ApiHandler.GetAsync(PartPaymentCollectionActivity.this).execute("getBranchWiseCP/{"+ branchId +"}");
    }

    private void setPartPaymentCollectionAdapter(String result)
    {
        //---Pop-up window's code---//
        if(result.equals("0"))
        {
            showAlert("Failed To Part-Payment Collection due to Collection Date Mismatch");
        }
        else
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(PartPaymentCollectionActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("Part-payment collection successfully completed");
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


        //---End of pop-up window's code---//
    }

    private void setPartPaymentLoanBondNoAdapter(String result)
    {
        try
        {
            partPaymentCollectionList = new ArrayList<PartPayment>();
            partSearchText = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(result);
            for (int k = 0; k < jsonArray.length(); k++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(k);
                PartPayment partPayment = new PartPayment(
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
                partPaymentCollectionList.add(partPayment);
                partSearchText.add(jsonObject.getString("loanBondNo"));

            }
            partAdapter.setData(partSearchText);
            partAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
//---Ended by Debmalya---//
