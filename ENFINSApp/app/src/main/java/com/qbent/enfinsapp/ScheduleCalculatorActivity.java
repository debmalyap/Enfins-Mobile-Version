package com.qbent.enfinsapp;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.model.LoanProduct;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.layout.simple_spinner_item;

public class ScheduleCalculatorActivity extends MainActivity implements ApiCallback
{
    AlertDialogue alertDialogue;
    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    Boolean editLoanProduct = false;
    Spinner scheduleLoanProductSpinner;
    Spinner loanTenureSpinner;

    Button generateButton,cancelButton;

    EditText amountField;
    TextView loanTentureView;

    private List<LoanProduct> scheduleLoanProductList;

    HashMap<String, String> spinnerScheduleLoanProductMap = new HashMap<String, String>();

    private String loanProductId = " ";
    private Double amount;
    private int loanTenure = 0;

    String user_loan;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_schedule_calculator, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_schedulecalculator);

        scheduleLoanProductSpinner = (Spinner) findViewById(R.id.scheduleLoanProductSpinnerId);
        loanTenureSpinner = (Spinner) findViewById(R.id.loanTenurSpinnerId);

        amountField = (EditText) findViewById(R.id.amountId);
        amountField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(false);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        loanTentureView = (TextView) findViewById(R.id.loanTentId);

        generateButton = (Button) findViewById(R.id.generateButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        generateButton.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        cancelButton.setBackground(rippledBg1);

        amountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                saveActivityByText();
            }
        });


        scheduleLoanProductList = new ArrayList<LoanProduct>();

        //---Loan Product Spinner---//
        scheduleLoanProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                String name = scheduleLoanProductSpinner.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerScheduleLoanProductMap.values());
                loanProductId = (new ArrayList<String>(spinnerScheduleLoanProductMap.keySet())).get(indexes.indexOf(name));

                if(user_loan!=null && !user_loan.equals("null")&& !user_loan.equals(emptyGuid) && editLoanProduct == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerScheduleLoanProductMap.keySet());
                    int test = indexes1.indexOf(user_loan);
                    String test2 = (new ArrayList<String>(spinnerScheduleLoanProductMap.values())).get(test);
                    scheduleLoanProductSpinner.setSelection(((ArrayAdapter<String>)scheduleLoanProductSpinner.getAdapter()).getPosition(test2));
                    loanProductId = user_loan;
                }
                else if(loanProductId.equals(emptyGuid))
                {
                    // return;
                }
                if(name.contains("SMALL BUSINESS LOAN - BIWEEKLY"))
                {
                    loanTenureSpinner.setVisibility(View.GONE);
                    loanTentureView.setVisibility(View.GONE);
                }
                if(!name.contains("SMALL BUSINESS LOAN - BIWEEKLY"))
                {
                    loanTenureSpinner.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        scheduleLoanProductSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    editLoanProduct = true;
                }
                return false;
            }
        });
        //---End of Loan Product Spinner---//

        populateLoanProduct();

        //---Loan tenture spinner---//
        try
        {
            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.loan_tenure, android.R.layout.simple_spinner_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            loanTenureSpinner.setAdapter(arrayAdapter);
        }
        catch (UnsupportedOperationException uoe)
        {
            uoe.printStackTrace();
        }


        loanTenureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                loanTenure = Integer.parseInt(String.valueOf(loanTenureSpinner.getSelectedItem()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        //---End of loan tenture spinner---//



        generateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(scheduleLoanProductSpinner.getSelectedItemPosition() == 0)
                {
                    scheduleLoanProductSpinner.requestFocus();
                    showAlert("Please select a loan product");
                    return;
                }

                else if(amountField.getText().toString().length() == 0)
                {
                    amountField.requestFocus();
                    showAlert("Total amount can't be blank");
                    return;
                }

                try
                {
                    amount = new Double(amountField.getText().toString());
                }
                catch (NumberFormatException e)
                {

                }
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://166.62.38.28/reportView/genarateLoanSchedule/" + loanProductId + "/" + amount + "/" + loanTenure ));
                request.setDescription("Schedule Calculator Info");
                request.setTitle("Schedule Report");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "ScheduleReport_" + loanProductId + ".pdf");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                DownloadManager manager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

                final AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleCalculatorActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Loan schedule successfully downloaded");
                builder.setCancelable(true);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        finish();
                        startActivity(getIntent());
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }


        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
        fab.setVisibility(View.GONE);

    }

    //    //---Display alert message method---//
    private void showAlert(String error)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleCalculatorActivity.this);
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
//    //---End of alert message---//

    private void saveActivityByText()
    {

        if( scheduleLoanProductSpinner.getSelectedItemPosition() == 0)
        {
            showAlert("Please select a loan product");
            generateButton.setEnabled(false);
        }
        else
        {
            generateButton.setEnabled(true);
        }
    }


    private void populateLoanProduct()
    {
        new ApiHandler.GetAsync(ScheduleCalculatorActivity.this).execute("all-loanProduct");
    }

    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if (key.equals("all-loanProduct"))
        {
            setScheduleLoanProductAdapter(result);
        }

    }

    private void setScheduleLoanProductAdapter(String result)
    {
        try {
            scheduleLoanProductList = new ArrayList<LoanProduct>();
            JSONArray jsonArray = new JSONArray(result);
            LoanProduct lp = new LoanProduct(emptyGuid, "Select Loan Product");
            scheduleLoanProductList.add(lp);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                LoanProduct loanProduct = new LoanProduct(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                scheduleLoanProductList.add(loanProduct);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerScheduleLoanProductArray = new String[scheduleLoanProductList.size()];
        for (int i = 0; i < scheduleLoanProductList.size(); i++)
        {
            spinnerScheduleLoanProductMap.put(scheduleLoanProductList.get(i).getId(),scheduleLoanProductList.get(i).getName());
            spinnerScheduleLoanProductArray[i] = scheduleLoanProductList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScheduleCalculatorActivity.this, simple_spinner_item,spinnerScheduleLoanProductArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        scheduleLoanProductSpinner.setAdapter(adapter);
    }
}
