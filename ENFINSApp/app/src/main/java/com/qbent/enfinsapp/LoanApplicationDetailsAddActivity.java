package com.qbent.enfinsapp;

//---Developed by Debmalya---//
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qbent.enfinsapp.adapter.AutoCompleteAdapter;
import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.global.GlobalImageSettings;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Bank;
import com.qbent.enfinsapp.model.Borrower;
import com.qbent.enfinsapp.model.CoBorrower;
import com.qbent.enfinsapp.model.CollPoint;
import com.qbent.enfinsapp.model.ImageLoader;
import com.qbent.enfinsapp.model.LoanProduct;
import com.qbent.enfinsapp.model.LoanPurpose;
import com.qbent.enfinsapp.model.LoanSubPurpose;
import com.qbent.enfinsapp.model.UploadPassbook;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class LoanApplicationDetailsAddActivity extends MainActivity implements ApiCallback
{
    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    Boolean editCollectionPoint = false;
    Boolean editLoanProduct = false;
    Boolean editLoanPurpose = false;
    Boolean editLoanSubPurpose = false;
    Boolean editBank = false;
    Boolean editRelation = false;

    ImageView ivImage;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    private AutoCompleteAdapter autoCompleteAdapter;
    private  ArrayAdapter<CharSequence> relationArrayAdapter;
    private  ArrayAdapter<CharSequence> povertyLevelArrayAdapter;

    Spinner collectionDaysSpinner;
    Spinner collectionPointNameByDaySpinner;
    //    Spinner collectionPointNameSpinner;
    Spinner loanProductSpinner;
    Spinner relationshipSpinner;
    Spinner loanPurposeSpinner;
    Spinner loanSubPurposeSpinner;
    Spinner bankNameSpinner;
    Spinner povertyLevelSpinner;

    LinearLayout firstLinearLayout,secondLinearLayout,thirdLinearLayout,firstLinearLayoutForm,secondLinearLayoutForm,thirdLinearLayoutForm;
    TextView firstLinearLayoutText,secondLinearLayoutText,thirdLinearLayoutText;
    Boolean flagFirst = false;
    Boolean flagSecond = false;
    Boolean flagThird = false;
    Boolean flag = false;

    private List<CollPoint> collectionPointNameByDayList;
    private List<Borrower> borrowerList;
    private List<CoBorrower> coBorrowerList;
    private List<LoanProduct> loanProductList;
    private List<LoanPurpose> loanPurposeList;
    private List<LoanSubPurpose> loanSubPurposeList;
    private List<Bank> bankList;
    private List<String> borrowerName;
    private List<Integer> currentEmi;
    private List<String> coBorrowerName;

    private Bitmap bitmap;
    private Uri selectedImageUri;

    HashMap<String, String> spinnerCollectionPointNameByDayMap = new HashMap<String, String>();
    HashMap<String, String> spinnerLoanProductMap = new HashMap<String, String>();
    HashMap<String, String> spinnerLoanPurposeMap = new HashMap<String, String>();
    HashMap<String, String> spinnerLoanSubPurposeMap = new HashMap<String, String>();
    HashMap<String, String> spinnerBankMap = new HashMap<String, String>();
    HashMap<String, String> borrowerMap = new HashMap<String, String>();


    EditText applicationNumberField;
    EditText borrowerNameField;
    EditText coBorrowerNameField;
    EditText loanApprocedAmountField;
    EditText loanApprovedInterestField;
    EditText branchNameField;
    EditText bankAccountNumField;
    EditText bankIfscCodeField;
    EditText bankPassbookNameField;
    EditText lastTransField;
    EditText bplCardNoField;
    EditText loanApprovedInstallmentPeriodField;
    EditText monthlyIncomeField;
    EditText monthlyExpenseField;
    EditText monthlyExcessField;
    EditText annualExcessField;
    EditText currentEmiField;
    EditText propEmiField;
    EditText remarksField;

    TextView loanApplicationDetailsHeader;

    private AppCompatAutoCompleteTextView autoCompleteBorrowerName;
    private AppCompatAutoCompleteTextView autoCompleteCoBorrowerName;


    private int collectionDayIndex = 0;
    private int eventSeq = 1;

    private String collectionPointNameByDayId = " ";
    private String loanEditId = " ";
    private String loanProductId = " ";
    private String loanPurposeId = " ";
    private String loanSubPurposeId = " ";
    private String livingPlace = null;
    private String loanOfficerId = " ";
    private String collectionPointId = " ";
    private int approvedAmount;
    private Double approvedInterests;
    private int approvedInstallmentPeriode;
    private String search = " ";
    private String processAddEdit = " ";
    private String borrowerId = " ";
    private String eventId = " ";
    private String relationship = " ";
    private String borrowerAddressId = " ";
    private String borCoBorRelation = " ";
    private String coBorrowerId = " ";
    private String coBorrowerAddressId = " ";
    private String memberEligibilityApprovedId;
    private Handler handler;
    private Handler coHandler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private AutoCompleteAdapter adapter;
    private AutoCompleteAdapter coAdapter;
    private UploadPassbook uploadPassbook;

    private String user_collection,user_loan_product,user_loan_purpose,user_loan_sub_purpose,user_bank,user_id;

    private String appNumber,collcPointName,borroName,coborroName,relation,relationName,povertyLevel,bankBranchName,tranDate,ifCode,passBookName,
            accNumber,bplNumber,remarks;

    Image uploadImage;

    private int apprAmount,appInstPeriod,collcDay,monthIncome,monthExp,monthExc,annExc,currEmi,eMi;

    private int monthIncome1,monthExp1,monthExc1,annExc1,currEmi1,eMi1;
    private Double apprInterest;

    String saveFileName,json,image;

    ImageButton passBookButton,tickImageButton;
    Button saveLA,cancelLA,uploadPassBook,submitLA;

    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_loan_application_details_add);
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_loan_application_details_add_normal, null, false);
            drawer.addView(contentView, 0);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_loan_application_details_add, null, false);
            drawer.addView(contentView, 0);
        }

        final AlertDialogue alertDialogue = new AlertDialogue(LoanApplicationDetailsAddActivity.this);

        loanEditId = getIntent().getStringExtra("loan_edit_id");

        saveLA = (Button) findViewById(R.id.saveLoanApplicationButton);
        submitLA = (Button) findViewById(R.id.submitLoanApplicationButton);
        cancelLA = (Button) findViewById(R.id.cancelLoanApplicationButton);
        uploadPassBook =(Button) findViewById(R.id.uploadLoanApplicationButton);

        if(loanEditId != null)
        {
            populateLoanApplicatiuonsEditData(loanEditId);
        }

        populateLoanProduct();
        populateLoanPurpose();
        populateBank();



        Drawable image1 = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        saveLA.setBackground(rippledBg);

        Drawable image2 = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image2, null);
        uploadPassBook.setBackground(rippledBg1);

        Drawable image3 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg2 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image3, null);
        cancelLA.setBackground(rippledBg2);

        loanApplicationDetailsHeader = (TextView) findViewById(R.id.loanApplicationHeaderId);
        if(loanEditId != null)
        {
            loanApplicationDetailsHeader.setText("Edit Loan Applications");
        }
        else
        {
            submitLA.setVisibility(View.GONE);
        }



//        collectionPointNameSpinner = (Spinner) findViewById(R.id.collectionPointNameSpinnerId);
        applicationNumberField = (EditText) findViewById(R.id.applicationNumberId);
        applicationNumberField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        loanApprocedAmountField = (EditText) findViewById(R.id.loanApprovedAmountId);
        loanApprocedAmountField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        loanApprovedInterestField = (EditText) findViewById(R.id.approvedInterestId);
        loanApprovedInterestField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        loanApprovedInstallmentPeriodField = (EditText) findViewById(R.id.loanApprovedInstallmentPeriodId);
        loanApprovedInstallmentPeriodField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        collectionDaysSpinner = (Spinner) findViewById(R.id.collectionDaySpinnerId);
        collectionPointNameByDaySpinner = (Spinner) findViewById(R.id.collectionPointNameSpinnerId);
        loanProductSpinner = (Spinner) findViewById(R.id.loanProductSpinnerId);

        borrowerNameField = (EditText) findViewById(R.id.borrowerNameId);
        borrowerNameField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        coBorrowerNameField = (EditText) findViewById(R.id.coBorrowerNameId);
        coBorrowerNameField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        relationshipSpinner = (Spinner) findViewById(R.id.relationSpinnerId);
        loanPurposeSpinner = (Spinner) findViewById(R.id.loanPurposeSpinnerId);
        loanSubPurposeSpinner = (Spinner) findViewById(R.id.loanSubPurposeSpinnerId);
        bankNameSpinner = (Spinner) findViewById(R.id.bankNameId);

        branchNameField = (EditText) findViewById(R.id.branchNameId);
        branchNameField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        branchNameField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        lastTransField = (EditText) findViewById(R.id.transDateId);
        lastTransField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(false);
                return false;
            }
        });

        bankIfscCodeField = (EditText) findViewById(R.id.ifscCodeId);
        bankIfscCodeField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        bankIfscCodeField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        bankPassbookNameField = (EditText) findViewById(R.id.passbookNameId);
        bankPassbookNameField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        bankPassbookNameField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        bankAccountNumField = (EditText) findViewById(R.id.accountNumberId);
        bankAccountNumField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        ivImage = (ImageView) findViewById(R.id.passBookImage);

        monthlyIncomeField = (EditText) findViewById(R.id.monthlyIncomeId);
        monthlyIncomeField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        monthlyExpenseField = (EditText) findViewById(R.id.monthlyExpenseId);
        monthlyExpenseField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        monthlyExcessField = (EditText) findViewById(R.id.monthlyExcessId);
        monthlyExcessField.setEnabled(false);
        monthlyExcessField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        annualExcessField = (EditText) findViewById(R.id.annualExcessId);
        annualExcessField.setEnabled(false);
        annualExcessField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        currentEmiField = (EditText) findViewById(R.id.currentEmiId);
        currentEmiField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        propEmiField = (EditText) findViewById(R.id.proposedEmiId);
        propEmiField.setEnabled(false);
        propEmiField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        povertyLevelSpinner = (Spinner) findViewById(R.id.povertyLevelId);

        bplCardNoField = (EditText) findViewById(R.id.bplCardId);
        bplCardNoField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        remarksField = (EditText) findViewById(R.id.remarksId);
        remarksField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        tickImageButton = (ImageButton) findViewById(R.id.interestedImageId2);

        firstLinearLayout = (LinearLayout) findViewById(R.id.loanFirstLayout);
        secondLinearLayout = (LinearLayout) findViewById(R.id.loanSecondLayout);
        thirdLinearLayout = (LinearLayout) findViewById(R.id.loanThirdLayout);

        firstLinearLayoutForm = (LinearLayout) findViewById(R.id.loanFirstLayoutForm);
        secondLinearLayoutForm = (LinearLayout) findViewById(R.id.loanSecondLayoutForm);
        thirdLinearLayoutForm = (LinearLayout) findViewById(R.id.loanThirdLayoutForm);

        firstLinearLayoutText = (TextView) findViewById(R.id.loanFirstLayoutText);
        secondLinearLayoutText = (TextView) findViewById(R.id.loanSecondLayoutText);
        thirdLinearLayoutText = (TextView) findViewById(R.id.loanThirdLayoutText);


        monthlyExcessField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                editableRemarksField();
            }
        });
        //---Text watcher of monthly income---//
        monthlyIncomeField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                //saveActivityByText();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                saveActivityByText();
            }
        });
        //---End of text watcher---//

        //---Text watcher of monthly expense---//
        monthlyExpenseField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                //saveActivityByText();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                saveActivityByText();
            }
        });
        //---End of text watcher---//

        //---Text watcher of current EMI---//
        currentEmiField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                //saveActivityByText();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                saveActivityByText();
            }
        });

        propEmiField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                //saveActivityByText();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                saveActivityByText();
            }
        });
        //---End of text watcher---//

        //---Transaction Date---//
        lastTransField.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        LoanApplicationDetailsAddActivity.this,
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
                lastTransField.setText(date);
            }
        };
        //---End of transaction date---//

//        uploadPassBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ApiRequest apiRequest = new ApiRequest("uploadPassbookFile");
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.accumulate("image",json);
//                    jsonObject.accumulate("fileName",image);
//                    apiRequest.set_t(jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                new ApiHandler.PostAsync(LoanApplicationDetailsAddActivity.this).execute(apiRequest);
//            }
//        });

        tickImageButton.setVisibility(View.GONE);
        uploadPassBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(json==null)
                {
                    alertDialogue.showAlertMessage("Kindly select passbook");
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoanApplicationDetailsAddActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to upload this image?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        ApiRequest apiRequest = new ApiRequest("uploadPassbookFile");
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.accumulate("image",json);
                            jsonObject.accumulate("fileName",image);
                            apiRequest.set_t(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new ApiHandler.PostAsync(LoanApplicationDetailsAddActivity.this).execute(apiRequest);
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

        cancelLA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent memberIntent = new Intent(getApplicationContext(),LoanApplicationActivity.class);
                startActivity(memberIntent);
            }
        });



        passBookButton = (ImageButton) findViewById(R.id.passBookUpload);
        passBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SelectImage();
            }
        });


        collectionPointNameByDayList = new ArrayList<CollPoint>();
        borrowerList = new ArrayList<Borrower>();
        borrowerName = new ArrayList<String>();
        loanProductList = new ArrayList<LoanProduct>();
        loanPurposeList = new ArrayList<LoanPurpose>();
        loanSubPurposeList = new ArrayList<LoanSubPurpose>();
        bankList = new ArrayList<Bank>();

        fab.setVisibility(View.GONE);

        //---Collection day spinner---//
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.collection_days, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        collectionDaysSpinner.setAdapter(arrayAdapter);
        //---End of collection day spinner---//

        firstLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagFirst){
                    // means true
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                }
                else{
                    slideUpFirstLinearLayoutForm();
                }

            }
        });

        secondLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagSecond){
                    // means true
                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                }
                else{
                    slideUpSecondLinearLayoutForm();
                }

            }
        });
        thirdLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagThird){
                    // means true
                    slideDown(thirdLinearLayoutForm);
                    thirdLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagThird = false;
                }
                else{
                    slideUpThirdLinearLayoutForm();
                }

            }
        });

        //---AutoComplete of Borrower code---//
        autoCompleteBorrowerName = (AppCompatAutoCompleteTextView) findViewById (R.id.borrowerNameId);
        int layout = android.R.layout.simple_list_item_1;
        adapter = new AutoCompleteAdapter (this, android.R.layout.simple_dropdown_item_1line);
        //coAdapter = new AutoCompleteAdapter (this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteBorrowerName.setThreshold(3);
        autoCompleteBorrowerName.setAdapter (adapter);
        autoCompleteBorrowerName.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id)
                    {
                        borrowerNameField.setText(adapter.getObject(position));
                        Borrower borrower = null;

                        for (int i = 0; i < borrowerList.size(); i++)
                        {
                            if(borrowerList.get(i).getFullName() == adapter.getObject(position))
                            {
                                borrower = borrowerList.get(i);
                            }

                        }
                        String name = adapter.getObject(position);

                        borrowerId = borrower.getId();
                        borrowerAddressId = borrower.getBorrowerAddressId();
                        memberEligibilityApprovedId = borrower.getMemberEligibilityApprovedId();
                        approvedAmount = borrower.getApprovedAmount();
                        approvedInterests = borrower.getApprovedInterest();
                        approvedInstallmentPeriode = borrower.getApprovedInsPeriod();

                        loanApprocedAmountField.setText(approvedAmount + " ");
                        loanApprovedInterestField.setText(approvedInterests + " ");
                        loanApprovedInstallmentPeriodField.setText(String.valueOf(approvedInstallmentPeriode));

                        //---Generates current EMI method call---//
                        if(borrowerId != null && !borrowerId.equals(" "))
                        {
                            populateCurrentEmi(loanProductId,approvedAmount,approvedInstallmentPeriode);
                        }
                        //---End of method call---//

                    }
                });

        autoCompleteBorrowerName.addTextChangedListener(new TextWatcher()
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
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteBorrowerName.getText()) && !collectionPointId.equals(emptyGuid) && !loanProductId.equals(emptyGuid) &&!collectionPointId.equals(" ") && !loanProductId.equals(" "))
                    {
//                        String test = String.valueOf(autoCompleteBorrowerName.getThreshold());
//                        String test2 = autoCompleteBorrowerName.getText().toString().isEmpty()?"0":autoCompleteBorrowerName.getText().toString();
//                        if(test2.length()>=3)
//                        {
                            new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("getBorrowerBySearchText?"+ "searchText=" + autoCompleteBorrowerName.getText().toString().substring(0,2) + "&cp=" + collectionPointId + "&lp=" + loanProductId + "&proAorE=" +"A");
//                        }
//                        else
//                        {
//                            alertDialogue.showAlertMessage("No Borrower has found");
//                        }


                    }
//                    else
//                    {
////                        String test = String.valueOf(autoCompleteBorrowerName.getThreshold());
////                        String test2 = autoCompleteBorrowerName.getText().toString().isEmpty()?"0":autoCompleteBorrowerName.getText().toString();
////                        if(test2.length()>=3 && (loanEditId == null || (flag ==true && loanEditId != null)))
////                        {
//                            alertDialogue.showAlertMessage("Please select Collection Point and Loan Product");
////                        }
//                    }


                }
                return false;
            }
        });

        //---Autocomplete of Co-borrower---//
        autoCompleteCoBorrowerName = (AppCompatAutoCompleteTextView) findViewById (R.id.coBorrowerNameId);
        coAdapter = new AutoCompleteAdapter (this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteCoBorrowerName.setThreshold(3);
        autoCompleteCoBorrowerName.setAdapter (coAdapter);
        autoCompleteCoBorrowerName.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        coBorrowerNameField.setText(coAdapter.getObject(position));
                        CoBorrower coBorrower = null;

                        for (int i = 0; i < coBorrowerList.size(); i++)
                        {
                            if(coBorrowerList.get(i).getFullName() == coAdapter.getObject(position))
                            {
                                coBorrower = coBorrowerList.get(i);
                            }

                        }
                        String name = coAdapter.getObject(position);
                        coBorrowerId = coBorrower.getId();
                        coBorrowerAddressId = coBorrower.getCoBorrowerAddressId();
                        //memberEligibilityApprovedId = coBorrower.getMemberEligibilityApprovedId();
                    }
                });

        autoCompleteCoBorrowerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                coHandler.removeMessages(TRIGGER_AUTO_COMPLETE);
                coHandler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        coHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteCoBorrowerName.getText()) && !collectionPointId.equals(emptyGuid) && !loanProductId.equals(emptyGuid) &&!collectionPointId.equals(" ") && !loanProductId.equals(" "))
                    {
//                        String test2 = autoCompleteCoBorrowerName.getText().toString().isEmpty()?"0":autoCompleteCoBorrowerName.getText().toString();
//                        if(test2.length()>=3)
//                        {
//                            if(borrowerId.equals(" "))
//                            {
//                                alertDialogue.showAlertMessage("Kindly enter Borrower Name");
//                            }
//                            else
//                            {
                                new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("getCoBorrowerBySearchText?"+ "searchText=" + autoCompleteCoBorrowerName.getText().toString().substring(0,2) + "&bId=" + borrowerId + "&cp=" + collectionPointId + "&lp=" + loanProductId);
//                            }
//
//                        }
//                        else
//                        {
//                            alertDialogue.showAlertMessage("No Co-Borrower has found");
//                        }

                    }
//                    else
//                    {
////                        String test = String.valueOf(autoCompleteBorrowerName.getThreshold());
////                        String test2 = autoCompleteCoBorrowerName.getText().toString().isEmpty()?"0":autoCompleteCoBorrowerName.getText().toString();
////                        if(test2.length()>=3 && (loanEditId == null || (flag ==true && loanEditId != null)))
////                        {
//                            alertDialogue.showAlertMessage("Please select Collection Point and Loan Product");
////                        }
//                    }
                }
                return false;
            }
        });
        //---End of AutoComplete Co-borrower code---//

        //---Collection day spinner---//



        collectionDaysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
//                String name = collectionDaysSpinner.getSelectedItem().toString();
                collectionDayIndex = collectionDaysSpinner.getSelectedItemPosition();
                populateCollectionPoint(collectionDayIndex);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });
        //---End of Collection Day Spinner---//

        //---Collection point name by day spinner---//
        collectionPointNameByDaySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    flag = true;
                }
                return false;
            }
        });

        collectionPointNameByDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                if( user_collection!=null && !user_collection.equals("null")&& !user_collection.equals(emptyGuid)&& !collectionPointNameByDayId.equals(emptyGuid) && editCollectionPoint==false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerCollectionPointNameByDayMap.keySet());
                    int test = indexes1.indexOf(user_collection);
                    String test2 = (new ArrayList<String>(spinnerCollectionPointNameByDayMap.values())).get(test);
                    collectionPointNameByDaySpinner.setSelection(((ArrayAdapter<String>)collectionPointNameByDaySpinner.getAdapter()).getPosition(test2));
                    collectionPointId = user_collection;
                    if(flag == true)
                    {
                        borrowerNameField.setText("");
                        coBorrowerNameField.setText("");
                        borrowerId = " ";
                        coBorrowerId = " ";
                    }


                }
                else
                {
                    String name = collectionPointNameByDaySpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerCollectionPointNameByDayMap.values());
                    int a = indexes.indexOf(name);
                    collectionPointId = (new ArrayList<String>(spinnerCollectionPointNameByDayMap.keySet())).get(indexes.indexOf(name));
                    borrowerNameField.setText("");
                    coBorrowerNameField.setText("");
                    borrowerId = " ";
                    coBorrowerId = " ";
                }
//                borrowerId = " ";
//                coBorrowerId = " ";

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//
            }

        });
        collectionPointNameByDaySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editCollectionPoint = true;
                }
                return false;
            }
        });
        //---End of Collection point name by day spinner---//

        //---Loan Product Spinner---//
        loanProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                if( user_loan_product!=null && !user_loan_product.equals("null")&& !user_loan_product.equals(emptyGuid)&& !loanProductId.equals(emptyGuid) && editLoanProduct==false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerLoanProductMap.keySet());
                    int test = indexes1.indexOf(user_loan_product);
                    String test2 = (new ArrayList<String>(spinnerLoanProductMap.values())).get(test);
                    loanProductSpinner.setSelection(((ArrayAdapter<String>)loanProductSpinner.getAdapter()).getPosition(test2));
                    loanProductId = user_loan_product;
                }
                else
                {
                    String name = loanProductSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerLoanProductMap.values());
                    int a = indexes.indexOf(name);
                    loanProductId = (new ArrayList<String>(spinnerLoanProductMap.keySet())).get(indexes.indexOf(name));
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });
        loanProductSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editLoanProduct = true;
                }
                return false;
            }
        });
        //---End of Loan Product Spinner---//

        //---Loan Purpose Spinner---//
        loanPurposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_loan_purpose!=null && !user_loan_purpose.equals("null")&& !user_loan_purpose.equals(emptyGuid) && !loanPurposeId.equals(emptyGuid) && editLoanPurpose == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerLoanPurposeMap.keySet());
                    int test = indexes1.indexOf(user_loan_purpose);
                    String test2 = (new ArrayList<String>(spinnerLoanPurposeMap.values())).get(test);
                    loanPurposeSpinner.setSelection(((ArrayAdapter<String>)loanPurposeSpinner.getAdapter()).getPosition(test2));
                    loanSubPurposeId = user_loan_purpose;
                }
                else
                {
                    String name = loanPurposeSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerLoanPurposeMap.values());
                    int a = indexes.indexOf(name);
                    loanSubPurposeId = (new ArrayList<String>(spinnerLoanPurposeMap.keySet())).get(indexes.indexOf(name));
                }
                populateLoanSubPurpose(loanSubPurposeId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });
        loanPurposeSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editLoanPurpose = true;
                }
                return false;
            }
        });
        //---End of Loan Purpose Spinner---//

        //---Loan Sub-purpose Spinner---//
        loanSubPurposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_loan_sub_purpose!=null && !user_loan_sub_purpose.equals("null")&& !user_loan_sub_purpose.equals(emptyGuid) && !loanSubPurposeId.equals(emptyGuid) && editLoanSubPurpose == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerLoanSubPurposeMap.keySet());
                    int test = indexes1.indexOf(user_loan_sub_purpose);
                    String test2 = (new ArrayList<String>(spinnerLoanSubPurposeMap.values())).get(test);
                    loanSubPurposeSpinner.setSelection(((ArrayAdapter<String>)loanSubPurposeSpinner.getAdapter()).getPosition(test2));
                    loanSubPurposeId = user_loan_sub_purpose;
                }
                else
                {
                    String name = loanSubPurposeSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerLoanPurposeMap.values());
                    int a = indexes.indexOf(name);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });
        //---End of Loan Sub-purpose Spinner---//

        //---Bank Name Spinner---//
        bankNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_bank!=null && !user_bank.equals("null")&& !user_bank.equals(emptyGuid) &&  editBank == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerBankMap.keySet());
                    int test = indexes1.indexOf(user_bank);
                    String test2 = (new ArrayList<String>(spinnerBankMap.values())).get(test);
                    bankNameSpinner.setSelection(((ArrayAdapter<String>)bankNameSpinner.getAdapter()).getPosition(test2));

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });
        bankNameSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editBank = true;
                }
                return false;
            }
        });
        //---End of Bank Name Spinner---//

        //---Relationship Spinner---//
        relationArrayAdapter = ArrayAdapter.createFromResource(this, R.array.relationship, android.R.layout.simple_spinner_item);
        relationArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        relationshipSpinner.setAdapter(relationArrayAdapter);

        relationshipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(relationName!=null && !relationName.equals("null")&& !relationName.equals(emptyGuid) && !borCoBorRelation.equals(emptyGuid))
                {
                    relationName = relationshipSpinner.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        //---End of Relationship Spinner---//

        //---Poverty level Spinner---//
        povertyLevelArrayAdapter = ArrayAdapter.createFromResource(this, R.array.poverty_level, android.R.layout.simple_spinner_item);
        povertyLevelArrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        povertyLevelSpinner.setPrompt("Select Poverty Level");
        povertyLevelSpinner.setAdapter(povertyLevelArrayAdapter);

        povertyLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                //String name = povertyLevelSpinner.getSelectedItem().toString();
                povertyLevel = povertyLevelSpinner.getSelectedItem().toString();
                if(povertyLevel.contains("APL"))
                {
                    bplCardNoField.setVisibility(View.GONE);
                }
                else
                {
                    bplCardNoField.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //---End of Poverty level Spinner---//



        applicationNumberField.setEnabled(false);
        loanApprocedAmountField.setEnabled(false);
        loanApprovedInterestField.setEnabled(false);
        loanApprovedInstallmentPeriodField.setEnabled(false);

        //---Save loan applications method---//
        saveLA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
//                if(applicationNumberField.getText().toString().length() == 0)
//                {
//                    firstLinearLayoutForm.setVisibility(View.VISIBLE);
//                    slideUp(firstLinearLayoutForm);
//                    applicationNumberField.setError("Please put an application number");
//                    applicationNumberField.requestFocus();
//                    applicationNumberField.performClick();
//                    return;
//                }
//
//                if(loanApprocedAmountField.getText().toString().length() == 0)
//                {
//                    firstLinearLayoutForm.setVisibility(View.VISIBLE);
//                    slideUp(firstLinearLayoutForm);
//                    loanApprocedAmountField.setError("Please put a loan approved amount");
//                    loanApprocedAmountField.requestFocus();
//                    loanApprocedAmountField.performClick();
//                    return;
//                }
//                if(loanApprovedInterestField.getText().toString().length() == 0)
//                {
//                    firstLinearLayoutForm.setVisibility(View.VISIBLE);
//                    slideUp(firstLinearLayoutForm);
//                    loanApprovedInterestField.setError("Please put a loan interest");
//                    loanApprovedInterestField.requestFocus();
//                    loanApprovedInterestField.performClick();
//                    return;
//                }
//                if(loanApprovedInstallmentPeriodField.getText().toString().length() == 0)
//                {
//                    firstLinearLayoutForm.setVisibility(View.VISIBLE);
//                    slideUp(firstLinearLayoutForm);
//                    loanApprovedInstallmentPeriodField.setError("Please put an instalment period");
//                    loanApprovedInstallmentPeriodField.requestFocus();
//                    loanApprovedInstallmentPeriodField.performClick();
//                    return;
//                }

                if(collectionDaysSpinner.getSelectedItemPosition() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select a collection day");
                    setSpinnerError(collectionDaysSpinner,"Please select a collection day");
                    collectionDaysSpinner.setFocusable(true);
                    collectionDaysSpinner.requestFocus();
                    return;
                }
                if((new ArrayList<String>(spinnerCollectionPointNameByDayMap.keySet())).get(new ArrayList<String>(spinnerCollectionPointNameByDayMap.values())
                        .indexOf(collectionPointNameByDaySpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {

                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select a collection point name");
                    setSpinnerError(collectionPointNameByDaySpinner,"Select Collection Point Name");
                    collectionPointNameByDaySpinner.setFocusable(true);
                    collectionPointNameByDaySpinner.requestFocus();
                    return;
                }

                if((new ArrayList<String>(spinnerLoanProductMap.keySet())).get(new ArrayList<String>(spinnerLoanProductMap.values())
                        .indexOf(loanProductSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select a loan product");
                    setSpinnerError(loanProductSpinner,"Please select a loan product");
                    loanProductSpinner.setFocusable(true);
                    loanProductSpinner.requestFocus();
                    return;
                }
                if(borrowerNameField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    borrowerNameField.setError("Please enter a borrower name");
                    borrowerNameField.requestFocus();
                    borrowerNameField.performClick();
                    return;
                }
                if(coBorrowerNameField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    coBorrowerNameField.setError("Please enter a co-borrower name");
                    coBorrowerNameField.requestFocus();
                    coBorrowerNameField.performClick();
                    return;
                }
//                if(relationshipSpinner.getSelectedItemPosition() == 0)
//                {
//                    slideUpFirstLinearLayoutForm();
//                    alertDialogue.showAlertMessage("Please select a relation");
//                    setSpinnerError(relationshipSpinner,"Please select a relation");
//                    relationshipSpinner.setFocusable(true);
//                    relationshipSpinner.requestFocus();
//                    return;
//                }

                if((new ArrayList<String>(spinnerLoanPurposeMap.keySet())).get(new ArrayList<String>(spinnerLoanPurposeMap.values())
                        .indexOf(loanPurposeSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select a loan purpose");
                    setSpinnerError(loanPurposeSpinner,"Please select a loan purpose");
                    loanPurposeSpinner.setFocusable(true);
                    loanPurposeSpinner.requestFocus();
                    return;
                }

                if((new ArrayList<String>(spinnerLoanSubPurposeMap.keySet())).get(new ArrayList<String>(spinnerLoanSubPurposeMap.values())
                        .indexOf(loanSubPurposeSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select a loan sub-purpose");
                    setSpinnerError(loanSubPurposeSpinner,"Please select a loan sub-purpose");
                    loanSubPurposeSpinner.setFocusable(true);
                    loanSubPurposeSpinner.requestFocus();
                    return;
                }

                if((new ArrayList<String>(spinnerBankMap.keySet())).get(new ArrayList<String>(spinnerBankMap.values())
                        .indexOf(bankNameSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {
                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    alertDialogue.showAlertMessage("Please select a bank name");
                    setSpinnerError(bankNameSpinner,"Please select a bank name");
                    bankNameSpinner.setFocusable(true);
                    bankNameSpinner.requestFocus();
                    return;
                }

                if(branchNameField.getText().toString().length() == 0)
                {
                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    branchNameField.setError("Please enter a branch name");
                    branchNameField.requestFocus();
                    branchNameField.performClick();
                    return;
                }
                if(lastTransField.getText().toString().length() == 0)
                {
                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    alertDialogue.showAlertMessage("Please select a date");
                    lastTransField.requestFocus();
                    lastTransField.performClick();
                    return;
                }
                if(bankIfscCodeField.getText().toString().length() == 0)
                {
                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    bankIfscCodeField.setError("Please put a valid IFSC Code");
                    bankIfscCodeField.requestFocus();
                    bankIfscCodeField.performClick();
                    return;
                }
                if(bankIfscCodeField.getText().toString().length() != 11)
                {
                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    bankIfscCodeField.setError("IFSC code is not valid");
                    bankIfscCodeField.requestFocus();
                    bankIfscCodeField.performClick();
                    return;
                }
                if(bankPassbookNameField.getText().toString().length() == 0)
                {
                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    bankPassbookNameField.setError("Please enter the name according to passbook");
                    bankPassbookNameField.requestFocus();
                    bankPassbookNameField.performClick();
                    return;
                }
                if(bankAccountNumField.getText().toString().length() == 0)
                {
                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    bankAccountNumField.setError("Please enter your's bank account number");
                    bankAccountNumField.requestFocus();
                    bankAccountNumField.performClick();
                    return;
                }
                if(saveFileName==null)
                {
                    alertDialogue.showAlertMessage("Kindly upload passbook");
                    return;
                }
                if(monthlyIncomeField.getText().toString().length() == 0)
                {
                    slideDown(thirdLinearLayoutForm);
                    thirdLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagThird = false;
                    monthlyIncomeField.setError("Please enter your's monthly income");
                    monthlyIncomeField.requestFocus();
                    monthlyIncomeField.performClick();
                    return;
                }
                if(monthlyExpenseField.getText().toString().length() == 0)
                {
                    slideDown(thirdLinearLayoutForm);
                    thirdLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagThird = false;
                    monthlyExpenseField.setError("Please enter your's monthly expenses");
                    monthlyExpenseField.requestFocus();
                    monthlyExpenseField.performClick();
                    return;
                }
                if(currentEmiField.getText().toString().length() == 0)
                {
                    slideDown(thirdLinearLayoutForm);
                    thirdLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagThird = false;
                    currentEmiField.setError("Please enter current EMI");
                    currentEmiField.requestFocus();
                    currentEmiField.performClick();
                    return;
                }
                if(remarksField.getText().toString().length() == 0 && monthExc<0)
                {
                    remarksField.requestFocus();
                    alertDialogue.showAlertMessage("Remarks can't be blank");
                    return;
                }
                if(povertyLevelSpinner.getSelectedItem().toString().equals("BPL") && bplCardNoField.getText().toString().length() == 0)
                {
                    bplCardNoField.requestFocus();
                    alertDialogue.showAlertMessage("BPL card number can not be empty");
                    return;
                }

                //---Loan application save POST call---//

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoanApplicationDetailsAddActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to save loan application?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k)
                    {
                        ApiRequest apiRequest = new ApiRequest("add-loanApplication");
                        try{
                            JSONObject jsonObject = new JSONObject();
                            if(loanEditId != null)
                            {
                                apiRequest = new ApiRequest("edit-loanApplication/{"+ loanEditId+"}");
                                jsonObject.accumulate("id",loanEditId);
                            }
                            //---save loan application---//
                            jsonObject.accumulate("borrowerId",borrowerId);
                            jsonObject.accumulate("borrowerAddressId",borrowerAddressId);
                            jsonObject.accumulate("coBorrowerId",coBorrowerId);
                            jsonObject.accumulate("coBorrowerAddressId",coBorrowerAddressId);
                            jsonObject.accumulate("memberEligibilityApprovedId",memberEligibilityApprovedId);
                            jsonObject.accumulate("borCoBorRelation",relationshipSpinner.getSelectedItem().toString());
                            jsonObject.accumulate("loanProductId",(spinnerLoanProductMap.size() != 0)?(new ArrayList<String>(spinnerLoanProductMap.keySet())).get(new ArrayList<String>(spinnerLoanProductMap.values()).indexOf(loanProductSpinner.getSelectedItem().toString())).toString():user_loan_product);
                            jsonObject.accumulate("loanPurposeId",(spinnerLoanPurposeMap.size() != 0)?(new ArrayList<String>(spinnerLoanPurposeMap.keySet())).get(new ArrayList<String>(spinnerLoanPurposeMap.values()).indexOf(loanPurposeSpinner.getSelectedItem().toString())).toString():user_loan_purpose);
                            jsonObject.accumulate("loanSubPurposeId",(spinnerLoanSubPurposeMap.size() != 0)?(new ArrayList<String>(spinnerLoanSubPurposeMap.keySet())).get(new ArrayList<String>(spinnerLoanSubPurposeMap.values()).indexOf(loanSubPurposeSpinner.getSelectedItem().toString())).toString():user_loan_sub_purpose);
                            jsonObject.accumulate("bankId",(spinnerBankMap.size() != 0)?(new ArrayList<String>(spinnerBankMap.keySet())).get(new ArrayList<String>(spinnerBankMap.values()).indexOf(bankNameSpinner.getSelectedItem().toString())).toString():user_bank);
                            jsonObject.accumulate("bankBranch",branchNameField.getText().toString());
                            jsonObject.accumulate("bankAccountNo",bankAccountNumField.getText().toString());
                            jsonObject.accumulate("bankIfscode",bankIfscCodeField.getText().toString());
                            jsonObject.accumulate("bankPassbookName",bankPassbookNameField.getText().toString());
                            jsonObject.accumulate("passbookScan",image);
                            jsonObject.accumulate("passbookFileName",saveFileName);
                            jsonObject.accumulate("lastBankTxnDate",lastTransField.getText().toString());
                            jsonObject.accumulate("livingPlace"," ");
                            jsonObject.accumulate("povertyLevel",povertyLevelSpinner.getSelectedItem().toString());
                            jsonObject.accumulate("bplcardNo",bplCardNoField.getText().toString());
                            jsonObject.accumulate("applicationAmount",loanApprocedAmountField.getText().toString());
                            jsonObject.accumulate("monthlyIncome",monthlyIncomeField.getText().toString());
                            jsonObject.accumulate("monthlyExpense",monthlyExpenseField.getText().toString());
                            jsonObject.accumulate("monthlySavings",monthlyExcessField.getText().toString());
                            jsonObject.accumulate("currentEmi",currentEmiField.getText().toString());
                            jsonObject.accumulate("remarks",remarksField.getText().toString());

                            apiRequest.set_t(jsonObject);

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        new ApiHandler.PostAsync(LoanApplicationDetailsAddActivity.this).execute(apiRequest);
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

                //---End of save loan application POST call---//
            }
        });
        //---End of save method---//

        //---Submit loan application---//
        submitLA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(loanEditId != null)
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoanApplicationDetailsAddActivity.this);
                    builder.setTitle("ENFIN Admin");
                    builder.setMessage("Are you sure to submit loan application?");
                    builder.setCancelable(true);
                    builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int k)
                        {
                            new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("submit-loanApplication/{"+ loanEditId +"}/{"+ eventId +"}/"+ eventSeq);
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
            }
        });
        //---End of submit---//

        //---Call loan application edit method---//

        //---End of method call---//



    }



    public void slideUpFirstLinearLayoutForm()
    {
        slideUp(firstLinearLayoutForm);

        firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);

        flagFirst = true;
    }
    public void slideUpSecondLinearLayoutForm()
    {
        slideUp(secondLinearLayoutForm);

        secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
        flagSecond = true;
    }
    public void slideUpThirdLinearLayoutForm()
    {
        slideUp(thirdLinearLayoutForm);

        thirdLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);

        flagThird = true;
    }

    private void editableRemarksField()
    {
        if(!monthlyExcessField.getText().toString().isEmpty())
        {
            if(Integer.parseInt(monthlyExcessField.getText().toString()) < 0)
            {
                remarksField.setVisibility(View.VISIBLE);
            }
            else
            {
                remarksField.setVisibility(View.GONE);
            }
        }
        else
        {
            remarksField.setVisibility(View.GONE);
        }

    }

    private void setSpinnerError(Spinner spinner, String error)
    {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
        }
    }

    //---API call of loan application edit---//
    private void populateLoanApplicatiuonsEditData(String loanEditId)
    {
        new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("getLoanApplicationById/{"+ loanEditId +"}");
    }
    //---End of API call---//


    //---Text watcher method---//
    private void saveActivityByText()
    {
        try
        {
            int monthlyExpense = (monthlyExpenseField.getText().toString().length()!=0)?Integer.valueOf(monthlyExpenseField.getText().toString()):0;
            int currentEmi = (currentEmiField.getText().toString().length()!=0)?Integer.valueOf(currentEmiField.getText().toString()):0;
            int proposedEmi = (propEmiField.getText().toString().length()!=0)?Integer.valueOf(propEmiField.getText().toString()):0;

            Integer totalExpenses = monthlyExpense+currentEmi+proposedEmi;
            //Integer totalExpense2 = Integer.valueOf(totalExpenses);
            Integer totalIncome = (monthlyIncomeField.getText().toString().length()!=0)?Integer.valueOf(monthlyIncomeField.getText().toString()):0;

            Integer monthlyExcess = totalIncome - totalExpenses;

            Integer finExcess = monthlyExcess;

            Integer totalAnnualExpense = finExcess * 12;

            if(monthlyIncomeField.getText().toString().length()!=0 && monthlyExpenseField.getText().toString().length()!=0
                    && currentEmiField.getText().toString().length()!=0)
            {
                monthlyExcessField.setText(String.valueOf(finExcess));
                annualExcessField.setText(String.valueOf(totalAnnualExpense));
            }
            else
            {
                monthlyExcessField.setText(null);
            }
        }
        catch (NumberFormatException nfe)
        {
            nfe.printStackTrace();
        }
    }
    //---End of method---//



    private void SelectImage()
    {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LoanApplicationDetailsAddActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(items[i].equals("Camera"))
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,REQUEST_CAMERA);
                }
                else if(items[i].equals("Gallery"))
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent,SELECT_FILE);
                }
                else if(items[i].equals("Cancel"))
                {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ivImage = (ImageView) findViewById(R.id.passBookImage);
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == REQUEST_CAMERA)
            {

                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
                byte[] b = baos.toByteArray();
                json = Base64.encodeToString(b, Base64.DEFAULT);
                ivImage.setImageBitmap(bitmap);
                Uri uri =  getImageUri(getApplicationContext(),bitmap);
                image = getFileName(uri);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//                selectedImageUri = getImageUri(getApplicationContext(), bitmap);


            }
            else if(requestCode == SELECT_FILE)
            {
                Uri selectedImageUri = data.getData();
                image = getFileName(selectedImageUri);
                ivImage.setImageURI(selectedImageUri);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
                byte[] b = baos.toByteArray();
                json = Base64.encodeToString(b, Base64.DEFAULT);
            }
        }
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    public void onApiRequestComplete(String key, final String result) throws IOException
    {
        if (key.contains("getCollectionpointByDay"))
        {
            setCollectionPointByNameAdapter(result);
        }
        else if(key.contains("getBorrowerBySearchText"))
        {
            setBorrowerNameAdapter(result);
        }
        else if(key.contains("getCoBorrowerBySearchText"))
        {
            setCoBorrowerNameAdapter(result);
        }
        else if (key.contains("all-loanProduct"))
        {
            setloanProductsAdapter(result);
        }
        else if (key.contains("all-loan-purpose"))
        {
            setloanPurposeAdapter(result);
        }
        else if (key.contains("allByLoanPurpose"))
        {
            setloanSubPurposeAdapter(result);
        }

        else if (key.contains("all-bank-list"))
        {
            setBankAdapter(result);
        }
        else if (key.contains("uploadPassbookFile"))
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(LoanApplicationDetailsAddActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("Image successfully uploaded");
            builder1.setCancelable(true);
            builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface1, int i)
                {
                    saveFileName = result.substring(1,result.length()-1);
                    tickImageButton.setVisibility(View.VISIBLE);
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
//            saveFileName = result.substring(1,result.length()-1);
        }
        else if (key.contains("getEmi"))
        {
            try {
                setCurrentEmiAdapter(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (key.contains("add-loanApplication") || key.contains("edit-loanApplication"))
        {
            saveLoanApplicationsAdapter(result);
        }
        else if(key.contains("getLoanApplicationById"))
        {
            try {
                setEditLoanApplicationsAdapter(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if(key.contains("submit-loanApplication") && !key.contains("add-loanApplication") && !key.contains("edit-loanApplication"))
        {
            submitLoanApplicationsAdapter(result);
        }

    }

    //---Set proposed EMI adapter---//
    private void setCurrentEmiAdapter(String result) throws JSONException
    {
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length()-1; i++)
            {
                JSONObject mJsonObject = jsonArray.getJSONObject(i);
                eMi = mJsonObject.getInt("totalInstallmentAmount");

            }
            propEmiField.setText(String.valueOf(eMi));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //---End of proposed EMI adapter---//

    //---Put values for edit---//
    private void setEditLoanApplicationsAdapter(String result) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            JSONObject jsonObject = new JSONObject(result);

            borrowerId = jsonObject.getString("borrowerId");
            borrowerAddressId = jsonObject.getString("borrowerAddressId");
            coBorrowerId = jsonObject.getString("coBorrowerId");
            coBorrowerAddressId = jsonObject.getString("coBorrowerAddressId");
            memberEligibilityApprovedId = jsonObject.getString("memberEligibilityApprovedId");
            appNumber = jsonObject.getString("applicationNo");
            apprAmount = jsonObject.getInt("approvedAmount");
            apprInterest = jsonObject.getDouble("approvedInterest");
            appInstPeriod = jsonObject.getInt("approvedInsPeriod");
            collcDay = jsonObject.getInt("collectionDay");
            user_collection = jsonObject.getString("collectionPointId");
            user_loan_product = jsonObject.getString("loanProductId");
            borroName = jsonObject.getString("borrowerName");
            coborroName = jsonObject.getString("coBorrowerName");
            eventId = jsonObject.getString("loanEventId");
            relationName = jsonObject.getString("borCoBorRelation");
            povertyLevel = jsonObject.getString("povertyLevel");
            user_loan_purpose = jsonObject.getString("loanPurposeId");
            user_loan_sub_purpose = jsonObject.getString("loanSubPurposeId");
            user_bank = jsonObject.getString("bankId");
            bankBranchName = jsonObject.getString("bankBranch");
            tranDate = jsonObject.getString("lastBankTxnDate");
            ifCode = jsonObject.getString("bankIfscode");
            passBookName = jsonObject.getString("bankPassbookName");
            accNumber = jsonObject.getString("bankAccountNo");
            image = jsonObject.getString("passbookScan");
            saveFileName = jsonObject.getString("passbookFileName");

            monthIncome = jsonObject.getJSONObject("loanIncExpDetailsLO").getInt("monthlyIncome");
            monthExp = jsonObject.getJSONObject("loanIncExpDetailsLO").getInt("monthlyExpense");
            monthExc = jsonObject.getJSONObject("loanIncExpDetailsLO").getInt("monthlySavings");
            currEmi = jsonObject.getJSONObject("loanIncExpDetailsLO").getInt("currentEmi");
            bplNumber = jsonObject.getString("bplcardNo");
            remarks = jsonObject.getJSONObject("loanIncExpDetailsLO").getString("remarks");

            String passbookFileName = jsonObject.getString("passbookFileName");
            int loader = R.drawable.dummy_upload_img;
            String image_url = GlobalImageSettings.API_BASE_URL.getKey() + passbookFileName;
            ImageLoader imgLoader = new ImageLoader(getApplicationContext());

            imgLoader.DisplayImage(image_url, loader, ivImage);

            populateCurrentEmi(jsonObject.getString("loanProductId"),apprAmount,appInstPeriod);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Date date = dateFormat.parse(tranDate);
        SimpleDateFormat finalTransDate = new SimpleDateFormat("MM-dd-yyyy");

        int test = collcDay+1;

        applicationNumberField.setText(appNumber);
        propEmiField.setText(String.valueOf(eMi));
        loanApprocedAmountField.setText(String.valueOf(apprAmount));
        loanApprovedInterestField.setText(String.valueOf(apprInterest));
        loanApprovedInstallmentPeriodField.setText(String.valueOf(appInstPeriod));
        collectionDaysSpinner.setSelection(collcDay);
        borrowerNameField.setText(borroName);
        coBorrowerNameField.setText(coborroName);
        relationshipSpinner.setSelection(relationArrayAdapter.getPosition(relationName));
        povertyLevelSpinner.setSelection(povertyLevelArrayAdapter.getPosition(povertyLevel));
        branchNameField.setText(bankBranchName);
        lastTransField.setText(finalTransDate.format(date));
        bankIfscCodeField.setText(ifCode);
        bankPassbookNameField.setText(passBookName);
        bankAccountNumField.setText(accNumber);
        ivImage.setImageURI(Uri.parse(image));
        ivImage.setImageURI(Uri.parse(saveFileName));
        monthlyIncomeField.setText(String.valueOf(monthIncome));
        monthlyExpenseField.setText(String.valueOf(monthExp));
        monthlyExcessField.setText(String.valueOf(monthExc));
        annualExcessField.setText(String.valueOf(monthExc * 12));
        currentEmiField.setText(String.valueOf(currEmi));

        bplCardNoField.setText(bplNumber);
        remarksField.setText(remarks);

    }
    //---End of put values---//

    //---Save loan application adapter---//
    private void saveLoanApplicationsAdapter(String result)
    {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(LoanApplicationDetailsAddActivity.this);
        builder1.setTitle("ENFIN Admin");
        builder1.setMessage("Loan Application Saved Successfully");
        builder1.setCancelable(true);
        builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface1, int i)
            {
                Intent intent = new Intent(getApplicationContext(),LoanApplicationActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog1 = builder1.create();
        alertDialog1.show();

    }
    //---End of save adapter---//

    private void setCollectionPointByNameAdapter(String result)
    {
        try {
            collectionPointNameByDayList = new ArrayList<CollPoint>();
            JSONArray jsonArray = new JSONArray(result);
//            CollPoint cp = new CollPoint(emptyGuid, "Select Collection Point");
//            collectionPointNameByDayList.add(cp);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                CollPoint collectionPointNameByDay = new CollPoint(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                collectionPointNameByDayList.add(collectionPointNameByDay);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerCollectionPointNameByDayArray = new String[collectionPointNameByDayList.size()];
        for (int i = 0; i < collectionPointNameByDayList.size(); i++)
        {
            spinnerCollectionPointNameByDayMap.put(collectionPointNameByDayList.get(i).getId(),collectionPointNameByDayList.get(i).getName());
            spinnerCollectionPointNameByDayArray[i] = collectionPointNameByDayList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoanApplicationDetailsAddActivity.this, android.R.layout.simple_spinner_item,spinnerCollectionPointNameByDayArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        collectionPointNameByDaySpinner.setAdapter(adapter);
    }
    private void setBorrowerNameAdapter(String result)
    {
        if(result.equals("[]"))
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(LoanApplicationDetailsAddActivity.this);
            builder.setTitle("ENFIN Admin");
            builder.setMessage("No Borrower Found");
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
        else
        {
            try
            {
                borrowerList = new ArrayList<Borrower>();
                borrowerName = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray(result);
                for (int k = 0; k < jsonArray.length(); k++)
                {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(k);
                    Borrower borrower = new Borrower(
                            jsonObject.getString("id"),
                            jsonObject.getString("fullName"),
                            jsonObject.getString("addressId"),
                            jsonObject.getString("memberEligibilityApprovedId"),
                            jsonObject.getInt("approvedAmount"),
                            jsonObject.getDouble("approvedInterest"),
                            jsonObject.getInt("approvedInsPeriod")
                    );
                    borrowerList.add(borrower);
                    borrowerName.add(jsonObject.getString("fullName"));

                }
                adapter.setData(borrowerName);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void setCoBorrowerNameAdapter(String result)
    {
        if(result.equals("[]"))
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(LoanApplicationDetailsAddActivity.this);
            builder.setTitle("ENFIN Admin");
            builder.setMessage("No Co-Borrower Found");
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
        else
        {
            try
            {
                coBorrowerList = new ArrayList<CoBorrower>();
                coBorrowerName = new ArrayList<String>();
                JSONArray jsonArray = new JSONArray(result);
                for (int k = 0; k < jsonArray.length(); k++)
                {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(k);
                    CoBorrower coBorrower = new CoBorrower(
                            jsonObject.getString("id"),
                            jsonObject.getString("fullName"),
                            jsonObject.getString("addressId"),
                            jsonObject.getString("memberEligibilityApprovedId")
                    );
                    coBorrowerList.add(coBorrower);
                    coBorrowerName.add(jsonObject.getString("fullName"));

                }
                coAdapter.setData(coBorrowerName);
                coAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setloanProductsAdapter(String result)
    {
        try {
            JSONArray jsonArray = new JSONArray(result);
            LoanProduct lp1= new LoanProduct(emptyGuid,"Select Loan Product");
            loanProductList.add(lp1);
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

        String[] spinnerloanProductsArray = new String[loanProductList.size()];
        for (int i = 0; i < loanProductList.size(); i++)
        {
            spinnerLoanProductMap.put(loanProductList.get(i).getId(),loanProductList.get(i).getName());
            spinnerloanProductsArray[i] = loanProductList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoanApplicationDetailsAddActivity.this, android.R.layout.simple_spinner_item,spinnerloanProductsArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        loanProductSpinner.setAdapter(adapter);
    }

    private void setloanPurposeAdapter(String result)
    {
        try {
            loanPurposeList = new ArrayList<LoanPurpose>();
            JSONArray jsonArray = new JSONArray(result);
            LoanPurpose lpr = new LoanPurpose(emptyGuid,"Select Loan Purpose");
            loanPurposeList.add(lpr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                LoanPurpose loanPurpose = new LoanPurpose(
                        jsonObject.getString("id"),
                        jsonObject.getString("description")
                );
                loanPurposeList.add(loanPurpose);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerloanPurposeArray = new String[loanPurposeList.size()];
        for (int i = 0; i < loanPurposeList.size(); i++)
        {
            spinnerLoanPurposeMap.put(loanPurposeList.get(i).getId(),loanPurposeList.get(i).getDescription());
            spinnerloanPurposeArray[i] = loanPurposeList.get(i).getDescription();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoanApplicationDetailsAddActivity.this, android.R.layout.simple_spinner_item,spinnerloanPurposeArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        loanPurposeSpinner.setAdapter(adapter);
    }

    private void setloanSubPurposeAdapter(String result)
    {
        try {
            loanSubPurposeList = new ArrayList<LoanSubPurpose>();
            JSONArray jsonArray = new JSONArray(result);
            LoanSubPurpose lsp = new LoanSubPurpose(emptyGuid,"Select Loan Sub-Purpose");
            loanSubPurposeList.add(lsp);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                LoanSubPurpose loanSubPurpose = new LoanSubPurpose(
                        jsonObject.getString("id"),
                        jsonObject.getString("description")
                );
                loanSubPurposeList.add(loanSubPurpose);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerloanSubPurposeArray = new String[loanSubPurposeList.size()];
        for (int i = 0; i < loanSubPurposeList.size(); i++)
        {
            spinnerLoanSubPurposeMap.put(loanSubPurposeList.get(i).getId(),loanSubPurposeList.get(i).getDescription());
            spinnerloanSubPurposeArray[i] = loanSubPurposeList.get(i).getDescription();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoanApplicationDetailsAddActivity.this, android.R.layout.simple_spinner_item,spinnerloanSubPurposeArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        loanSubPurposeSpinner.setAdapter(adapter);
    }

    private void setBankAdapter(String result)
    {
        try {
            bankList = new ArrayList<Bank>();
            JSONArray jsonArray = new JSONArray(result);
            Bank b1 = new Bank(emptyGuid, "Select Bank");
            bankList.add(b1);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Bank bank = new Bank(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                bankList.add(bank);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerBankArray = new String[bankList.size()];
        for (int i = 0; i < bankList.size(); i++)
        {
            spinnerBankMap.put(bankList.get(i).getId(),bankList.get(i).getName());
            spinnerBankArray[i] = bankList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoanApplicationDetailsAddActivity.this, android.R.layout.simple_spinner_item,spinnerBankArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        bankNameSpinner.setAdapter(adapter);
    }

    private void submitLoanApplicationsAdapter(String result)
    {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(LoanApplicationDetailsAddActivity.this);
        builder1.setTitle("ENFIN Admin");
        builder1.setMessage("Loan Application Submitted Successfully");
        builder1.setCancelable(true);
        builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface1, int i)
            {
                Intent intent = new Intent(getApplicationContext(),LoanApplicationActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog1 = builder1.create();
        alertDialog1.show();

    }

    private void populateCollectionPoint(int collectionDayIndex)
    {
        new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("getCollectionpointByDay/"+ collectionDayIndex);
    }
    private void populateLoanProduct()
    {
        new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("all-loanProduct");
    }

    private void populateLoanPurpose()
    {
        new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("all-loan-purpose");
    }

    private void populateLoanSubPurpose(String loanSubPurposeId)
    {
        new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("allByLoanPurpose/{"+ loanSubPurposeId +"}");
    }

    private void populateBank()
    {
        new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("all-bank-list");
    }

    private void populateCurrentEmi(String loanProductId, int approvedAmount, int approvedInstallmentPeriode)
    {
        new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("getEmi/{"+ loanProductId +"}/"+ approvedAmount +"/"+ approvedInstallmentPeriode);
    }

//    private void populateLoanSubmit()
//    {
//        new ApiHandler.GetAsync(LoanApplicationDetailsAddActivity.this).execute("submit-loanApplication/{"+ loanEditId +"}/{"+ eventId +"}/{"+ eventSeq +"}");
//    }


    // slide the view from below itself to the current position
    public void slideDown(View view){

        // Prepare the View for the animation
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);

// Start the animation
        view.animate()
                .translationY(0)
                .alpha(2.0f)
                .setListener(null);
    }

    // slide the view from its current position to below itself
    public void slideUp(final View view){
        view.animate()
                .translationY(0)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }
}
//---Ended by Debmalya---//
