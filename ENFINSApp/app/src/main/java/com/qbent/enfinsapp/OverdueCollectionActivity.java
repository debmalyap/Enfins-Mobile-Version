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
import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.global.AuthHelper;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Branch;
import com.qbent.enfinsapp.model.CollPoint;
import com.qbent.enfinsapp.model.Overdue;
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

import static java.lang.String.*;

public class OverdueCollectionActivity extends MainActivity implements ApiCallback
{
    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    Boolean editCollectionPoint = false;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    public static final String TAG = "OverdueCollectionActivity";

    Spinner collectionPointNamesSpinner;

    private List<CollPoint> collectionPointNameLists;

    Button cancelButton,saveButton;

    EditText paymentDateField;
    EditText loanBondNoField;
    EditText overDuePrincipalField;
    EditText overDueInsterestField;
    EditText totalPaidAmountField;

    private Handler loanBondHandler;
    private AutoCompleteAdapter loanBondAdapter;

    private AppCompatAutoCompleteTextView autoCompleteLoanBondNo;

    private List<Overdue> overDueList;
    private List<String> overDueSearchText;

    private String collectionPointId = " ";
    private String loanId = " ";
    private Number overDuePrincipal;
    private Number overDueInterest;

    private String user_collection;

    private AuthHelper _authHelper;
    private Date workingDate,selectionDate;
    private Date date;
    private String workingStringDate;

    HashMap<String, String> spinnerCollectionPointNamesMap = new HashMap<String, String>();
    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_overdue_collection_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_overduecollection);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_overdue_collection, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_overduecollection);
        }

        final AlertDialogue alertDialogue = new AlertDialogue(OverdueCollectionActivity.this);

        saveButton = (Button) findViewById(R.id.saveOverdueButton);
        cancelButton = (Button) findViewById(R.id.cancelOverdueButton);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        saveButton.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        cancelButton.setBackground(rippledBg1);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });



        paymentDateField = (EditText) findViewById(R.id.overduePaymentDateId);

        loanBondNoField = (EditText) findViewById(R.id.loanBondNoId);

        loanBondNoField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        overDuePrincipalField = (EditText) findViewById(R.id.overduePrincipalId);
        overDueInsterestField = (EditText) findViewById(R.id.overdueInterestId);
        totalPaidAmountField = (EditText) findViewById(R.id.paidAmountId);

        totalPaidAmountField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        overDuePrincipalField.setEnabled(false);
        overDueInsterestField.setEnabled(false);

        //---Loan bond number validation---//
        loanBondNoField.addTextChangedListener(new TextWatcher()
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
                saveActivityByText(alertDialogue);
            }
        });
        //---End of Loan bond number validation---//

        //---Total amount validation---//
        totalPaidAmountField.addTextChangedListener(new TextWatcher()
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
                saveActivityByText(alertDialogue);
            }


        });
        //---End of total amount validation---//

        collectionPointNamesSpinner = (Spinner) findViewById(R.id.colcPointNameId);

        collectionPointNameLists = new ArrayList<CollPoint>();

        //---Displaying current Date with edit option---//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
//        paymentDateField.setText((String)dateFormat.format(new Date()));

        _authHelper = AuthHelper.getInstance(this);
        paymentDateField.setEnabled(false);

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
            paymentDateField.setText((String)simpleDateFormat1.format(date));
            paymentDateField.setOnClickListener(new View.OnClickListener()
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
                            OverdueCollectionActivity.this,
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
                    try
                    {
                        selectionDate = simpleDateFormat1.parse(date);
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                    paymentDateField.setText(date);
                }
            };
        } catch (ParseException e) {
            e.printStackTrace();
        }



        //---End of Displaying current Date---//

        collectionPointNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if( user_collection!=null && !user_collection.equals("null")&& !user_collection.equals(emptyGuid) && editCollectionPoint==false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerCollectionPointNamesMap.keySet());
                    int test = indexes1.indexOf(user_collection);
                    String test2 = (new ArrayList<String>(spinnerCollectionPointNamesMap.values())).get(test);
                    collectionPointNamesSpinner.setSelection(((ArrayAdapter<String>)collectionPointNamesSpinner.getAdapter()).getPosition(test2));
                    collectionPointId = user_collection;
                }
                else
                {
                    String name = collectionPointNamesSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerCollectionPointNamesMap.values());
                    int a = indexes.indexOf(name);
                    collectionPointId = (new ArrayList<String>(spinnerCollectionPointNamesMap.keySet())).get(indexes.indexOf(name));
                }
                if(!collectionPointId.equals(loanId))
                {
                    loanBondNoField.setText(null);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//

            }
        });

        populateCollectionPointNames();

        //---AutoComplete of Loan Bond code---//
        autoCompleteLoanBondNo = (AppCompatAutoCompleteTextView) findViewById (R.id.loanBondNoId);
        loanBondAdapter = new AutoCompleteAdapter (this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteLoanBondNo.setThreshold(3);
        autoCompleteLoanBondNo.setAdapter (loanBondAdapter);
        autoCompleteLoanBondNo.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id)
                    {
                        loanBondNoField.setText(loanBondAdapter.getObject(position));
                        Overdue overdue = null;

                        for (int i = 0; i < overDueList.size(); i++)
                        {
                            if(overDueList.get(i).getLoanBondNo() == loanBondAdapter.getObject(position))
                            {
                                overdue = overDueList.get(i);
                            }

                        }

                        loanId = overdue.getLoanId();
                        overDuePrincipal = overdue.getOverDuePrincipal();
                        overDueInterest =  overdue.getOverDueInterest();
                        overDuePrincipalField.setText(String.valueOf(overDuePrincipal));
                        overDueInsterestField.setText(String.valueOf(overDueInterest));



                    }
                });

        autoCompleteLoanBondNo.addTextChangedListener(new TextWatcher()
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
                loanBondHandler.removeMessages(TRIGGER_AUTO_COMPLETE);
                loanBondHandler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                editFields(alertDialogue);
            }
        });
        loanBondHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteLoanBondNo.getText()))
                    {
                        ApiRequest apiRequest = new ApiRequest("search-loanBondNo-overdue");
                        try
                        {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.accumulate("collectedDate",(!paymentDateField.getText().toString().isEmpty())?paymentDateField.getText().toString():null);
                            jsonObject.accumulate("collectionPointId", collectionPointId);
                            jsonObject.accumulate("searchText", (!loanBondNoField.getText().toString().isEmpty())?loanBondNoField.getText().toString():null);

                            apiRequest.set_t(jsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(OverdueCollectionActivity.this).execute(apiRequest);
                    }
                }
                return false;
            }
        });
        // ---End of AutoComplete of Loan Bond code---//

        //saveButton.setEnabled(false);
        saveButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                if(paymentDateField.getText().toString().length()==0)
                {
                    alertDialogue.showAlertMessage("Date field can't be empty");
                    return;
                }

                else if(collectionPointNamesSpinner.getSelectedItemPosition() == 0)
                {
                    alertDialogue.showAlertMessage("Select a collection point");
                    return;
                }

                else if(loanBondNoField.getText().toString().length()==0)
                {
                    alertDialogue.showAlertMessage("Loan bond number can't be empty");
                    return;
                }

                else if(overDuePrincipalField.getText().toString().length()==0)
                {
                    alertDialogue.showAlertMessage("Overdue principal can't be empty");
                    return;
                }

                else if(overDueInsterestField.getText().toString().length()==0)
                {
                    alertDialogue.showAlertMessage("Overdue interest can't be empty");
                    return;
                }

                else if(totalPaidAmountField.getText().toString().length()==0)
                {
                    alertDialogue.showAlertMessage("Total paid amount can't be empty");
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(OverdueCollectionActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to collect overDue?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        ApiRequest apiRequest = new ApiRequest("saveOverDueLoan");
                        try{
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.accumulate("collectedDate",paymentDateField.getText().toString());
                            jsonObject.accumulate("collectionPointId",collectionPointId);
                            jsonObject.accumulate("loanId",loanId);
                            jsonObject.accumulate("loanBondNo",loanBondNoField.getText().toString());
                            jsonObject.accumulate("overDuePrincipal",overDuePrincipalField.getText().toString());
                            jsonObject.accumulate("overDueInterest",overDueInsterestField.getText().toString());
                            jsonObject.accumulate("totalPaidAmount",totalPaidAmountField.getText().toString());

                            apiRequest.set_t(jsonObject);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(OverdueCollectionActivity.this).execute(apiRequest);
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
        if(loanBondNoField.getText().toString().length() == 0)
        {
            overDuePrincipalField.setText(null);
            overDueInsterestField.setText(null);
            totalPaidAmountField.setText(null);
        }
        else if(paymentDateField.getText().toString().length() == 0)
        {
            alertDialogue.showAlertMessage("Please select a specific payment date");
            return;
        }
        else if(collectionPointNamesSpinner.getSelectedItemPosition() == 0)
        {
            alertDialogue.showAlertMessage("Please select a collection point");
            return;
        }
    }

    private void saveActivityByText(AlertDialogue alertDialogue)
    {

//        if(loanBondNoField.getText().toString().length() == 0)
//        {
//
//        }
//        else
//        {
//
//        }
        Double total = 0.0;
        Integer finTotal = null;

        if(totalPaidAmountField.getText().toString().length() != 0)
        {
            try
            {
                total = Double.valueOf(overDuePrincipalField.getText().toString()) + Double.valueOf(overDueInsterestField.getText().toString());
                finTotal = total.intValue();

                if(Integer.parseInt(totalPaidAmountField.getText().toString()) > finTotal)
                {
                    alertDialogue.showAlertMessage("Total paid amount must be less than or equals to " +finTotal);
                    saveButton.setEnabled(false);
                }
                else
                {
                    saveButton.setEnabled(true);
//
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

    //---Showing alert dialogue message---//
    private void showAlert(String errMessage)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(OverdueCollectionActivity.this);
        builder.setTitle("ENFIN Admin");
        builder.setMessage(errMessage);
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
    //---End of alert dialogue message---//


    //---End of user defined field validation method---//


    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if(key.equals("collection-points-loWise"))
        {
            setCollectionPointNamesAdapter(result);
        }
        else if(key.contains("saveOverDueLoan"))
        {
            setOverDueLoanAdapter(result);
        }
        else if(key.contains("search-loanBondNo-overdue"))
        {
            setOverDueLoanBondNoAdapter(result);
        }
    }

    private void setCollectionPointNamesAdapter(String result)
    {
        try
        {
            collectionPointNameLists = new ArrayList<CollPoint>();
            JSONArray jsonArray = new JSONArray(result);
            CollPoint cp1 = new CollPoint(emptyGuid,"Select Collection Point");
            collectionPointNameLists.add(cp1);
            for(int i=0;i<jsonArray.length()-1;i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                CollPoint collPoint = new CollPoint(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                collectionPointNameLists.add(collPoint);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerCollectionPointNamesArray = new String[collectionPointNameLists.size()];
        for (int i = 0; i < collectionPointNameLists.size(); i++)
        {
            spinnerCollectionPointNamesMap.put(collectionPointNameLists.get(i).getId(),collectionPointNameLists.get(i).getName());
            spinnerCollectionPointNamesArray[i] = collectionPointNameLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OverdueCollectionActivity.this, android.R.layout.simple_spinner_item,spinnerCollectionPointNamesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        collectionPointNamesSpinner.setAdapter(adapter);
    }

    private void setOverDueLoanAdapter(String result)
    {
        if(result.equals("0"))
        {
            showAlert("Overdue collection failed");
        }
        else
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(OverdueCollectionActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("Overdue collected successfully");
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


    private void populateCollectionPointNames()
    {
        new ApiHandler.GetAsync(OverdueCollectionActivity.this).execute("collection-points-loWise");
    }

    private void setOverDueLoanBondNoAdapter(String result)
    {
        try
        {
            overDueList = new ArrayList<Overdue>();
            overDueSearchText = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(result);
            for (int k = 0; k < jsonArray.length(); k++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(k);
                Overdue overdue = new Overdue(
                        jsonObject.getString("collectedDate"),
                        jsonObject.getString("searchText"),
                        jsonObject.getString("collectionPointId"),
                        jsonObject.getString("loanId"),
                        jsonObject.getString("loanBondNo"),
                        jsonObject.optDouble("outstandingAmount"),
                        jsonObject.optDouble("overDuePrincipal"),
                        jsonObject.optDouble("overDueInterest"),
                        jsonObject.optDouble("preCloseOutstandingAmount"),
                        jsonObject.optDouble("preCloseInterestAmount")
                );
                overDueList.add(overdue);
                overDueSearchText.add(jsonObject.getString("loanBondNo"));

            }
            loanBondAdapter.setData(overDueSearchText);
            loanBondAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
//---Ended by Debmalya---//