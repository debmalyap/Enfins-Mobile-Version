package com.qbent.enfinsapp;

//---Developed by Debmalya---//
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.qbent.enfinsapp.adapter.CreditSummartListRecyclerViewMobile;
import com.qbent.enfinsapp.adapter.CreditSummaryListRecyclerViewAdapter;
import com.qbent.enfinsapp.adapter.HomepageAdapter;
import com.qbent.enfinsapp.adapter.HomepageAdapterMobileVersion;
import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.CreditSummary;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.R.layout.simple_spinner_item;

public class MemberEligibityDetailsActivity extends MainActivity implements ApiCallback
{
//    TelephonyManager mTelephonyManager;
//    int PERMISSIONS_REQUEST_CAMERA,PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE,
//            PERMISSIONS_REQUEST_INTERNET,PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE= 0;
//    private String imei;

    final AlertDialogue alertDialogue = new AlertDialogue(MemberEligibityDetailsActivity.this);

    Button backToMemberListButton;

    EditText loanProductNameField;
    EditText appliedAmountField;
    EditText appliedInterestRateField;
    EditText appliedInstallmentField;

    EditText nameField,dateOfBirthField,genderField,ageField,activeAccountField,pastDueAccountField,balanceAmountField
            ,pastDueAmountField,installmentAmountField,writtenOfAmountField,scoreField;

    //---Edited by Debmalya---//
    TextInputLayout title1,title2,title3,title4;

    EditText approvedAmountField,approvedAmountFieldBM,approvedAmountFieldCM,approvedAmountFieldHO,
            approvedInterestRateField,approvedInterestRateFieldBM,approvedInterestRateFieldCM,approvedInterestRateFieldHO,
            remarksField,remarksFieldBM,remarksFieldCM,remarksFieldHO;

    TextView periodSpinnerText,periodSpinnerTextBM,periodSpinnerTextCM,periodSpinnerTextHO;

    LinearLayout loanOfficerLayout,branchManagerLayout,clusterManagerLayout,headOfficeLayout;
    //---Ended by Debmalya---//

    private RecyclerView recyclerView;
    private List<CreditSummary> creditSummaries = new ArrayList<CreditSummary>();


    private String creditOrganizationId, memberId, loanCreditId, eligibilityLoanProductId;

    //---Edited by Debmalya---//
    Boolean isForwardedByLO,isForwardedByBM,isForwardedByCM,isForwardedByHO,isApprovedByLO,isApprovedByBM,isApprovedByCM,isApprovedByHO,
            inProgressLoanCreditData,isRejectedByLO;
    Double appliedLoanAmount, appliedInterestRate,approvedLoanAmount,approvedLoanAmountBM,approvedLoanAmountCM,approvedLoanAmountHO,approvedInterestRate,
            approvedInterestRateBM,approvedInterestRateCM,approvedInterestRateHO;

    int appliedInstallmentPeriod;

    int approvedInstallmentPeriod,approvedInstallmentPeriodBM,approvedInstallmentPeriodCM,approvedInstallmentPeriodHO;
    String consumerName, dateOfBirth,dateOfBirth2, gender, remarks,remarksBM,remarksCM,remarksHO,age;
    //---Ended by Debmalya---//
    int noOfActiveAcc, noOfPastDueAcc;
    Double  totalBalanceAmount, totalPastDueAmount, totalMonthlyInstAmount, totalWritoffAmount, score;
    String loanProductName,loanProductId;
    String id;
    Double minLoanAmount,maxLoanAmount,maxInterestRate,minInterestRate;

    Spinner periodListSpinner,periodListSpinnerBM,periodListSpinnerCM,periodListSpinnerHO;

    private String eligibilityEventId = " ";

    Button approvedButton;
    Button forwardButton;
    Button rejectButton,editButton;

    HashMap<String, String> spinnerLoanProductNameMap = new HashMap<String, String>();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_member_eligibity_details);


        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_member_eligibity_details_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_membereligibitycheck);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_member_eligibity_details, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_membereligibitycheck);
        }

        memberId = getIntent().getStringExtra("member_eligibity_id");
        creditOrganizationId = getIntent().getStringExtra("credit_organization_id");
        loanProductId = getIntent().getStringExtra("member_eligibity_loanProduct_id");

        loanProductNameField = (EditText) findViewById(R.id.loanProductNameId);
        loanProductNameField.setEnabled(false);

        appliedAmountField = (EditText) findViewById(R.id.appliedAmountId);
        appliedAmountField.setEnabled(false);

        appliedInterestRateField = (EditText) findViewById(R.id.appliedInterestRateId);
        appliedInterestRateField.setEnabled(false);

        appliedInstallmentField = (EditText) findViewById(R.id.appliedInstallmentPeriodId1);
        appliedInstallmentField.setEnabled(false);

        approvedButton = (Button) findViewById(R.id.approvedDataButton);
        forwardButton = (Button) findViewById(R.id.forwardDataButton);
        rejectButton = (Button) findViewById(R.id.rejectDataButton);
        editButton = (Button) findViewById(R.id.memberEditButton);



        nameField = findViewById(R.id.nameId);
        nameField.setEnabled(false);

        dateOfBirthField = findViewById(R.id.birthDateId);
        dateOfBirthField.setEnabled(false);

        genderField = findViewById(R.id.genderId);
        genderField.setEnabled(false);

        ageField = findViewById(R.id.ageID);
        ageField.setEnabled(false);

        activeAccountField = findViewById(R.id.activeAmountId);
        activeAccountField.setEnabled(false);

        pastDueAccountField = findViewById(R.id.dueAccountId);
        pastDueAccountField.setEnabled(false);

        balanceAmountField = findViewById(R.id.balanceId);
        balanceAmountField.setEnabled(false);
        pastDueAmountField = findViewById(R.id.pastDueAmountId);
        pastDueAmountField.setEnabled(false);
        installmentAmountField = findViewById(R.id.monthlyInstlId);
        installmentAmountField.setEnabled(false);
        writtenOfAmountField = findViewById(R.id.writtenOfId);
        writtenOfAmountField.setEnabled(false);
        scoreField = findViewById(R.id.scoreId);
        scoreField.setEnabled(false);

        fab.setVisibility(View.GONE);

        //---LO Fields---//
        approvedAmountField = (EditText) findViewById(R.id.approvedAmountId);
        approvedAmountField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        approvedInterestRateField = (EditText) findViewById(R.id.approvedInterestRateId);
        approvedInterestRateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        periodListSpinner = (Spinner) findViewById(R.id.spinnerApprovedInstallment);
        periodSpinnerText = (TextView) findViewById(R.id.spinnerApprovedInstallmentTextId);
        remarksField = findViewById(R.id.remarksId);
        remarksField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        loanOfficerLayout = (LinearLayout) findViewById(R.id.loanOfficerLayoutId);
        branchManagerLayout = (LinearLayout) findViewById(R.id.branchManagerLayoutId);
        clusterManagerLayout = (LinearLayout) findViewById(R.id.clusterManagerLayoutId);
        headOfficeLayout = (LinearLayout) findViewById(R.id.headOfficeLayoutId);
        //---End of Lo fields---//

        //---BM field section---//
        approvedAmountFieldBM = (EditText) findViewById(R.id.approvedAmountId2);
        title1 = (TextInputLayout) findViewById(R.id.approvedAmountTextInputId2);
        title2 = (TextInputLayout) findViewById(R.id.approvedInterestRateTextInputId2);
        approvedInterestRateFieldBM = (EditText) findViewById(R.id.approvedInterestRateId2);
        periodListSpinnerBM = (Spinner) findViewById(R.id.spinnerApprovedInstallment2);
        periodSpinnerTextBM = (TextView) findViewById(R.id.spinnerApprovedInstallmentTextId2);
        remarksFieldBM = findViewById(R.id.remarksId2);
        //---End of BM---//

        //---CM fields section---//
        approvedAmountFieldCM = findViewById(R.id.approvedAmountId3);
        title3 = (TextInputLayout) findViewById(R.id.approvedAmountTextInputId3);
        title4 = (TextInputLayout) findViewById(R.id.approvedInterestRateTextInputId3);
        approvedInterestRateFieldCM = findViewById(R.id.approvedInterestRateId3);
        periodListSpinnerCM = (Spinner) findViewById(R.id.spinnerApprovedInstallment3);
        periodSpinnerTextCM = (TextView) findViewById(R.id.spinnerApprovedInstallmentTextId3);
        remarksFieldCM = findViewById(R.id.remarksId3);
        //---End of CM---//

        //---HO fields section---//
        approvedAmountFieldHO = findViewById(R.id.approvedAmountId4);
        approvedInterestRateFieldHO = findViewById(R.id.approvedInterestRateId4);
        periodListSpinnerHO = (Spinner) findViewById(R.id.spinnerApprovedInstallment4);
        periodSpinnerTextHO = (TextView) findViewById(R.id.spinnerApprovedInstallmentTextId4);
        remarksFieldHO = findViewById(R.id.remarksId4);
        //---End of HO---//


        backToMemberListButton = (Button) findViewById(R.id.cancelButton);
        backToMemberListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MemberEligibityCheckActivity.class));
            }
        });
        populateMemberEligibityDetails(memberId);
        populateCreditSummary(memberId);

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent3 = new Intent(MemberEligibityDetailsActivity.this, MemberEditActivity.class);
                intent3.putExtra("emp_id",memberId);
                view.getContext().startActivity(intent3);
            }

        });

        //---Code for approved---//
        approvedButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(approvedAmountField.getText().toString().isEmpty())
                {
                    alertDialogue.showAlertMessage("Please Enter Approved Amount");
                    return;
                }
                if(approvedInterestRateField.getText().toString().isEmpty())
                {
                    alertDialogue.showAlertMessage("Please Enter Approved Interest Rate");
                    return;
                }
                if(periodListSpinner.getSelectedItem().toString().isEmpty())
                {
                    alertDialogue.showAlertMessage("Please Enter Approved Instalment Period");
                    return;
                }
                if(remarksField.getText().toString().isEmpty())
                {
                    alertDialogue.showAlertMessage("Please Enter a Remarks");
                    return;
                }
                if(Double.parseDouble(approvedAmountField.getText().toString()) < minLoanAmount || Double.parseDouble(approvedAmountField.getText().toString()) > maxLoanAmount)
                {
                    alertDialogue.showAlertMessage("Approved amount should be between "+ minLoanAmount + " and " + maxLoanAmount);
                    return;
                }
                if(Double.parseDouble(approvedInterestRateField.getText().toString()) < minInterestRate || Double.parseDouble(approvedInterestRateField.getText().toString()) > maxInterestRate)
                {
                    alertDialogue.showAlertMessage("Approved interest rate should be between "+ minInterestRate + " and " + maxInterestRate);
                    return;
                }
                approvedButton.setVisibility(View.VISIBLE);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MemberEligibityDetailsActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are You sure To Approved?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        ApiRequest apiRequest = new ApiRequest("approveMemberByLO");
                        try{
                            JSONObject jsonObject = new JSONObject();
                            JSONObject eligibilityDataByLOObject = new JSONObject();

                            jsonObject.put("creditOrganizationId",creditOrganizationId);

                            eligibilityDataByLOObject.put("loanCreditId",loanCreditId);
                            eligibilityDataByLOObject.put("eligibilityEventId",eligibilityEventId);
                            eligibilityDataByLOObject.put("appliedLoanAmount",(!appliedAmountField.getText().toString().isEmpty())?appliedAmountField.getText():null);
                            eligibilityDataByLOObject.put("appliedInterestRate",(!appliedInterestRateField.getText().toString().isEmpty())?appliedInterestRateField.getText():null);
                            eligibilityDataByLOObject.put("appliedInstallmentPeriod",(!appliedInstallmentField.getText().toString().isEmpty())?appliedInstallmentField.getText():null);
                            eligibilityDataByLOObject.put("approvedLoanAmount",(!approvedAmountField.getText().toString().isEmpty())?approvedAmountField.getText():null);
                            eligibilityDataByLOObject.put("approvedInterestRate",(!approvedInterestRateField.getText().toString().isEmpty())?approvedInterestRateField.getText():null);
                            eligibilityDataByLOObject.put("approvedInstallmentPeriod",(periodListSpinner.getSelectedItem().toString()));
                            eligibilityDataByLOObject.put("remarks",(!remarksField.getText().toString().isEmpty())?remarksField.getText().toString():null);

                            jsonObject.put("loanProductId",loanProductId);
                            jsonObject.put("memberId",memberId);

                            jsonObject.put("eligibilityDataByLO",eligibilityDataByLOObject);
                            apiRequest.set_t(jsonObject);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(MemberEligibityDetailsActivity.this).execute(apiRequest);
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
                return;
            }

        });
        //---end of approved code---//

        //---Code for forward---//
        forwardButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(remarksField.getText().toString().isEmpty())
                        {
                            alertDialogue.showAlertMessage("Please Enter a Remarks");
                            return;
                        }
                        forwardButton.setEnabled(true);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MemberEligibityDetailsActivity.this);
                        builder.setTitle("ENFIN Admin");
                        builder.setMessage("Are You sure To Forward?");
                        builder.setCancelable(true);
                        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                ApiRequest apiRequest = new ApiRequest("forwardMemberByLO");
                                try{
                                    JSONObject jsonObject = new JSONObject();
                                    JSONObject eligibilityDataByLOObject = new JSONObject();

                                    jsonObject.put("creditOrganizationId",creditOrganizationId);

                                    eligibilityDataByLOObject.put("loanCreditId",loanCreditId);
                                    eligibilityDataByLOObject.put("eligibilityEventId",eligibilityEventId);
                                    eligibilityDataByLOObject.put("appliedLoanAmount",(!appliedAmountField.getText().toString().isEmpty())?appliedAmountField.getText().toString():null);
                                    eligibilityDataByLOObject.put("appliedInterestRate",(!appliedInterestRateField.getText().toString().isEmpty())?appliedInterestRateField.getText().toString():null);
                                    eligibilityDataByLOObject.put("appliedInstallmentPeriod",(!appliedInstallmentField.getText().toString().isEmpty())?appliedInstallmentField.getText().toString():null);
                                    eligibilityDataByLOObject.put("approvedLoanAmount",(!approvedAmountField.getText().toString().isEmpty())?approvedAmountField.getText().toString():null);
                                    eligibilityDataByLOObject.put("approvedInterestRate",(!approvedInterestRateField.getText().toString().isEmpty())?approvedInterestRateField.getText().toString():null);
                                    eligibilityDataByLOObject.put("approvedInstallmentPeriod",(periodListSpinner.getSelectedItem().toString()));
                                    eligibilityDataByLOObject.put("remarks",(!remarksField.getText().toString().isEmpty())?remarksField.getText().toString():null);

                                    jsonObject.put("loanProductId",loanProductId);
                                    jsonObject.put("memberId",memberId);

                                    jsonObject.put("eligibilityDataByLO",eligibilityDataByLOObject);
                                    apiRequest.set_t(jsonObject);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                                new ApiHandler.PostAsync(MemberEligibityDetailsActivity.this).execute(apiRequest);
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
        //---End of code for forward---//


        //---Code for reject---//
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(remarksField.getText().toString().isEmpty())
                {
                    alertDialogue.showAlertMessage("Please Enter a Remarks");
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(MemberEligibityDetailsActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are You sure To Reject?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        ApiRequest apiRequest = new ApiRequest("rejectMemberByLO");
                        try{
                            JSONObject jsonObject = new JSONObject();
                            JSONObject eligibilityDataByLOObject = new JSONObject();

                            jsonObject.put("creditOrganizationId",creditOrganizationId);

                            eligibilityDataByLOObject.put("loanCreditId",loanCreditId);
                            eligibilityDataByLOObject.put("eligibilityEventId",eligibilityEventId);
                            eligibilityDataByLOObject.put("appliedLoanAmount",(!appliedAmountField.getText().toString().isEmpty())?appliedAmountField.getText().toString():null);
                            eligibilityDataByLOObject.put("appliedInterestRate",(!appliedInterestRateField.getText().toString().isEmpty())?appliedInterestRateField.getText().toString():null);
                            eligibilityDataByLOObject.put("appliedInstallmentPeriod",(!appliedInstallmentField.getText().toString().isEmpty())?appliedInstallmentField.getText().toString():null);
                            eligibilityDataByLOObject.put("approvedLoanAmount",(!approvedAmountField.getText().toString().isEmpty())?approvedAmountField.getText().toString():null);
                            eligibilityDataByLOObject.put("approvedInterestRate",(!approvedInterestRateField.getText().toString().isEmpty())?approvedInterestRateField.getText().toString():null);
                            eligibilityDataByLOObject.put("approvedInstallmentPeriod",(periodListSpinner.getSelectedItem().toString()));
                            eligibilityDataByLOObject.put("remarks",(!remarksField.getText().toString().isEmpty())?remarksField.getText().toString():null);

                            jsonObject.put("loanProductId",loanProductId);
                            jsonObject.put("memberId",memberId);

                            jsonObject.put("eligibilityDataByLO",eligibilityDataByLOObject);
                            apiRequest.set_t(jsonObject);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(MemberEligibityDetailsActivity.this).execute(apiRequest);
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
        //---End of code for reject---//

//        //TODO Permission  Debmalaya//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//        {
//            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//            {
//                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
//            }
//            else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//            {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
//            }
//            else if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
//            {
//                requestPermissions(new String[]{Manifest.permission.INTERNET}, PERMISSIONS_REQUEST_INTERNET);
//            }
//            else if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
//            {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE);
//            }
//            else {
//                getDeviceImei();
//            }
//        }
//        else
//        {
//            getDeviceImei();
//        }
//        //TODO End//

    }

//    //TODO method implemented by Debmalya//
//    private void getDeviceImei()
//    {
//        mTelephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        //mTelephonyManager2 = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//        {
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//        {
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
//        {
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
//        {
//            return;
//        }
//        imei = mTelephonyManager.getDeviceId();
//        //imei2 = mTelephonyManager2.getDeviceId();
//    }
//
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        if (requestCode == PERMISSIONS_REQUEST_CAMERA
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//        {
//            getDeviceImei();
//        }
//        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//        {
//            getDeviceImei();
//        }
//        if (requestCode == PERMISSIONS_REQUEST_INTERNET
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//        {
//            getDeviceImei();
//        }
//        if (requestCode == PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//        {
//            getDeviceImei();
//        }
//    }
//    //TODO End//


    @Override
    public void onApiRequestStart() throws IOException {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException {
        if (key.contains("getCreditData") && !key.contains("getCreditDataChkDate")) {
            try {
                setMemberEligibityDetailsAdapter(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (key.contains("getCreditDataChkDate"))
        {
            setCreditSummaryAdapter(result);
        }
        else if (key.contains("getLoanProductDtlByProductId"))
        {
            setLoanProductDetailsAdapter(result);
        }
        else if (key.contains("approveMemberByLO"))
        {
            setApprovedDataAdapter(result);
        }
        else if (key.contains("forwardMemberByLO"))
        {
            setforwardDataAdapter(result);
        }
        else if (key.contains("rejectMemberByLO"))
        {
            setRejectDataAdapter(result);
        }
    }

    private void setMemberEligibityDetailsAdapter(String result) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {

            JSONObject jsonObject = new JSONObject(result);
            //---Data binding for First table layout---//
            memberId = jsonObject.getString("memberId");
            loanProductId = jsonObject.getString("loanProductId");

            loanProductName = jsonObject.getString("loanProductName");
            appliedLoanAmount = jsonObject.getJSONObject("eligibilityDataByLO").optDouble("appliedLoanAmount");
            appliedInterestRate = jsonObject.getJSONObject("eligibilityDataByLO").optDouble("appliedInterestRate");
            appliedInstallmentPeriod = jsonObject.getJSONObject("eligibilityDataByLO").optInt("appliedInstallmentPeriod");

            loanProductNameField.setText(loanProductName);
            appliedAmountField.setText(appliedLoanAmount.toString());
            appliedInterestRateField.setText(appliedInterestRate.toString());
            appliedInstallmentField.setText(appliedInstallmentPeriod + " ");
            //---End of First table layout Data Binding---//


            //---Data Binding for Credit Summary Details---//
            consumerName = jsonObject.getJSONObject("equifaxCreditDetail").getString("consumerName");
            dateOfBirth = jsonObject.getJSONObject("equifaxCreditDetail").getString("dateOfBirth");
            gender = jsonObject.getJSONObject("equifaxCreditDetail").getString("gender");
            age = jsonObject.getJSONObject("equifaxCreditDetail").getString("age");
            noOfActiveAcc = jsonObject.getJSONObject("equifaxCreditDetail").optInt("noOfActiveAcc");
            noOfPastDueAcc = jsonObject.getJSONObject("equifaxCreditDetail").optInt("noOfPastDueAcc");
            totalBalanceAmount = jsonObject.getJSONObject("equifaxCreditDetail").optDouble("totalBalanceAmount");
            totalPastDueAmount = jsonObject.getJSONObject("equifaxCreditDetail").optDouble("totalPastDueAmount");
            totalMonthlyInstAmount = jsonObject.getJSONObject("equifaxCreditDetail").optDouble("totalMonthlyInstAmount");
            totalWritoffAmount = jsonObject.getJSONObject("equifaxCreditDetail").optDouble("totalWritoffAmount");
            score = jsonObject.getJSONObject("equifaxCreditDetail").optDouble("score");


            SimpleDateFormat dateFormatEdit = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmtOut = new SimpleDateFormat("MM-dd-yyyy");

            //            nameField.setText(consumerName);
//            if(dateOfBirth!="null")
//            {
//                dateOfBirthField.setText(fmtOut2.format(date));
//            }
//            if(gender!="null")
//            {
//                genderField.setText(gender);
//            }
//            if(age!="null")
//            {
//                ageField.setText(age);
//            }
//            activeAccountField.setText(String.valueOf(noOfActiveAcc));
//            pastDueAccountField.setText(String.valueOf(noOfPastDueAcc));
//            if(!totalBalanceAmount.isNaN())
//            {
//                balanceAmountField.setText(totalBalanceAmount.toString());
//            }
//            if(!totalPastDueAmount.isNaN())
//            {
//                pastDueAmountField.setText(totalPastDueAmount.toString());
//            }
//            if(!totalMonthlyInstAmount.isNaN())
//            {
//                installmentAmountField.setText(totalMonthlyInstAmount.toString());
//            }
//            if(!totalWritoffAmount.isNaN())
//            {
//                writtenOfAmountField.setText(totalWritoffAmount.toString());
//            }
//            if(!score.isNaN())
//            {
//                scoreField.setText(score.toString());
//            }

            //---End of Data Binding for Credit Summary Details---//

            //---Data Binding for Member Eligibity For Loan Officer---//
            loanCreditId = jsonObject.getJSONObject("eligibilityDataByLO").getString("loanCreditId");
            eligibilityEventId = jsonObject.getJSONObject("eligibilityDataByLO").getString("eligibilityEventId");


            //---Edited by Debmalya---//

            //---Loan officer section---//
            approvedLoanAmount = jsonObject.getJSONObject("eligibilityDataByLO").optDouble("approvedLoanAmount");
            approvedInterestRate = jsonObject.getJSONObject("eligibilityDataByLO").optDouble("approvedInterestRate");
            approvedInstallmentPeriod = jsonObject.getJSONObject("eligibilityDataByLO").optInt("approvedInstallmentPeriod");
            remarks = jsonObject.getJSONObject("eligibilityDataByLO").getString("remarks");

            //---If LO forwarded then only remarks field will be visible---..
            isApprovedByLO = jsonObject.getJSONObject("eligibilityDataByLO").getBoolean("isApproved");

            if(isApprovedByLO)
            {
                approvedAmountField.setEnabled(false);
                approvedInterestRateField.setEnabled(false);
                periodListSpinner.setEnabled(false);
                remarksField.setEnabled(false);

                approvedAmountField.setText(String.valueOf(approvedLoanAmount));
                approvedInterestRateField.setText(String.valueOf(approvedInterestRate));
                periodListSpinner.setPrompt(String.valueOf(approvedInstallmentPeriod));
                remarksField.setText(remarks + " ");
            }
//            else
//                {
//                    approvedAmountField.setEnabled(true);
//                    approvedInterestRateField.setEnabled(true);
//                    periodListSpinner.setEnabled(true);
//                    remarksField.setEnabled(true);
//
//                    remarksField.setText(remarks + " ");
//                }
            //---End of remarks---//

            isForwardedByLO = jsonObject.getJSONObject("eligibilityDataByLO").getBoolean("isForwarded");
            isRejectedByLO = jsonObject.getJSONObject("eligibilityDataByLO").getBoolean("isRejected");

            //---Hide buttons if user done approved,forward or reject, i.e any of them---//
            if(isForwardedByLO.equals(true) || isApprovedByLO.equals(true) || isRejectedByLO.equals(true))
            {
                approvedButton.setVisibility(View.GONE);
                forwardButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
            }
            else
            {
                approvedButton.setVisibility(View.VISIBLE);
                forwardButton.setVisibility(View.VISIBLE);
                rejectButton.setVisibility(View.VISIBLE);
            }
            //---End of button task---//

            //---Only remarks field if it is "checked or forwarded---//
            if(isForwardedByLO.equals(true))
            {
                approvedAmountField.setEnabled(false);
                approvedInterestRateField.setEnabled(false);
                periodListSpinner.setEnabled(false);
                remarksField.setEnabled(false);
                remarksField.setText(remarks + " ");
            }

            approvedLoanAmountBM = jsonObject.getJSONObject("eligibilityDataByBM").optDouble("approvedLoanAmount");
            approvedInterestRateBM = jsonObject.getJSONObject("eligibilityDataByBM").optDouble("approvedInterestRate");
            approvedInstallmentPeriodBM = jsonObject.getJSONObject("eligibilityDataByBM").optInt("approvedInstallmentPeriod");
            remarksBM = jsonObject.getJSONObject("eligibilityDataByBM").getString("remarks");

            isApprovedByBM = jsonObject.getJSONObject("eligibilityDataByBM").getBoolean("isApproved");
            if(isApprovedByBM)
            {
                approvedAmountFieldBM.setEnabled(true);
                approvedInterestRateFieldBM.setEnabled(true);
                periodListSpinnerBM.setEnabled(true);
                periodSpinnerTextBM.setEnabled(true);

                approvedAmountFieldBM.setText(approvedLoanAmountBM + " ");
                approvedInterestRateFieldBM.setText(approvedInterestRateBM + " ");
                periodListSpinnerBM.setPrompt(approvedInstallmentPeriodBM + " ");
                remarksFieldBM.setText(remarksBM + " ");
            }
            else
            {
                approvedAmountFieldBM.setEnabled(false);
                approvedInterestRateFieldBM.setEnabled(false);
                periodListSpinnerBM.setEnabled(false);
                periodSpinnerTextBM.setEnabled(false);
                title1.setEnabled(false);
                title2.setEnabled(false);

                remarksFieldBM.setEnabled(false);
                remarksFieldBM.setText(remarksBM + " ");
            }
            //---End of Branch manager---//

            //---Cluster manager section---//
            approvedLoanAmountCM = jsonObject.getJSONObject("eligibilityDataByCM").optDouble("approvedLoanAmount");
            approvedInterestRateCM = jsonObject.getJSONObject("eligibilityDataByCM").optDouble("approvedInterestRate");
            approvedInstallmentPeriodCM = jsonObject.getJSONObject("eligibilityDataByCM").optInt("approvedInstallmentPeriod");
            remarksCM = jsonObject.getJSONObject("eligibilityDataByCM").getString("remarks");

            isApprovedByCM = jsonObject.getJSONObject("eligibilityDataByCM").getBoolean("isApproved");
            if(isApprovedByCM)
            {

                approvedAmountFieldCM.setEnabled(true);
                approvedInterestRateFieldCM.setEnabled(true);
                periodListSpinnerCM.setEnabled(true);
                remarksFieldCM.setEnabled(true);

                approvedAmountFieldCM.setText(approvedLoanAmountCM + " ");
                approvedInterestRateFieldCM.setText(approvedInterestRateCM + " ");
                periodListSpinnerCM.setPrompt(approvedInstallmentPeriodBM + " ");
                remarksFieldCM.setText(remarksCM + " ");
            }
            else
            {
                approvedAmountFieldCM.setEnabled(false);
                approvedInterestRateFieldCM.setEnabled(false);
                periodListSpinnerCM.setEnabled(false);
                remarksFieldCM.setEnabled(false);
                title3.setEnabled(false);
                title4.setEnabled(false);


                remarksFieldCM.setEnabled(false);
                remarksFieldCM.setText(remarksCM + " ");
            }
            //---End of cluster manager---//

            //---Show and hide of different layouts---//
            remarksBM = jsonObject.getJSONObject("eligibilityDataByBM").getString("remarks");
            if(remarksBM.equals("null"))
            {
                branchManagerLayout.setVisibility(View.GONE);
            }
            else
            {
                branchManagerLayout.setVisibility(View.VISIBLE);
            }

            remarksCM = jsonObject.getJSONObject("eligibilityDataByCM").getString("remarks");
            if(remarksCM.equals("null"))
            {
                clusterManagerLayout.setVisibility(View.GONE);
            }
            else
            {
                clusterManagerLayout.setVisibility(View.VISIBLE);
            }

            //---HO section---//
            approvedLoanAmountHO = jsonObject.getJSONObject("eligibilityDataByHO").optDouble("approvedLoanAmount");
            approvedInterestRateHO = jsonObject.getJSONObject("eligibilityDataByHO").optDouble("approvedInterestRate");
            approvedInstallmentPeriodHO = jsonObject.getJSONObject("eligibilityDataByHO").optInt("approvedInstallmentPeriod");
            remarksHO = jsonObject.getJSONObject("eligibilityDataByHO").getString("remarks");

            isApprovedByHO = jsonObject.getJSONObject("eligibilityDataByHO").getBoolean("isApproved");
            if(isApprovedByHO)
            {
                approvedAmountFieldHO.setVisibility(View.VISIBLE);
                approvedInterestRateFieldHO.setVisibility(View.VISIBLE);
                periodListSpinnerHO.setVisibility(View.VISIBLE);
                periodSpinnerTextHO.setVisibility(View.VISIBLE);

                approvedAmountFieldHO.setEnabled(false);
                approvedInterestRateFieldHO.setEnabled(false);
                periodListSpinnerHO.setEnabled(false);
                remarksFieldHO.setEnabled(false);

                approvedAmountFieldHO.setText(approvedLoanAmountHO + " ");
                approvedInterestRateFieldHO.setText(approvedInterestRateHO + " ");
                periodListSpinnerHO.setPrompt(approvedInstallmentPeriodHO + " ");
                remarksFieldHO.setText(remarksHO + " ");
            }
            else
            {
                approvedAmountFieldHO.setVisibility(View.GONE);
                approvedInterestRateFieldHO.setVisibility(View.GONE);
                periodListSpinnerHO.setVisibility(View.GONE);
                periodSpinnerTextHO.setVisibility(View.GONE);
            }
            //---End of HO---//

            remarksHO = jsonObject.getJSONObject("eligibilityDataByHO").getString("remarks");
            if(remarksHO.equals("null"))
            {
                headOfficeLayout.setVisibility(View.GONE);
            }
            else
            {
                headOfficeLayout.setVisibility(View.VISIBLE);
            }
            //---End of show and hide---//

            if(!approvedLoanAmount.isNaN())
            {
                approvedAmountField.setHint(approvedLoanAmount.toString());
            }
            if(!approvedInterestRate.isNaN())
            {
                approvedInterestRateField.setHint(approvedInterestRate.toString());
            }

//            remarksField.setText(remarks + " ");
//            remarksFieldBM.setText(remarksBM + " ");
//            remarksFieldCM.setText(remarksCM + " ");
            //---Ended by Debmalya---//

            populateAppliedAmount(memberId,loanProductId);


            //---End of Data Binding for Member Eligibity For Loan Officer---//

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try
        {
            Date date = dateFormat.parse(dateOfBirth);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");

            nameField.setText(consumerName);
            if(dateOfBirth!="null")
            {
                dateOfBirthField.setText(fmtOut.format(date));
            }
            if(gender!="null")
            {
                genderField.setText(gender);
            }
            if(age!="null")
            {
                ageField.setText(age);
            }
            activeAccountField.setText(String.valueOf(noOfActiveAcc));
            pastDueAccountField.setText(String.valueOf(noOfPastDueAcc));
            if(!totalBalanceAmount.isNaN())
            {
                balanceAmountField.setText(totalBalanceAmount.toString());
            }
            if(!totalPastDueAmount.isNaN())
            {
                pastDueAmountField.setText(totalPastDueAmount.toString());
            }
            if(!totalMonthlyInstAmount.isNaN())
            {
                installmentAmountField.setText(totalMonthlyInstAmount.toString());
            }
            if(!totalWritoffAmount.isNaN())
            {
                writtenOfAmountField.setText(totalWritoffAmount.toString());
            }
            if(!score.isNaN())
            {
                scoreField.setText(score.toString());
            }
        }
        catch (ParseException exc)
        {
            exc.printStackTrace();
        }
        catch (NullPointerException npe)
        {
            npe.printStackTrace();
        }


    }

    private void setCreditSummaryAdapter(String result)
    {
        try {
            creditSummaries = new ArrayList<CreditSummary>();
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String form_Date = jsonObject.getString("checkingDate").substring(0,10);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(form_Date);

                SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");
                CreditSummary creditSummary = new CreditSummary(
                        jsonObject.getString("id"),
                        fmtOut.format(date),
                        jsonObject.getString("organizationName")
                );
                creditSummaries.add(creditSummary);
            }

            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
            {
                CreditSummartListRecyclerViewMobile creditSummartListRecyclerViewMobile = new CreditSummartListRecyclerViewMobile(creditSummaries);
                recyclerView = findViewById(R.id.recyclerViewCreditSummary);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(creditSummartListRecyclerViewMobile);
                recyclerView.setLayoutManager(new LinearLayoutManager(MemberEligibityDetailsActivity.this));
            }
            else
            {
                CreditSummaryListRecyclerViewAdapter creditSummaryListRecyclerViewAdapter = new CreditSummaryListRecyclerViewAdapter(creditSummaries);
                recyclerView = findViewById(R.id.recyclerViewCreditSummary);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(creditSummaryListRecyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MemberEligibityDetailsActivity.this));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setApprovedDataAdapter(String result)
    {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberEligibityDetailsActivity.this);
        builder1.setTitle("ENFIN Admin");
        builder1.setMessage("Member Approved Successfully");
        builder1.setCancelable(true);
        builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface1, int i)
            {
                Intent intent = new Intent(getApplicationContext(),MemberEligibityCheckActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog1 = builder1.create();
        alertDialog1.show();

    }

    private void setforwardDataAdapter(String result)
    {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberEligibityDetailsActivity.this);
        builder1.setTitle("ENFIN Admin");
        builder1.setMessage("Member Forwarded Successfully");
        builder1.setCancelable(true);
        builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface1, int i)
            {
                Intent intent = new Intent(getApplicationContext(),MemberEligibityCheckActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog1 = builder1.create();
        alertDialog1.show();
    }

    private void setRejectDataAdapter(String result)
    {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberEligibityDetailsActivity.this);
        builder1.setTitle("ENFIN Admin");
        builder1.setMessage("Member Rejected Successfully");
        builder1.setCancelable(true);
        builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface1, int i)
            {
                Intent intent = new Intent(getApplicationContext(),MemberEligibityCheckActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog1 = builder1.create();
        alertDialog1.show();
    }

    private void setLoanProductDetailsAdapter(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);

            id = jsonObject.getString("id");
            minInterestRate = jsonObject.optDouble("minInterestRate");
            maxLoanAmount = jsonObject.optDouble("maxLoanAmount");
            maxInterestRate = jsonObject.optDouble("maxInterestRate");
            minLoanAmount = jsonObject.optDouble("minLoanAmount");


            JSONArray installPeriodList = jsonObject.getJSONArray("installmentPeriodList");
            String[] installmentPeriodArray = new String[installPeriodList.length()];
            for(int i = 0; i < installPeriodList.length(); i++)
                installmentPeriodArray[i] = installPeriodList.getString(i);


            inProgressLoanCreditData = jsonObject.getBoolean("inProgressLoanCreditData");

            //---In case of Waiting for Eligibility Checking---//
            if(inProgressLoanCreditData)
            {
                approvedAmountField.setHint(minLoanAmount + " - " + maxLoanAmount);
                approvedInterestRateField.setHint(minInterestRate + " - " + maxInterestRate);
            }
            //---End---//

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEligibityDetailsActivity.this, simple_spinner_item,installmentPeriodArray);
            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            periodListSpinner.setAdapter(adapter);
            periodListSpinner.setSelection(((ArrayAdapter<String>)periodListSpinner.getAdapter()).getPosition(String.valueOf(approvedInstallmentPeriod)));

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MemberEligibityDetailsActivity.this, simple_spinner_item,installmentPeriodArray);
            adapter2.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            periodListSpinnerBM.setAdapter(adapter2);
            periodListSpinnerBM.setSelection(((ArrayAdapter<String>)periodListSpinnerBM.getAdapter()).getPosition(String.valueOf(approvedInstallmentPeriodBM)));

            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(MemberEligibityDetailsActivity.this, simple_spinner_item,installmentPeriodArray);
            adapter3.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            periodListSpinnerCM.setAdapter(adapter3);
            periodListSpinnerCM.setSelection(((ArrayAdapter<String>)periodListSpinnerCM.getAdapter()).getPosition(String.valueOf(approvedInstallmentPeriodCM)));

            ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(MemberEligibityDetailsActivity.this, simple_spinner_item,installmentPeriodArray);
            adapter4.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            periodListSpinnerHO.setAdapter(adapter3);
            periodListSpinnerHO.setSelection(((ArrayAdapter<String>)periodListSpinnerHO.getAdapter()).getPosition(String.valueOf(approvedInstallmentPeriodHO)));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
//        //---End of data binding for Credit Summary List table---//

    private void populateMemberEligibityDetails (String memberId)
    {
        new ApiHandler.GetAsync(MemberEligibityDetailsActivity.this).execute("getCreditData/{" + memberId + "}/{" + loanProductId + "}");
    }

    private void populateCreditSummary (String memberId)
    {
        new ApiHandler.GetAsync(MemberEligibityDetailsActivity.this).execute("getCreditDataChkDate/{" + memberId + "}");
    }

    private void populateAppliedAmount(String memberId,String loanProductId)
    {
        new ApiHandler.GetAsync(MemberEligibityDetailsActivity.this).execute("getLoanProductDtlByProductId/{"+ memberId +"}/{"+ loanProductId + "}");
    }
}

//---Ended by Debmalya---//
