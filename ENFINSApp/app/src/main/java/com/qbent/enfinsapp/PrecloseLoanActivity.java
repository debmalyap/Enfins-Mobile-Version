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
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.CollPoint;
import com.qbent.enfinsapp.model.Preclose;
import com.qbent.enfinsapp.model.State;
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

public class PrecloseLoanActivity extends MainActivity implements ApiCallback
{
    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    Boolean editPreCloseCollectionPointChanged = false;

    String user_collection;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    public static final String TAG = "PrecloseLoanActivity";

    Spinner preCloseCollectionPointSpinner;

    private List<CollPoint> preCloseCollectionPointLists;

    Button cancelPreButton,savePreButton;

    EditText dateField;
    EditText preCloseLoanBondNoField;
    EditText outStandingAmountField;
    EditText interestField;
    EditText totalPaidAmountField;

    private Handler preCloseHandler;
    private AutoCompleteAdapter preCloseAdapter;

    private AppCompatAutoCompleteTextView autoCompletePreCloseLoanBondNo;

    private List<Preclose> preCloseList;
    private List<String> preCloseSearchText;

    private String collectionPointId = " ";
    private String loanId = " ";
    private Double outStandingAmount;
    private Double interestAmount;

    HashMap<String, String> spinnerPreCloseCollectionPointNamesMap = new HashMap<String, String>();

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
            View contentView = inflater.inflate(R.layout.activity_preclose_loan_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_precloseloan);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_preclose_loan, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_precloseloan);
        }

        final AlertDialogue alertDialogue = new AlertDialogue(PrecloseLoanActivity.this);

        preCloseCollectionPointSpinner = (Spinner) findViewById(R.id.preCloseColcPointNameId);

        dateField = (EditText) findViewById(R.id.preClosePaymentDateId);
        preCloseLoanBondNoField = (EditText) findViewById(R.id.preCloseLoanBondNoId);

        preCloseLoanBondNoField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        outStandingAmountField = (EditText) findViewById(R.id.outStandingAmountId);
        interestField = (EditText) findViewById(R.id.interestAmountId);
        totalPaidAmountField = (EditText) findViewById(R.id.preClosePaidAmountId);

        totalPaidAmountField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        savePreButton = (Button) findViewById(R.id.savePreCloseLoanButton);
        cancelPreButton = (Button) findViewById(R.id.cancelPreCloseLoanButton) ;

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        savePreButton.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        cancelPreButton.setBackground(rippledBg1);

        outStandingAmountField.setEnabled(false);
        interestField.setEnabled(false);



        //---Date field---//
        _authHelper = AuthHelper.getInstance(this);
        dateField.setEnabled(false);

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
            dateField.setText((String)simpleDateFormat1.format(date));
            dateField.setOnClickListener(new View.OnClickListener()
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
                            PrecloseLoanActivity.this,
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
                    dateField.setText(date);
                }
            };
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //---End of date field---//

        //---Payment date validation---//
//        dateField.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
//            { }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
//            { }
//
//            @Override
//            public void afterTextChanged(Editable editable)
//            {
//                saveActivityByText();
//            }
//        });
//        //---End of payment date validation---//
//
//        //---Loan bond number validation---//
//        preCloseLoanBondNoField.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
//            { }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
//            { }
//
//            @Override
//            public void afterTextChanged(Editable editable)
//            {
//                saveActivityByText();
//            }
//        });
//        //---End of loan bond number validation---//
//
//        //---Outstanding amount validation---//
//        outStandingAmountField.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
//            { }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
//            { }
//
//            @Override
//            public void afterTextChanged(Editable editable)
//            {
//                saveActivityByText();
//            }
//        });
//        //---End of Outstanding amount validation---//
//
//        //---Interest validation---//
//        interestField.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
//            { }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
//            { }
//
//            @Override
//            public void afterTextChanged(Editable editable)
//            {
//                saveActivityByText();
//            }
//        });
//        //---End of interest validation---//
//
//        //---Total amount validation---//
        totalPaidAmountField.addTextChangedListener(new TextWatcher()
        {
            boolean isOnTextChanged = false;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            { }

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
        //---End of total amount validation---//

        preCloseCollectionPointLists = new ArrayList<CollPoint>();

        //---Pre close collection point names spinner---//
        preCloseCollectionPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if( user_collection!=null && !user_collection.equals("null")&& !user_collection.equals(emptyGuid)&& editPreCloseCollectionPointChanged==false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerPreCloseCollectionPointNamesMap.keySet());
                    int test = indexes1.indexOf(user_collection);
                    String test2 = (new ArrayList<String>(spinnerPreCloseCollectionPointNamesMap.values())).get(test);
                    preCloseCollectionPointSpinner.setSelection(((ArrayAdapter<String>)preCloseCollectionPointSpinner.getAdapter()).getPosition(test2));
                    collectionPointId = user_collection;

                }

                else
                {
                    String name = preCloseCollectionPointSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerPreCloseCollectionPointNamesMap.values());
                    int a = indexes.indexOf(name);
                    collectionPointId = (new ArrayList<String>(spinnerPreCloseCollectionPointNamesMap.keySet())).get(indexes.indexOf(name));
                }
                if(!collectionPointId.equals(loanId))
                {
                    preCloseLoanBondNoField.setText(null);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        preCloseCollectionPointSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editPreCloseCollectionPointChanged = true;
                }
                return false;
            }
        });
        //---End of spinner---//

        //cancelPreButton = (Button) findViewById(R.id.cancelPartButton);
        cancelPreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });



        populatePreCloseCollectionPointNames();

        //---AutoComplete of Pre Close Loan Bond code---//
        autoCompletePreCloseLoanBondNo = (AppCompatAutoCompleteTextView) findViewById (R.id.preCloseLoanBondNoId);
        preCloseAdapter = new AutoCompleteAdapter (this, android.R.layout.simple_dropdown_item_1line);
        autoCompletePreCloseLoanBondNo.setThreshold(3);
        autoCompletePreCloseLoanBondNo.setAdapter (preCloseAdapter);
        autoCompletePreCloseLoanBondNo.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id)
                    {
                        preCloseLoanBondNoField.setText(preCloseAdapter.getObject(position));
                        Preclose preclose = null;

                        for (int i = 0; i < preCloseList.size(); i++)
                        {
                            if(preCloseList.get(i).getLoanBondNo() == preCloseAdapter.getObject(position))
                            {
                                preclose = preCloseList.get(i);
                            }
                        }
                        loanId = preclose.getLoanId();
                        outStandingAmount = preclose.getOutstandingAmount();
                        interestAmount = preclose.getPreCloseInterestAmount();
                        outStandingAmountField.setText(String.valueOf(outStandingAmount));
                        interestField.setText(String.valueOf(interestAmount));


                    }
                });

        autoCompletePreCloseLoanBondNo.addTextChangedListener(new TextWatcher()
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
                preCloseHandler.removeMessages(TRIGGER_AUTO_COMPLETE);
                preCloseHandler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                editFields(alertDialogue);
            }
        });
        preCloseHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompletePreCloseLoanBondNo.getText()))
                    {
                        ApiRequest apiRequest = new ApiRequest("getLoanBondNoBySearchText");
                        try
                        {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.accumulate("collectedDate",(!dateField.getText().toString().isEmpty())?dateField.getText().toString():null);
                            jsonObject.accumulate("collectionPointId", collectionPointId);
                            jsonObject.accumulate("searchText", (!preCloseLoanBondNoField.getText().toString().isEmpty())?preCloseLoanBondNoField.getText().toString():null);

                            apiRequest.set_t(jsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(PrecloseLoanActivity.this).execute(apiRequest);

                    }
                }
                return false;
            }
        });
        //---End of AutoComplete of Pre Close Loan Bond code---//


        //---save preclose loan---//

        savePreButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(dateField.getText().toString().length() == 0)
                {
                    dateField.requestFocus();
                    alertDialogue.showAlertMessage("Payment date can't be empty");
                    return;
                }
                if(preCloseCollectionPointSpinner.getSelectedItemPosition() == 0)
                {
                    preCloseCollectionPointSpinner.requestFocus();
                    alertDialogue.showAlertMessage("Please select a collection point");
                    return;
                }
                if(preCloseLoanBondNoField.getText().toString().length() == 0)
                {
                    preCloseLoanBondNoField.requestFocus();
                    alertDialogue.showAlertMessage("Loan bond number can't be empty");
                    return;
                }
                if(outStandingAmountField.getText().toString().length() == 0)
                {
                    outStandingAmountField.requestFocus();
                    alertDialogue.showAlertMessage("Outstanding amount can't be empty");
                    return;
                }
                if(interestField.getText().toString().length() == 0)
                {
                    interestField.requestFocus();
                    alertDialogue.showAlertMessage("Interest field can't be empty");
                    return;
                }

                if(totalPaidAmountField.getText().toString().length() == 0)
                {
                    totalPaidAmountField.requestFocus();
                    alertDialogue.showAlertMessage("Total paid amount can't be empty");
                    return;
                }

                if((new ArrayList<String>(spinnerPreCloseCollectionPointNamesMap.keySet())).get(new ArrayList<String>(spinnerPreCloseCollectionPointNamesMap.values()).indexOf(preCloseCollectionPointSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {

                    if((new ArrayList<String>(spinnerPreCloseCollectionPointNamesMap.keySet())).get(new ArrayList<String>(spinnerPreCloseCollectionPointNamesMap.values()).indexOf(preCloseCollectionPointSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                    {
                        alertDialogue.showAlertMessage("Please select a collection point");
                    }
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(PrecloseLoanActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to preclosed the loan?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        ApiRequest apiRequest = new ApiRequest("savePreCloseLoan");
                        try{
                            JSONObject jsonObject = new JSONObject();
                            //---Post JSON Data here---//
                            jsonObject.accumulate("collectedDate",dateField.getText().toString());
                            jsonObject.accumulate("collectionPointId",collectionPointId);
                            jsonObject.accumulate("loanId",loanId);
                            jsonObject.accumulate("loanBondNo",preCloseLoanBondNoField.getText().toString());
                            jsonObject.accumulate("preCloseOutstandingAmount",outStandingAmountField.getText().toString());
                            jsonObject.accumulate("preCloseInterestAmount",interestField.getText().toString());
                            jsonObject.accumulate("totalPaidAmount",totalPaidAmountField.getText().toString());

                            apiRequest.set_t(jsonObject);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(PrecloseLoanActivity.this).execute(apiRequest);
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
        //---End of save preclose loan---//

        fab.setVisibility(View.GONE);


    }



    private void editFields(AlertDialogue alertDialogue)
    {
        if(preCloseLoanBondNoField.getText().toString().length() == 0)
        {
            outStandingAmountField.setText(null);
            interestField.setText(null);
            totalPaidAmountField.setText(null);
        }
        else if(preCloseCollectionPointSpinner.getSelectedItemPosition() == 0)
        {
            alertDialogue.showAlertMessage("Please select a collection point");
        }

    }



    //---User defined text field validation---//
    private void saveActivityByText()
    {
        String paidText = totalPaidAmountField.getText().toString();

        Double total = 0.0;
        Integer finTotal = null;

        if(totalPaidAmountField.getText().toString().length() != 0)
        {
            try
            {
                total = Double.valueOf(outStandingAmountField.getText().toString()) + Double.valueOf(interestField.getText().toString());
                finTotal = total.intValue();

                if(!paidText.equals(finTotal.toString()))
                {
                    totalPaidAmountField.setError("Total paid amount must be equals to " +finTotal);
                    savePreButton.setEnabled(false);
                }
                else
                {
                    savePreButton.setEnabled(true);
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


    }
    //---End of user defined text field validation---//

    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if (key.equals("collection-points-loWise"))
        {
            setCollectionPointNames(result);
        }
        else if(key.contains("getLoanBondNoBySearchText"))
        {
            setPreCloseLoanBondNoAdapter(result);
        }
        else if(key.contains("savePreCloseLoan"))
        {
            setPreCloseLoanAdapter(result);
        }
    }

    private void setCollectionPointNames(String result)
    {
        try {

            preCloseCollectionPointLists = new ArrayList<CollPoint>();
            JSONArray jsonArray = new JSONArray(result);
            CollPoint collPoint1 = new CollPoint(emptyGuid,"Select Collection Point Names");
            preCloseCollectionPointLists.add(collPoint1);
            for(int i=0;i<jsonArray.length() - 1;i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                CollPoint collPoint = new CollPoint(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                preCloseCollectionPointLists.add(collPoint);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerPreCloseCollectionPointNamesArray = new String[preCloseCollectionPointLists.size()];
        for (int i = 0; i < preCloseCollectionPointLists.size(); i++)
        {
            spinnerPreCloseCollectionPointNamesMap.put(preCloseCollectionPointLists.get(i).getId(),preCloseCollectionPointLists.get(i).getName());
            spinnerPreCloseCollectionPointNamesArray[i] = preCloseCollectionPointLists.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PrecloseLoanActivity.this, android.R.layout.simple_spinner_item,spinnerPreCloseCollectionPointNamesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        preCloseCollectionPointSpinner.setAdapter(adapter);
    }

    private void setPreCloseLoanAdapter(String result)
    {
        AlertDialogue alertDialogue = new AlertDialogue(PrecloseLoanActivity.this);

        if(result.equals("0"))
        {
            alertDialogue.showAlertMessage("Loan preclosed failed");
        }
        else
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(PrecloseLoanActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("Loan preclosed successfully");
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

    private void populatePreCloseCollectionPointNames()
    {
        new ApiHandler.GetAsync(PrecloseLoanActivity.this).execute("collection-points-loWise");
    }

    private void setPreCloseLoanBondNoAdapter(String result)
    {
        try
        {
            preCloseList = new ArrayList<Preclose>();
            preCloseSearchText = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(result);
            for (int k = 0; k < jsonArray.length(); k++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(k);
                Preclose preclose = new Preclose(
                        jsonObject.getString("collectedDate"),
                        jsonObject.getString("searchText"),
                        jsonObject.getString("collectionPointId"),
                        jsonObject.getString("loanId"),
                        jsonObject.getString("loanBondNo"),
                        jsonObject.getDouble("outstandingAmount"),
                        jsonObject.getDouble("overDuePrincipal"),
                        jsonObject.getInt("overDueInterest"),
                        jsonObject.getDouble("preCloseOutstandingAmount"),
                        jsonObject.getDouble("preCloseInterestAmount")
                );
                preCloseList.add(preclose);
                preCloseSearchText.add(jsonObject.getString("loanBondNo"));

            }
            preCloseAdapter.setData(preCloseSearchText);
            preCloseAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
//---Ended by Debmalya---//
