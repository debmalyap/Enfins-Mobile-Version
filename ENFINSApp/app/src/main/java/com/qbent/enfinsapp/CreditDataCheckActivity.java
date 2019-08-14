package com.qbent.enfinsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.CreditOrganization;
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

public class CreditDataCheckActivity extends MainActivity implements ApiCallback {

    //---Developed by Debmalya---//
    Button backToMemberListButton,saveCreditDataButton;

    Spinner creditOrganizationSpinner;
    Spinner loanProductSpinner;
    Spinner periodListSpinner;


    private List<CreditOrganization> creditOrganizationList;
    private List<LoanProduct> loanProductList;
    //private List<Installment> installmentList;

    private String creditOrganizationId;
    private String loanProductId = " ";
    private String appliedAmountId = " ";
    private String memberId = " ";
    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    HashMap<String, String> spinnerCreditOrganizationMap = new HashMap<String, String>();
    HashMap<String, String> spinnerLoanProductMap = new HashMap<String, String>();
    //HashMap<String, String> installmentMap = new HashMap<String, String>();

    EditText appliedAmountField;
    EditText appliedInterestRateField;

    String id;
    Double minLoanAmount,maxLoanAmount,maxInterestRate,minInterestRate;
    String installmentPeriodArray;
    //public int installmentPeriodList[];
    //---Ended by Debmalya---//

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_credit_data_check);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_credit_data_check, null, false);
        drawer.addView(contentView, 0);

        memberId = getIntent().getStringExtra("emp_id");

        //---Developed by Debmalya---//
        backToMemberListButton = (Button)findViewById(R.id.backToMember);
        saveCreditDataButton = (Button)findViewById(R.id.saveCreditData);


        creditOrganizationSpinner = (Spinner) findViewById(R.id.spinnerCreditOrganization);
        loanProductSpinner = (Spinner) findViewById(R.id.spinnerLoanProduct);
        periodListSpinner = (Spinner) findViewById(R.id.spinnerInstallment);

        appliedAmountField = (EditText) findViewById(R.id.editTextAppliedAmount);
        appliedAmountField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        appliedInterestRateField = (EditText) findViewById(R.id.editTextInterestRate);
        appliedInterestRateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });


        creditOrganizationList = new ArrayList<CreditOrganization>();
        loanProductList = new ArrayList<LoanProduct>();

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        saveCreditDataButton.setBackground(rippledBg);

        Drawable image2 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg2 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image2, null);
        backToMemberListButton.setBackground(rippledBg2);

        fab.setVisibility(View.GONE);
        //installmentList = new ArrayList<Installment>();

        saveCreditDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if((new ArrayList<String>(spinnerLoanProductMap.keySet())).get(new ArrayList<String>(spinnerLoanProductMap.values()).indexOf(loanProductSpinner.getSelectedItem().toString())).equals(emptyGuid))
                {
                    showAlert("Kindly select Loan Product");
                    return;
                }
                if((new ArrayList<String>(spinnerCreditOrganizationMap.keySet())).get(new ArrayList<String>(spinnerCreditOrganizationMap.values()).indexOf(creditOrganizationSpinner.getSelectedItem().toString())).equals(emptyGuid))
                {
                    showAlert("Kindly select credit organization");
                    return;
                }
                if(appliedAmountField.getText().toString().length()==0)
                {
                    showAlert("Applied amount can not be empty");
                    return;
                }
                if(Double.parseDouble(appliedAmountField.getText().toString()) < minLoanAmount || Double.parseDouble(appliedAmountField.getText().toString()) > maxLoanAmount)
                {
                    showAlert("Applied amount should be between "+ minLoanAmount + " and " + maxLoanAmount);
                    return;
                }
                if(appliedInterestRateField.getText().toString().length()==0)
                {
                    showAlert("Applied interest rate can not be empty");
                    return;
                }
                if(Double.parseDouble(appliedInterestRateField.getText().toString()) < minInterestRate || Double.parseDouble(appliedInterestRateField.getText().toString()) > maxInterestRate)
                {
                    showAlert("Applied interest rate should be between "+ minInterestRate + " and " + maxInterestRate);
                    return;
                }



                final ApiRequest apiRequest = new ApiRequest("saveCreditData");
                try{
                    JSONObject jsonObject = new JSONObject();
                    JSONObject eligibilityDataObject = new JSONObject();


                    jsonObject.put("creditOrganizationId",(new ArrayList<String>(spinnerCreditOrganizationMap.keySet())).get(new ArrayList<String>(spinnerCreditOrganizationMap.values()).indexOf(creditOrganizationSpinner.getSelectedItem().toString())).toString());
                    jsonObject.put("loanProductId",(new ArrayList<String>(spinnerLoanProductMap.keySet())).get(new ArrayList<String>(spinnerLoanProductMap.values()).indexOf(loanProductSpinner.getSelectedItem().toString())).toString());
                    jsonObject.put("memberId",memberId);
                    loanProductId = (new ArrayList<String>(spinnerLoanProductMap.keySet())).get(new ArrayList<String>(spinnerLoanProductMap.values()).indexOf(loanProductSpinner.getSelectedItem().toString())).toString();
                    //jsonObject.accumulate("memberAddres");

                    eligibilityDataObject.put("appliedLoanAmount",(!appliedAmountField.getText().toString().isEmpty())?appliedAmountField.getText().toString():null);
                    eligibilityDataObject.put("appliedInterestRate",(!appliedInterestRateField.getText().toString().isEmpty())?appliedInterestRateField.getText().toString():null);
                    eligibilityDataObject.put("appliedInstallmentPeriod",periodListSpinner.getSelectedItem().toString());



                    jsonObject.put("eligibilityDataByLO",eligibilityDataObject);

                    String value = jsonObject.toString();


                    apiRequest.set_t(jsonObject);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                //---Pop-up window's code---//
                final AlertDialog.Builder builder = new AlertDialog.Builder(CreditDataCheckActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to check credit data?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        new ApiHandler.PostAsync(CreditDataCheckActivity.this).execute(apiRequest);
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
                //---End of pop-up window's code---//



                //populateCollectionPoints(collectionPointId);
            }
        });

        backToMemberListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent memberIntent = new Intent(getApplicationContext(),MemberActivity.class);
                startActivity(memberIntent);
            }
        });

        creditOrganizationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String name = creditOrganizationSpinner.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerCreditOrganizationMap.values());
                int a = indexes.indexOf(name);
                creditOrganizationId = (new ArrayList<String>(spinnerCreditOrganizationMap.keySet())).get(indexes.indexOf(name));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        loanProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String name = loanProductSpinner.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerLoanProductMap.values());
                int a = indexes.indexOf(name);
                loanProductId = (new ArrayList<String>(spinnerLoanProductMap.keySet())).get(indexes.indexOf(name));
                populateAppliedAmount(memberId,loanProductId);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        periodListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String name = periodListSpinner.getSelectedItem().toString();

                //populateInterestRate(memberId,loanProductId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        populateCreditOrganization(creditOrganizationId);

        populateLoanProduct(loanProductId);
        //---Ended by Debmalya---//

    }

    //---Developed by Debmalya---//
    @Override
    public void onApiRequestStart() throws IOException {

    }
    //---Ended by Debmalya---//

    //---Developed by Debmalya---//
    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if (key.equals("get-credit-organization"))
        {
            setCreditOrganizationAdapter(result);
        }
        else if (key.equals("all-loanProduct"))
        {
            setLoanProductAdapter(result);
        }
        else if (key.contains("getLoanProductDtlByProductId"))
        {
            setLoanProductDetailsAdapter(result);
        }
        else if (key.contains("saveCreditData"))
        {
            String message;
            if(result.isEmpty())
            {
                message = "Failed To Check, Due To Insufficient Member Data, Please Update Member.";
            }
            else
            {
                message = "Credit data saved successfully";
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(CreditDataCheckActivity.this);
            builder.setTitle("ENFIN Admin");
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Intent intent = new Intent(getApplicationContext(),MemberEligibityDetailsActivity.class);
                    intent.putExtra("member_eligibity_id",memberId);
                    intent.putExtra("member_eligibity_loanProduct_id",loanProductId);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    //---Ended by Debmalya---//

    //---Developed by Debmalya---//
    private void populateCreditOrganization(String creditOrganizationId)
    {
        new ApiHandler.GetAsync(CreditDataCheckActivity.this).execute("get-credit-organization");
    }

    private void populateLoanProduct(String loanProductId)
    {
        new ApiHandler.GetAsync(CreditDataCheckActivity.this).execute("all-loanProduct");
    }

    private void populateAppliedAmount(String memberId,String loanProductId)
    {
        new ApiHandler.GetAsync(CreditDataCheckActivity.this).execute("getLoanProductDtlByProductId/{"+ memberId +"}/{"+ loanProductId + "}");
    }

//    private void populateInterestRate(String memberId, String loanProductId)
//    {
//        new ApiHandler.GetAsync(CreditDataCheckActivity.this).execute("getLoanProductDtlByProductId/{"+ memberId +"}/{"+ loanProductId + "}");
//    }
    //---Ended by Debmalya---//

    //---Developed by Debmalya---//
    private void setCreditOrganizationAdapter(String result)
    {
        try {
            creditOrganizationList = new ArrayList<CreditOrganization>();

            CreditOrganization creditOrganization1 = new CreditOrganization(emptyGuid,"Select Credit Organization");
            creditOrganizationList.add(creditOrganization1);




            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                CreditOrganization creditOrganization = new CreditOrganization(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                creditOrganizationList.add(creditOrganization);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerCreditOrganizationArray = new String[creditOrganizationList.size()];
        for (int i = 0; i < creditOrganizationList.size(); i++)
        {
            spinnerCreditOrganizationMap.put(creditOrganizationList.get(i).getId(),creditOrganizationList.get(i).getName());
            spinnerCreditOrganizationArray[i] = creditOrganizationList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreditDataCheckActivity.this, simple_spinner_item,spinnerCreditOrganizationArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        creditOrganizationSpinner.setAdapter(adapter);
    }


    private void setLoanProductAdapter(String result)
    {
        try {
            loanProductList = new ArrayList<LoanProduct>();
            JSONArray jsonArray = new JSONArray(result);

            LoanProduct loanProduct1 = new LoanProduct(emptyGuid,"Select Loan Product");
            loanProductList.add(loanProduct1);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                LoanProduct loanProduct = new LoanProduct(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                loanProductList.add(loanProduct);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerLoanProductArray = new String[loanProductList.size()];
        for (int i = 0; i < loanProductList.size(); i++)
        {
            spinnerLoanProductMap.put(loanProductList.get(i).getId(),loanProductList.get(i).getName());
            spinnerLoanProductArray[i] = loanProductList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreditDataCheckActivity.this, simple_spinner_item,spinnerLoanProductArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        loanProductSpinner.setAdapter(adapter);
    }

    private void setLoanProductDetailsAdapter(String result) {
        if(result.equals("null"))
        {
            showAlert("In this loan product, member has already a loan application / collection in progress");
            resetCreditOrganization();
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(result);

            id = jsonObject.getString("id");
            minInterestRate = jsonObject.optDouble("minInterestRate");
            maxLoanAmount = jsonObject.optDouble("maxLoanAmount");
            maxInterestRate = jsonObject.optDouble("maxInterestRate");
            minLoanAmount = jsonObject.optDouble("minLoanAmount");
            creditOrganizationId = jsonObject.getString("creditOrganizationId");
            Boolean inProgressLoanCreditData = jsonObject.optBoolean("inProgressLoanCreditData");
            Boolean isCreditDataChkBeyondLimit = jsonObject.optBoolean("isCreditDataChkBeyondLimit");

            if(inProgressLoanCreditData == true)
            {
                showAlert("This member credit data in progress");
                resetCreditOrganization();
                return;
            }
            if(isCreditDataChkBeyondLimit == true)
            {
                showAlert("You can not Re-Check Within 15 Days");
                resetCreditOrganization();
                return;
            }

            List<String> indexes1 = new ArrayList<String>(spinnerCreditOrganizationMap.keySet());
            int test = indexes1.indexOf(creditOrganizationId);
            String test2 = (new ArrayList<String>(spinnerCreditOrganizationMap.values())).get(test);
            creditOrganizationSpinner.setSelection(((ArrayAdapter<String>)creditOrganizationSpinner.getAdapter()).getPosition(test2));


            JSONArray installPeriodList = jsonObject.getJSONArray("installmentPeriodList");
            String[] installmentPeriodArray = new String[installPeriodList.length()];
            for(int i = 0; i < installPeriodList.length(); i++)
                installmentPeriodArray[i] = installPeriodList.getString(i);


            appliedAmountField.setHint(minLoanAmount + " - " + maxLoanAmount);
            appliedInterestRateField.setHint(minInterestRate + " - " + maxInterestRate);
//
//




            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreditDataCheckActivity.this, simple_spinner_item,installmentPeriodArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            periodListSpinner.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void resetCreditOrganization()
    {
        List<String> indexes1 = new ArrayList<String>(spinnerCreditOrganizationMap.keySet());
        int test = indexes1.indexOf(emptyGuid);
        String test2 = (new ArrayList<String>(spinnerCreditOrganizationMap.values())).get(test);
        creditOrganizationSpinner.setSelection(((ArrayAdapter<String>)creditOrganizationSpinner.getAdapter()).getPosition(test2));
    }

    private void showAlert(String errMessage)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CreditDataCheckActivity.this);
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
    //---Ended by Debmalya---//
}
