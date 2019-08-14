package com.qbent.enfinsapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.qbent.enfinsapp.adapter.MemberDocumentAdapter;
import com.qbent.enfinsapp.adapter.MemberDocumentAdapterMobileVersion;
import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.global.GlobalImageSettings;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Block;
import com.qbent.enfinsapp.model.CollPoint;
import com.qbent.enfinsapp.model.Country;
import com.qbent.enfinsapp.model.District;
import com.qbent.enfinsapp.model.Documents;
import com.qbent.enfinsapp.model.GramPanchayet;
import com.qbent.enfinsapp.model.ImageLoader;
import com.qbent.enfinsapp.model.MemberKycDocument;
import com.qbent.enfinsapp.model.Municipality;
import com.qbent.enfinsapp.model.Occupation;
import com.qbent.enfinsapp.model.PoliceStation;
import com.qbent.enfinsapp.model.Qualifications;
import com.qbent.enfinsapp.model.State;
import com.qbent.enfinsapp.model.Village;
import com.qbent.enfinsapp.model.Ward;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MemberEditActivity extends MainActivity implements ApiCallback
{
    final AlertDialogue alertDialogue = new AlertDialogue(MemberEditActivity.this);
    long adharNumber;

    Button saveMemberEdit,cancelMemberBack;

    private Uri uri,selectedImageUri;

    private String memberEditId,imei,imei2;

    private RecyclerView recyclerView;

    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    Boolean editOccupationChanged = false;
    Boolean editQualificationChanged = false;
    Boolean editCountryChanged = false;
    Boolean editStateChanged = false;
    Boolean editDistrictChanged = false;
    Boolean editPoliceStationChanged = false;
    Boolean editMunChanged = false;
    Boolean editWardChanged = false;
    Boolean editPanchayetChanged = false;
    Boolean editVillChanged = false;
    Boolean editBlockChanged = false;
    Boolean editDocumentChanged = false;

    LinearLayout firstLinearLayout,secondLinearLayout,thirdLinearLayout,fourthLinearLayout,fifthLinearLayout,firstLinearLayoutForm,secondLinearLayoutForm,thirdLinearLayoutForm,fourthLinearLayoutForm,fifthLinearLayoutForm;
    TextView firstLinearLayoutText,secondLinearLayoutText,thirdLinearLayoutText,fourthLinearLayoutText,fifthLinearLayoutText;
    Boolean flagFirst = false;
    Boolean flagSecond = false;
    Boolean flagThird = false;
    Boolean flagFourth = false;
    Boolean flagFifth = false;

    final int CROP_PIC  = 7;
    final int CROP_PIC_SIGNATURE  = 8;
    final int CROP_PIC_DOCUMENT  = 9;

    private Bitmap bitmap,bitmapImage,bitmapSignature;
    String json,jsonImage,jsonSignature,image,imageImage,imageSignature;

    Integer REQUEST_CAMERA = 1, SELECT_FILE = 2;
    Integer REQUEST_CAMERA_SIGNATURE = 3, SELECT_FILE_SIGNATURE = 4;
    Integer REQUEST_CAMERA_IMAGE = 5, SELECT_FILE_IMAGE = 6;

    MemberDocumentAdapter mdAdapter;
    MemberDocumentAdapterMobileVersion memberDocumentAdapterMobileVersion;

    Spinner qualificationSpinner,occupationSpinner,maritalStatusSpinner,religionSpinner,castSpinner,genderSpinner
            ,countriesSpinner,statesSpinner,distrcitSpinner,policeStationSpinner,municipalitySpinner,wardSpinner,blockSpinner,gramPanchayetSpinner,
            villageSpinner,collectionDaySpinner,collectionPointNameSpinner,idTypeSpinner,visitStatusSpinner ;

    EditText memberApplicationNumberField,memberAdharNumberField,memberFirstNameField,memberMiddleNameField,memberLastNameField,
            memberDobField,memberMobileField,memberHouseField,memberStreetField,memberPostOfficeField,memberWardField,memberPincodeField,
            memberGurdainNameField,memberCardNoField,memberGurdainField,issueDateField,expiryDateField,memberMotherFirstNameField,memberMotherMiddleNameField,memberMotherLastNameField;

    Button addDocument,memberImageButton,memberSignatureButton;
    ImageButton memberImageSelect,memberSignatureSelect,memberImageTickButton,memberSignatureTickButton;
    ImageView memberImageView,memberSignatureView;

    CheckBox borrowerCheckFiled, coBorrowerCheckField, fatherCheckField;

    private List<Occupation> occupationList;
    private List<Qualifications> qualificationsList;
    private List<Country> countryLists;
    private List<State> statesLists;
    private List<District> districtList;
    private List<PoliceStation> policeStationList;
    private List<Municipality> municipalityList;
    private List<Ward> wardList;
    private List<Block> blockList;
    private List<GramPanchayet> gramPanchayetList;
    private List<Village> villageList;
    private List<Documents> documentsList;
    private List<CollPoint> collectionPointList;
    private List<MemberKycDocument> memberKycDocumentList = new ArrayList<MemberKycDocument>();

    private String qualificationId,stateId,districtId,policeId,municipalityId,wardId,blockId,gramPanchayatId,villageId;

    String id,code,firstName,middleName,lastName,guardianName,memberQualificationId,maritalStatus,dateOfBirth,occupationId,gender,religion,caste,motherFirstName,motherMiddleName,
            motherLastName,fullName,collectionPointId,collectionPointName,mobileNo,scanPicture,scanSignature,visitStatus,remarks,dateOfDeath,memberAccessCode,branchCode,memberAddressId,
            houseNo,streetName,countryId,memberStateId,memberDistrictId,memberBlockId,memberGramPanchayatId,memberMunicipalityId,memberWardId,memberPoliceStationId,postOffice,wardNo,pinCode,
            imageSaveFile,signatureSaveFile;


    Long aadharNo;


    boolean isFather,isBorrower,isCoBorrower,isDead,isDefault;

    int mem_adhar,collectionDay,position;

    HashMap<String, String> spinnerOccupationMap = new HashMap<String, String>();
    HashMap<String, String> spinnerQualificationsMap = new HashMap<String, String>();
    HashMap<String, String> spinnerCountriesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerStatesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerDistrictsMap = new HashMap<String, String>();
    HashMap<String, String> spinnerPoliceMap = new HashMap<String, String>();
    HashMap<String, String> spinnerMunicipalitiesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerWardsMap = new HashMap<String, String>();
    HashMap<String, String> spinnerBlocksMap = new HashMap<String, String>();
    HashMap<String, String> spinnerGramPanchayetMap = new HashMap<String, String>();
    HashMap<String, String> spinnerVillageMap = new HashMap<String, String>();
    HashMap<String, String> spinnerCollectionPointNamesMap = new HashMap<String, String>();

    HashMap<String, String> spinnerVisitMap = new HashMap<String, String>();

    Boolean editCollectionPoint = false;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        Configuration config = getResources().getConfiguration();
//        System.out.println(config.smallestScreenWidthDp);
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_member_edit_normal, null, false);
            drawer.addView(contentView, 0);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_member_edit, null, false);
            drawer.addView(contentView, 0);
        }

        memberEditId = getIntent().getStringExtra("emp_id");

        final AlertDialogue alertDialogue = new AlertDialogue(MemberEditActivity.this);

        fab.setVisibility(View.GONE);

        //---Edit text initialization---//
        memberApplicationNumberField = (EditText) findViewById(R.id.Edit_applicationNumberId);
        memberApplicationNumberField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberAdharNumberField = (EditText) findViewById(R.id.Edit_aadhar_No_Id);
        memberAdharNumberField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberFirstNameField = (EditText) findViewById(R.id.Edit_Applicant_FirstName_Id);
        memberFirstNameField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberMiddleNameField = (EditText) findViewById(R.id.Edit_Applicant_MiddleName_Id);
        memberMiddleNameField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberLastNameField = (EditText) findViewById(R.id.Edit_Applicant_LastName_Id);
        memberLastNameField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberDobField = (EditText) findViewById(R.id.Edit_Applicant_dob);
        borrowerCheckFiled = (CheckBox) findViewById(R.id.checkBox_Is_Borrower);
        coBorrowerCheckField = (CheckBox) findViewById(R.id.checkBox_Is_Co_Borrower);
        fatherCheckField = (CheckBox) findViewById(R.id.checkBox_Is_father);
        memberMobileField = (EditText) findViewById(R.id.Edit_Mobile_Id);
        memberGurdainNameField = (EditText) findViewById(R.id.Edit_Guardian_Id);
        memberGurdainNameField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberHouseField = (EditText) findViewById(R.id.Edit_House_No_Id);
        memberHouseField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberStreetField = (EditText) findViewById(R.id.Edit_Street_Name_Id);
        memberStreetField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberPostOfficeField = (EditText) findViewById(R.id.Edit_Post_Office_Id);
        memberPostOfficeField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberWardField = (EditText) findViewById(R.id.Edit_Ward_No_Id);
        memberWardField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberPincodeField = (EditText) findViewById(R.id.Edit_Pin_Code_Id);
        memberGurdainField = (EditText) findViewById(R.id.Remarks_Id);
        memberMotherFirstNameField = (EditText) findViewById(R.id.Edit_Mother_fristName);
        memberMotherFirstNameField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberMotherMiddleNameField = (EditText) findViewById(R.id.Edit_Mother_middleName);
        memberMotherMiddleNameField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberMotherLastNameField = (EditText) findViewById(R.id.Edit_Mother_lastName);
        memberMotherLastNameField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberImageSelect = (ImageButton) findViewById(R.id.memberImageUploadSelect);
        memberSignatureSelect = (ImageButton) findViewById(R.id.memberSignatureUploadSelect);
        memberSignatureButton = (Button) findViewById(R.id.memberSignatureUploadButton);
        memberImageButton = (Button) findViewById(R.id.memberImageUploadButton);
        memberImageTickButton = (ImageButton) findViewById(R.id.memberEditImageTickButton);
        memberSignatureTickButton = (ImageButton) findViewById(R.id.memberEditSignatureTickButton);
        memberImageView = (ImageView) findViewById(R.id.memberEditImageView);
        memberSignatureView = (ImageView) findViewById(R.id.memberEditSignatureView);
        //---End of edit text initialization---//

        //---Spinner initialization---//
        qualificationSpinner = (Spinner) findViewById(R.id.Edit_Education_Id);
        occupationSpinner = (Spinner) findViewById(R.id.Edit_Occupation_Id);
        maritalStatusSpinner = (Spinner) findViewById(R.id.Edit_Marital_Status_Id);
        religionSpinner = (Spinner) findViewById(R.id.Edit_Religion_Id);
        castSpinner = (Spinner) findViewById(R.id.Edit_Cast_Id);
        genderSpinner = (Spinner) findViewById(R.id.Edit_Gender_Id);
        countriesSpinner = (Spinner) findViewById(R.id.Edit_Country_Id);
        statesSpinner = (Spinner) findViewById(R.id.Edit_State_Id);
        distrcitSpinner = (Spinner) findViewById(R.id.Edit_District_Id);
        policeStationSpinner = (Spinner) findViewById(R.id.Edit_Police_Station_Id);
        municipalitySpinner = (Spinner) findViewById(R.id.Edit_Municipality_Id);
        wardSpinner = (Spinner) findViewById(R.id.Edit_Ward_Name_Id);
        blockSpinner = (Spinner) findViewById(R.id.Edit_Block_Id);
        gramPanchayetSpinner = (Spinner) findViewById(R.id.Edit_Panchayat_Id);
        villageSpinner = (Spinner) findViewById(R.id.Edit_Village_Id);
        collectionDaySpinner = (Spinner) findViewById(R.id.Edit_Collection_Day_Id);
        collectionPointNameSpinner = (Spinner) findViewById(R.id.Edit_Collection_Point_Name_Id);
        visitStatusSpinner = (Spinner) findViewById(R.id.Edit_Visit_Status_Id);
        //---End of Spinner initialization---//

        addDocument = (Button) findViewById(R.id.member_add_document);

        //---List initialization---//
        occupationList = new ArrayList<Occupation>();
        qualificationsList = new ArrayList<Qualifications>();
        countryLists = new ArrayList<Country>();
        statesLists = new ArrayList<State>();
        districtList = new ArrayList<District>();
        policeStationList = new ArrayList<PoliceStation>();
        municipalityList = new ArrayList<Municipality>();
        wardList = new ArrayList<Ward>();
        blockList = new ArrayList<Block>();
        gramPanchayetList = new ArrayList<GramPanchayet>();
        villageList = new ArrayList<Village>();
        documentsList = new ArrayList<Documents>();

        memberApplicationNumberField.setEnabled(false);

        populateDocuments();

        //---Device Responsive Part---//
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            recyclerView = findViewById(R.id.member_edit_document);
            recyclerView.setLayoutManager(new LinearLayoutManager(MemberEditActivity.this));
            memberDocumentAdapterMobileVersion = new MemberDocumentAdapterMobileVersion(memberKycDocumentList,MemberEditActivity.this);
            recyclerView.setAdapter(memberDocumentAdapterMobileVersion);
        }
        else
        {
            recyclerView = findViewById(R.id.member_edit_document);
            recyclerView.setLayoutManager(new LinearLayoutManager(MemberEditActivity.this));
            mdAdapter = new MemberDocumentAdapter(memberKycDocumentList,MemberEditActivity.this);
            recyclerView.setAdapter(mdAdapter);
        }
        //---End of Device Responsive Part---//


        memberImageTickButton.setVisibility(View.GONE);
        memberSignatureTickButton.setVisibility(View.GONE);


        firstLinearLayout = (LinearLayout) findViewById(R.id.memberFirstLayout);
        secondLinearLayout = (LinearLayout) findViewById(R.id.memberSecondLayout);
        thirdLinearLayout = (LinearLayout) findViewById(R.id.memberThirdLayout);
        fourthLinearLayout = (LinearLayout) findViewById(R.id.memberFourthLayout);
        fifthLinearLayout = (LinearLayout) findViewById(R.id.memberFifthLayout);

        firstLinearLayoutForm = (LinearLayout) findViewById(R.id.memberFirstLayoutForm);
        secondLinearLayoutForm = (LinearLayout) findViewById(R.id.memberSecondLayoutForm);
        thirdLinearLayoutForm = (LinearLayout) findViewById(R.id.memberThirdLayoutForm);
        fourthLinearLayoutForm = (LinearLayout) findViewById(R.id.memberFourthLayoutForm);
        fifthLinearLayoutForm = (LinearLayout) findViewById(R.id.memberFifthLayoutForm);

        firstLinearLayoutText = (TextView) findViewById(R.id.memberFirstLayoutText);
        secondLinearLayoutText = (TextView) findViewById(R.id.memberSecondLayoutText);
        thirdLinearLayoutText = (TextView) findViewById(R.id.memberThirdLayoutText);
        fourthLinearLayoutText = (TextView) findViewById(R.id.memberFourthLayoutText);
        fifthLinearLayoutText = (TextView) findViewById(R.id.memberFifthLayoutText);

        memberAdharNumberField = (EditText) findViewById(R.id.Edit_aadhar_No_Id);
        try {
            adharNumber = Long.parseLong(memberAdharNumberField.getText().toString().isEmpty()?"0":memberAdharNumberField.getText().toString());
        }
        catch (NumberFormatException nfe)
        {
            nfe.printStackTrace();
        }
        memberAdharNumberField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        memberAdharNumberField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (getCurrentFocus() == memberAdharNumberField)
                {
                    checkAdharCardStatus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });


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
        fourthLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagFourth){
                    // means true
                    slideDown(fourthLinearLayoutForm);
                    fourthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFourth = false;
                }
                else{
                    slideUpFourthLinearLayoutForm();
                }

            }
        });
        fifthLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagFifth){
                    // means true
                    slideDown(fifthLinearLayoutForm);
                    //thirdLinearLayoutForm.setVisibility(View.GONE);
                    fifthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFifth = false;
                }
                else{
                    slideUpFifthLinearLayoutForm();
                }

            }
        });


        //---End of list initialization---//

        addDocument.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MemberKycDocument memberKycDocument1 = new MemberKycDocument(
                        "",null,null,null,"","",
                        "","","","",null,null,null,documentsList
                );
                memberKycDocumentList.add(memberKycDocument1);

                if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
                {
                    memberDocumentAdapterMobileVersion.notifyDataSetChanged();
                }
                else
                {
                    mdAdapter.notifyDataSetChanged();
                }


            }
        });

        memberImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SelectImage();
            }
        });

        memberSignatureSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SelectSignature();
            }
        });

        memberImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(jsonImage==null)
                {
                    alertDialogue.showAlertMessage("Kindly select Image");
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(MemberEditActivity.this);
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
                            jsonObject.accumulate("image",jsonImage);
                            jsonObject.accumulate("fileName",imageImage);
                            apiRequest.set_t(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new ApiHandler.PostAsync(MemberEditActivity.this).execute(apiRequest);
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

        memberSignatureButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(jsonSignature==null)
                {
                    alertDialogue.showAlertMessage("Kindly select signature");
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(MemberEditActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to upload this signature?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        ApiRequest apiRequest = new ApiRequest("uploadPassbookFile3");
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.accumulate("image",jsonSignature);
                            jsonObject.accumulate("fileName",imageSignature);
                            apiRequest.set_t(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new ApiHandler.PostAsync(MemberEditActivity.this).execute(apiRequest);
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

        //---Marital status spinner---//
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.maritalStatus, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        maritalStatusSpinner.setPrompt("Select Marital Status");
        maritalStatusSpinner.setAdapter(arrayAdapter);
        //---End of marital status spinner---//

        //---Religion spinner---//
        ArrayAdapter<CharSequence> arrayAdapterReligion = ArrayAdapter.createFromResource(this, R.array.religions, android.R.layout.simple_spinner_item);
        arrayAdapterReligion.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        religionSpinner.setPrompt("Select Religion");
        religionSpinner.setAdapter(arrayAdapterReligion);
        //---End of religion spinner---//

        //---Cast spinner---//
        ArrayAdapter<CharSequence> arrayAdapterCast = ArrayAdapter.createFromResource(this, R.array.castes, android.R.layout.simple_spinner_item);
        arrayAdapterCast.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        castSpinner.setPrompt("Select Cast");
        castSpinner.setAdapter(arrayAdapterCast);
        //---End of cast spinner---//

        //---Gender spinner---//
        ArrayAdapter<CharSequence> arrayAdapterGender = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        arrayAdapterGender.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        genderSpinner.setPrompt("Select Gender");
        genderSpinner.setAdapter(arrayAdapterGender);
        //---End of gender spinner---//

        //---Collection days spinner---//
        ArrayAdapter<CharSequence> arrayAdapterDays = ArrayAdapter.createFromResource(this, R.array.collection_days, android.R.layout.simple_spinner_item);
        arrayAdapterDays.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        collectionDaySpinner.setPrompt("Select Collection Days");
        collectionDaySpinner.setAdapter(arrayAdapterDays);
        //---End of collection days spinner---//

        //---Visit status spinner---//
        ArrayAdapter<CharSequence> arrayAdapterVisitStatus = ArrayAdapter.createFromResource(this, R.array.visit_status, android.R.layout.simple_spinner_item);
        arrayAdapterVisitStatus.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        visitStatusSpinner.setPrompt("Select Status");
        visitStatusSpinner.setAdapter(arrayAdapterVisitStatus);
        //---End of visit status spinner---//

        if(memberEditId != null)
        {
            populateMemberEditData(memberEditId);
        }


        collectionDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int position = collectionDaySpinner.getSelectedItemPosition();
                populateCollectioPoints(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });

        collectionPointNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                if( collectionPointId!=null && !collectionPointId.isEmpty() && !collectionPointId.equals("null")&& !collectionPointId.equals(emptyGuid)&& !collectionPointId.equals(emptyGuid) && editCollectionPoint==false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerCollectionPointNamesMap.keySet());
                    int test = indexes1.indexOf(collectionPointId);
                    String test2 = (new ArrayList<String>(spinnerCollectionPointNamesMap.values())).get(test);
                    collectionPointNameSpinner.setSelection(((ArrayAdapter<String>)collectionPointNameSpinner.getAdapter()).getPosition(test2));


                }
                else
                {
                    String name = collectionPointNameSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerCollectionPointNamesMap.values());
                    int a = indexes.indexOf(name);
                    collectionPointId = (new ArrayList<String>(spinnerCollectionPointNamesMap.keySet())).get(indexes.indexOf(name));

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
        collectionPointNameSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editCollectionPoint = true;
                }
                return false;
            }
        });
        //---Educational qualifications spinner---//
        qualificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!memberQualificationId.isEmpty() && !memberQualificationId.equals(emptyGuid) && !memberQualificationId.equals("null") && editQualificationChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerQualificationsMap.keySet());
                    int test = indexes1.indexOf(memberQualificationId);
                    String test2 = (new ArrayList<String>(spinnerQualificationsMap.values())).get(test);
                    qualificationSpinner.setSelection(((ArrayAdapter<String>)qualificationSpinner.getAdapter()).getPosition(test2));
                    qualificationId = memberQualificationId;
                }
                else
                {
                    String name = qualificationSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerQualificationsMap.values());
                    int a = indexes.indexOf(name);
                    qualificationId = (new ArrayList<String>(spinnerQualificationsMap.keySet())).get(indexes.indexOf(name));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });

        qualificationSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editQualificationChanged = true;
                }
                return false;
            }
        });
        //---End of educational qualifications spinner---//

        //---Occupation spinner---//
        occupationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!occupationId.isEmpty() && !occupationId.equals(emptyGuid) && !occupationId.equals("null")  && editOccupationChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerOccupationMap.keySet());
                    int test = indexes1.indexOf(occupationId);
                    String test2 = (new ArrayList<String>(spinnerOccupationMap.values())).get(test);
                    occupationSpinner.setSelection(((ArrayAdapter<String>)occupationSpinner.getAdapter()).getPosition(test2));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });

        occupationSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editOccupationChanged = true;
                }
                return false;
            }
        });
        //---End of occupation spinner---//

        //---Country spinner---//
        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!countryId.isEmpty() && !countryId.equals(emptyGuid) && !memberStateId.equals("null") && editCountryChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerCountriesMap.keySet());
                    int test = indexes1.indexOf(countryId);
                    String test2 = (new ArrayList<String>(spinnerCountriesMap.values())).get(test);
                    countriesSpinner.setSelection(((ArrayAdapter<String>)countriesSpinner.getAdapter()).getPosition(test2));
                    stateId = countryId;
                }
                else
                {
                    String name = countriesSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerCountriesMap.values());
                    int a = indexes.indexOf(name);
                    stateId = (new ArrayList<String>(spinnerCountriesMap.keySet())).get(indexes.indexOf(name));
                }
                populateStates(stateId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });

        countriesSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editCountryChanged = true;
                }
                return false;
            }
        });
        //---End of country spinner---//

        //---States spinner---//
        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!memberStateId.isEmpty() && !memberStateId.equals(emptyGuid) && !memberStateId.equals("null") && editStateChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerStatesMap.keySet());
                    int test = indexes1.indexOf(memberStateId);
                    String test2 = (new ArrayList<String>(spinnerStatesMap.values())).get(test);
                    statesSpinner.setSelection(((ArrayAdapter<String>)statesSpinner.getAdapter()).getPosition(test2));
                    districtId = memberStateId;
                }
                else
                {
                    String name = statesSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerStatesMap.values());
                    int a = indexes.indexOf(name);
                    districtId = (new ArrayList<String>(spinnerStatesMap.keySet())).get(indexes.indexOf(name));
                }
                populateDistricts(districtId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });

        statesSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editStateChanged = true;
                }
                return false;
            }
        });
        //---End of states spinner---//

        //---District spinner---//
        distrcitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(!memberDistrictId.isEmpty() && !memberDistrictId.equals(emptyGuid) && !memberDistrictId.equals("null") && editDistrictChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerDistrictsMap.keySet());
                    int test = indexes1.indexOf(memberDistrictId);
                    String test2 = (new ArrayList<String>(spinnerDistrictsMap.values())).get(test);
                    distrcitSpinner.setSelection(((ArrayAdapter<String>)distrcitSpinner.getAdapter()).getPosition(test2));
                    policeId = memberDistrictId;
                    municipalityId = memberDistrictId;
                }
                else
                {
                    String name = distrcitSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerDistrictsMap.values());
                    int a = indexes.indexOf(name);
                    policeId = (new ArrayList<String>(spinnerDistrictsMap.keySet())).get(indexes.indexOf(name));
                    municipalityId = (new ArrayList<String>(spinnerDistrictsMap.keySet())).get(indexes.indexOf(name));
                    blockId = (new ArrayList<String>(spinnerDistrictsMap.keySet())).get(indexes.indexOf(name));
                }
                populatePoliceStations(policeId);
                populateMunicipalities(municipalityId);
                populateBlocks(municipalityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        distrcitSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editDistrictChanged = true;
                }
                return false;
            }
        });
        //---End of district spinner---//

        //---Police station spinner---//
        policeStationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(!memberPoliceStationId.isEmpty() && !memberPoliceStationId.equals(emptyGuid) && !memberPoliceStationId.equals("null") && editPoliceStationChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerPoliceMap.keySet());
                    int test = indexes1.indexOf(memberPoliceStationId);
                    String test2 = (new ArrayList<String>(spinnerPoliceMap.values())).get(test);
                    policeStationSpinner.setSelection(((ArrayAdapter<String>)policeStationSpinner.getAdapter()).getPosition(test2));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        policeStationSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editPoliceStationChanged = true;
                }
                return false;
            }
        });
        //---End of police station spinner---//

        //---Municipality spinner---//
        municipalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(!memberMunicipalityId.isEmpty() && !memberMunicipalityId.equals(emptyGuid) && !memberMunicipalityId.equals("null") && editMunChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerMunicipalitiesMap.keySet());
                    int test = indexes1.indexOf(memberMunicipalityId);
                    String test2 = (new ArrayList<String>(spinnerMunicipalitiesMap.values())).get(test);
                    municipalitySpinner.setSelection(((ArrayAdapter<String>)municipalitySpinner.getAdapter()).getPosition(test2));
                    wardId = memberMunicipalityId;
                }
                else
                {
                    String name = municipalitySpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerMunicipalitiesMap.values());
                    int a = indexes.indexOf(name);
                    wardId = (new ArrayList<String>(spinnerMunicipalitiesMap.keySet())).get(indexes.indexOf(name));

                }
                populateWards(wardId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        municipalitySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editMunChanged = true;
                }
                return false;
            }
        });
        //---End of municipality spinner---//

        //---Ward spinner---//
        wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(!memberWardId.isEmpty() && !memberWardId.equals(emptyGuid) && !memberWardId.equals("null") && editWardChanged ==false)
                {
                    List<String> indexes = new ArrayList<String>(spinnerWardsMap.keySet());
                    int test = indexes.indexOf(memberWardId);
                    String test2 = (new ArrayList<String>(spinnerWardsMap.values())).get(test);
                    wardSpinner.setSelection(((ArrayAdapter<String>)wardSpinner.getAdapter()).getPosition(test2));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });
        wardSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editWardChanged = true;
                }
                return false;
            }
        });
        //---End of ward spinner---//

        //---Block Spinner---//
        blockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(!memberBlockId.isEmpty() && !memberBlockId.equals(emptyGuid) && !memberBlockId.equals("null") && editBlockChanged ==false)
                {
                    List<String> indexes = new ArrayList<String>(spinnerBlocksMap.keySet());
                    int test = indexes.indexOf(memberBlockId);
                    String test2 = (new ArrayList<String>(spinnerBlocksMap.values())).get(test);
                    blockSpinner.setSelection(((ArrayAdapter<String>)blockSpinner.getAdapter()).getPosition(test2));
                    gramPanchayatId = memberBlockId;
                }

                else
                {
                    String name = blockSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerBlocksMap.values());
                    int a = indexes.indexOf(name);
                    gramPanchayatId = (new ArrayList<String>(spinnerBlocksMap.keySet())).get(indexes.indexOf(name));

                }
                populatePanchayets(gramPanchayatId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });
        blockSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editBlockChanged = true;
                }
                return false;
            }
        });
        //---End of block spinner---//

        //---Panchayet spinner---//
        gramPanchayetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(!memberGramPanchayatId.isEmpty() && !memberGramPanchayatId.equals(emptyGuid) && !memberGramPanchayatId.equals("null") && editPanchayetChanged ==false)
                {
                    List<String> indexes = new ArrayList<String>(spinnerGramPanchayetMap.keySet());
                    int test = indexes.indexOf(memberGramPanchayatId);
                    String test2 = (new ArrayList<String>(spinnerGramPanchayetMap.values())).get(test);
                    gramPanchayetSpinner.setSelection(((ArrayAdapter<String>)gramPanchayetSpinner.getAdapter()).getPosition(test2));
                    villageId = memberGramPanchayatId;
                }
                else
                {
                    String name = gramPanchayetSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerGramPanchayetMap.values());
                    int a = indexes.indexOf(name);
                    villageId = (new ArrayList<String>(spinnerGramPanchayetMap.keySet())).get(indexes.indexOf(name));
                }
                populateVillages(villageId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });
        gramPanchayetSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editPanchayetChanged = true;
                }
                return false;
            }
        });
        //---End of panchayet spinner---//

        //---Village spinner---//
        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                String name = villageSpinner.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerVillageMap.values());
                int a = indexes.indexOf(name);
                villageId = (new ArrayList<String>(spinnerVillageMap.keySet())).get(indexes.indexOf(name));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });
        villageSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editVillChanged = true;
                }
                return false;
            }
        });
        //---End of village spinner---//

        populateCountries();
        populateQualifications();
        populateOccupations();
//        populateMemberEdit(memberEditId);

        saveMemberEdit = (Button) findViewById(R.id.member_edit_save);
        cancelMemberBack = (Button) findViewById(R.id.member_edit_cancel);
        cancelMemberBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent memberIntent = new Intent(getApplicationContext(),MemberActivity.class);
                startActivity(memberIntent);
            }
        });
        saveMemberEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final List<MemberKycDocument> documents = mdAdapter.retrieveKycData();

                if(memberAdharNumberField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    memberAdharNumberField.setError("Please enter Aadhar Number");
                    memberAdharNumberField.requestFocus();
                    memberAdharNumberField.performClick();
                    return;
                }

                if(memberAdharNumberField.getText().toString().length() != 12)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    memberAdharNumberField.setError("Adhaar number should be 12 digit");
                    memberAdharNumberField.requestFocus();
                    memberAdharNumberField.performClick();
                    return;
                }
                if(memberFirstNameField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    memberFirstNameField.setError("Please enter First Name");
                    memberFirstNameField.requestFocus();
                    memberFirstNameField.performClick();
                    return;
                }

                if(memberLastNameField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    memberLastNameField.setError("Please enter Last Name");
                    memberLastNameField.requestFocus();
                    memberLastNameField.performClick();
                    return;
                }

                if(memberDobField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    memberDobField.setError("Please enter Date of Birth");
                    memberDobField.requestFocus();
                    memberDobField.performClick();
                    return;
                }

                if(spinnerQualificationsMap.size()==0||(new ArrayList<String>(spinnerQualificationsMap.keySet())).get(new ArrayList<String>(spinnerQualificationsMap.values())
                .indexOf(qualificationSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {

                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select Qualification");
                    setSpinnerError(qualificationSpinner,"Please select Qualification");
                    qualificationSpinner.setFocusable(true);
                    qualificationSpinner.requestFocus();
                    return;
                }

                if(spinnerOccupationMap.size()==0||(new ArrayList<String>(spinnerOccupationMap.keySet())).get(new ArrayList<String>(spinnerOccupationMap.values())
                        .indexOf(occupationSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {

                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select Occupation");
                    setSpinnerError(occupationSpinner,"Please select Occupation");
                    occupationSpinner.setFocusable(true);
                    occupationSpinner.requestFocus();
                    return;
                }

                if(maritalStatusSpinner.getSelectedItemPosition() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select Marital Status");
                    setSpinnerError(maritalStatusSpinner,"Please select Marital Status");
                    maritalStatusSpinner.setFocusable(true);
                    maritalStatusSpinner.requestFocus();
                    return;
                }

                if(religionSpinner.getSelectedItemPosition() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select Religion");
                    setSpinnerError(religionSpinner,"Please select Religion");
                    religionSpinner.setFocusable(true);
                    religionSpinner.requestFocus();
                    return;
                }

                if(castSpinner.getSelectedItemPosition() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select Caste");
                    setSpinnerError(castSpinner,"Please select Caste");
                    castSpinner.setFocusable(true);
                    castSpinner.requestFocus();
                    return;
                }

                if(genderSpinner.getSelectedItemPosition() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    alertDialogue.showAlertMessage("Please select Gender");
                    setSpinnerError(genderSpinner,"Please select Gender");
                    genderSpinner.setFocusable(true);
                    genderSpinner.requestFocus();
                    return;
                }
                if(memberMobileField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    memberMobileField.setError("Please enter Mobile Number");
                    memberMobileField.requestFocus();
                    memberMobileField.performClick();
                    return;
                }

                if(memberGurdainNameField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    memberGurdainNameField.setError("Please enter Guardian Name");
                    memberGurdainNameField.requestFocus();
                    memberGurdainNameField.performClick();
                    return;
                }

                if(memberGurdainNameField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    memberGurdainNameField.setError("Please enter Guardian Name");
                    memberGurdainNameField.requestFocus();
                    memberGurdainNameField.performClick();
                    return;
                }

                if(memberMotherFirstNameField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    memberMotherFirstNameField.setError("Please enter Mother First Name");
                    memberMotherFirstNameField.requestFocus();
                    memberMotherFirstNameField.performClick();
                    return;
                }

                if(memberMotherLastNameField.getText().toString().length() == 0)
                {
                    slideDown(firstLinearLayoutForm);
                    firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagFirst = false;
                    memberMotherLastNameField.setError("Please enter Mother Last Name");
                    memberMotherLastNameField.requestFocus();
                    memberMotherLastNameField.performClick();
                    return;
                }
                if(spinnerCountriesMap.size()==0||(new ArrayList<String>(spinnerCountriesMap.keySet())).get(new ArrayList<String>(spinnerCountriesMap.values())
                        .indexOf(countriesSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {

                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    alertDialogue.showAlertMessage("Please select Country");
                    setSpinnerError(countriesSpinner,"Please select Country");
                    countriesSpinner.setFocusable(true);
                    countriesSpinner.requestFocus();
                    return;
                }
                if(spinnerStatesMap.size()==0||(new ArrayList<String>(spinnerStatesMap.keySet())).get(new ArrayList<String>(spinnerStatesMap.values())
                        .indexOf(statesSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {

                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    alertDialogue.showAlertMessage("Please select State");
                    setSpinnerError(statesSpinner,"Please select State");
                    statesSpinner.setFocusable(true);
                    statesSpinner.requestFocus();
                    return;
                }
                if(spinnerDistrictsMap.size()==0||(new ArrayList<String>(spinnerDistrictsMap.keySet())).get(new ArrayList<String>(spinnerDistrictsMap.values())
                        .indexOf(distrcitSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {

                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    alertDialogue.showAlertMessage("Please select District");
                    setSpinnerError(distrcitSpinner,"Please select District");
                    distrcitSpinner.setFocusable(true);
                    distrcitSpinner.requestFocus();
                    return;
                }
                if(spinnerPoliceMap.size()==0||(new ArrayList<String>(spinnerPoliceMap.keySet())).get(new ArrayList<String>(spinnerPoliceMap.values())
                        .indexOf(policeStationSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {

                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    alertDialogue.showAlertMessage("Please select Police Station");
                    setSpinnerError(policeStationSpinner,"Please select Police Station");
                    policeStationSpinner.setFocusable(true);
                    policeStationSpinner.requestFocus();
                    return;
                }
                if(memberPostOfficeField.getText().toString().length() == 0)
                {
                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    memberPostOfficeField.setError("Please enter Post Office");
                    memberPostOfficeField.requestFocus();
                    memberPostOfficeField.performClick();
                    return;
                }
                if(memberPincodeField.getText().toString().length() == 0)
                {
                    slideDown(secondLinearLayoutForm);
                    secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                    flagSecond = false;
                    memberPincodeField.setError("Please enter Pin Code");
                    memberPincodeField.requestFocus();
                    memberPincodeField.performClick();
                    return;
                }
                for(int l = 0; l < documents.size(); l++)
                {
                    if(documents.get(l).getKycdocumentId()==null||documents.get(l).getKycdocumentId().isEmpty()||documents.get(l).getKycdocumentId().equals("null") || documents.get(l).getKycdocumentId().equals(emptyGuid))
                    {
                        slideDown(fourthLinearLayoutForm);
                        fourthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                        flagFourth = false;
                        alertDialogue.showAlertMessage("Please select Document Type");
                        return;
                    }
                    if(documents.get(l).getKycdocumentName()==null||documents.get(l).getKycdocumentName().isEmpty()||documents.get(l).getKycdocumentName().equals("null"))
                    {
                        slideDown(fourthLinearLayoutForm);
                        fourthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                        flagFourth = false;
                        alertDialogue.showAlertMessage("Please upload Document");
                        return;
                    }
                    if(documents.get(l).getKycdocumentNo()==null||documents.get(l).getKycdocumentNo().isEmpty()||documents.get(l).getKycdocumentNo().equals("null"))
                    {
                        slideDown(fourthLinearLayoutForm);
                        fourthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);
                        flagFourth = false;
                        alertDialogue.showAlertMessage("Please enter Document Number");
                        return;
                    }
                }
                if(!visitStatusSpinner.getSelectedItem().toString().equals("Interested")&& memberGurdainField.getText().toString().length() == 0)
                {
                    memberGurdainField.setError("Please enter Remarks");
                    memberGurdainField.requestFocus();
                    memberGurdainField.performClick();
                    return;
                }
//                if((new ArrayList<String>(spinnerCollectionPointNameByDayMap.keySet())).get(new ArrayList<String>(spinnerCollectionPointNameByDayMap.values())
//                        .indexOf(collectionPointNameByDaySpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
//                {
//
//                    slideUpFirstLinearLayoutForm();
//                    alertDialogue.showAlertMessage("Please select a collection point name");
//                    setSpinnerError(collectionPointNameByDaySpinner,"Select Collection Point Name");
//                    collectionPointNameByDaySpinner.setFocusable(true);
//                    collectionPointNameByDaySpinner.requestFocus();
//                    return;
//                }
//
//
//                if(relationshipSpinner.getSelectedItemPosition() == 0)
//                {
//                    slideUpFirstLinearLayoutForm();
//                    alertDialogue.showAlertMessage("Please select a relation");
//                    setSpinnerError(relationshipSpinner,"Please select a relation");
//                    relationshipSpinner.setFocusable(true);
//                    relationshipSpinner.requestFocus();
//                    return;
//                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(MemberEditActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to edit member?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k)
                    {
                        ApiRequest apiRequest = new ApiRequest("edit-member/{"+ memberEditId +"}");
                        try{
                            JSONObject jsonObject = new JSONObject();
                            JSONObject addressObject = new JSONObject();

                            jsonObject.put("id",memberEditId);
                            jsonObject.put("firstName",(!memberFirstNameField.getText().toString().isEmpty())?memberFirstNameField.getText().toString():null);
                            jsonObject.put("middleName",(!memberMiddleNameField.getText().toString().isEmpty())?memberMiddleNameField.getText().toString():null);
                            jsonObject.put("lastName",(!memberLastNameField.getText().toString().isEmpty())?memberLastNameField.getText().toString():null);
                            jsonObject.put("guardianName",(!memberGurdainNameField.getText().toString().isEmpty())?memberGurdainNameField.getText().toString():null);
                            jsonObject.put("dateOfBirth",(!memberDobField.getText().toString().isEmpty())?memberDobField.getText().toString():null);
                            jsonObject.put("isFather",fatherCheckField.isChecked());
                            jsonObject.put("qualificationId",(new ArrayList<String>(spinnerQualificationsMap.keySet())).get(new ArrayList<String>(spinnerQualificationsMap.values()).indexOf(qualificationSpinner.getSelectedItem().toString())).toString());
                            jsonObject.put("maritalStatus",maritalStatusSpinner.getSelectedItem().toString());
                            jsonObject.put("gender",genderSpinner.getSelectedItem().toString());
                            jsonObject.put("occupationId",(new ArrayList<String>(spinnerOccupationMap.keySet())).get(new ArrayList<String>(spinnerOccupationMap.values()).indexOf(occupationSpinner.getSelectedItem().toString())).toString());
                            jsonObject.put("aadharNo",(!memberAdharNumberField.getText().toString().isEmpty())?memberAdharNumberField.getText().toString():null);
                            jsonObject.put("religion",religionSpinner.getSelectedItem().toString());
                            jsonObject.put("caste",castSpinner.getSelectedItem().toString());
                            jsonObject.put("motherFirstName",(!memberMotherFirstNameField.getText().toString().isEmpty())?memberMotherFirstNameField.getText().toString():null);
                            jsonObject.put("motherMiddleName",(!memberMotherMiddleNameField.getText().toString().isEmpty())?memberMotherMiddleNameField.getText().toString():null);
                            jsonObject.put("motherLastName",(!memberMotherLastNameField.getText().toString().isEmpty())?memberMotherLastNameField.getText().toString():null);
                            jsonObject.put("collectionPointId",(new ArrayList<String>(spinnerCollectionPointNamesMap.keySet())).get(new ArrayList<String>(spinnerCollectionPointNamesMap.values()).indexOf(collectionPointNameSpinner.getSelectedItem().toString())).toString());
                            jsonObject.put("mobileNo",(!memberMobileField.getText().toString().isEmpty())?memberMobileField.getText().toString():null);
                            jsonObject.put("visitStatus",visitStatusSpinner.getSelectedItem().toString());
                            jsonObject.put("remarks",(!memberGurdainField.getText().toString().isEmpty())?memberGurdainField.getText().toString():null);
                            jsonObject.put("isBorrower",borrowerCheckFiled.isChecked());
                            jsonObject.put("isCoBorrower",coBorrowerCheckField.isChecked());
                            jsonObject.put("scanPicture",imageSaveFile);
                            jsonObject.put("scanSignature",signatureSaveFile);

                            //jsonObject.accumulate("memberAddres");

                            addressObject.put("memberId",memberEditId);
                            addressObject.put("houseNo",(!memberHouseField.getText().toString().isEmpty())?memberHouseField.getText().toString():null);
                            addressObject.put("streetName",(!memberStreetField.getText().toString().isEmpty())?memberStreetField.getText().toString():null);
                            addressObject.put("postOffice",(!memberPostOfficeField.getText().toString().isEmpty())?memberPostOfficeField.getText().toString():null);
                            addressObject.put("wardNo",(!memberWardField.getText().toString().isEmpty())?memberWardField.getText().toString():null);
                            addressObject.put("pinCode",(!memberPincodeField.getText().toString().isEmpty())?memberPincodeField.getText().toString():null);
                            addressObject.put("policeStationId",(new ArrayList<String>(spinnerPoliceMap.keySet())).get(new ArrayList<String>(spinnerPoliceMap.values()).indexOf(policeStationSpinner.getSelectedItem().toString())).toString());
                            addressObject.put("countryId",!(new ArrayList<String>(spinnerCountriesMap.keySet())).get(new ArrayList<String>(spinnerCountriesMap.values()).indexOf(countriesSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerCountriesMap.keySet())).get(new ArrayList<String>(spinnerCountriesMap.values()).indexOf(countriesSpinner.getSelectedItem().toString())).toString():null);
                            addressObject.put("stateId",!(new ArrayList<String>(spinnerStatesMap.keySet())).get(new ArrayList<String>(spinnerStatesMap.values()).indexOf(statesSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerStatesMap.keySet())).get(new ArrayList<String>(spinnerStatesMap.values()).indexOf(statesSpinner.getSelectedItem().toString())).toString():null);
                            addressObject.put("districtId",!(new ArrayList<String>(spinnerDistrictsMap.keySet())).get(new ArrayList<String>(spinnerDistrictsMap.values()).indexOf(distrcitSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerDistrictsMap.keySet())).get(new ArrayList<String>(spinnerDistrictsMap.values()).indexOf(distrcitSpinner.getSelectedItem().toString())).toString():null);
                            addressObject.put("municipalityId",(spinnerMunicipalitiesMap.size() != 0)?!(new ArrayList<String>(spinnerMunicipalitiesMap.keySet())).get(new ArrayList<String>(spinnerMunicipalitiesMap.values()).indexOf(municipalitySpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerMunicipalitiesMap.keySet())).get(new ArrayList<String>(spinnerMunicipalitiesMap.values()).indexOf(municipalitySpinner.getSelectedItem().toString())).toString():null:null);
                            addressObject.put("wardId",(spinnerWardsMap.size() != 0)?!(new ArrayList<String>(spinnerWardsMap.keySet())).get(new ArrayList<String>(spinnerWardsMap.values()).indexOf(wardSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerWardsMap.keySet())).get(new ArrayList<String>(spinnerWardsMap.values()).indexOf(wardSpinner.getSelectedItem().toString())).toString():null:null);
                            addressObject.put("villageId",(spinnerVillageMap.size() != 0)?!(new ArrayList<String>(spinnerVillageMap.keySet())).get(new ArrayList<String>(spinnerVillageMap.values()).indexOf(villageSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerVillageMap.keySet())).get(new ArrayList<String>(spinnerVillageMap.values()).indexOf(villageSpinner.getSelectedItem().toString())).toString():null:null);
                            addressObject.put("blockId",(spinnerBlocksMap.size() != 0)?!(new ArrayList<String>(spinnerBlocksMap.keySet())).get(new ArrayList<String>(spinnerBlocksMap.values()).indexOf(blockSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerBlocksMap.keySet())).get(new ArrayList<String>(spinnerBlocksMap.values()).indexOf(blockSpinner.getSelectedItem().toString())).toString():null:null);
                            addressObject.put("gramPanchayatId",(spinnerGramPanchayetMap.size() != 0)?!(new ArrayList<String>(spinnerGramPanchayetMap.keySet())).get(new ArrayList<String>(spinnerGramPanchayetMap.values()).indexOf(gramPanchayetSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerGramPanchayetMap.keySet())).get(new ArrayList<String>(spinnerGramPanchayetMap.values()).indexOf(gramPanchayetSpinner.getSelectedItem().toString())).toString():null:null);
                            addressObject.put("isDefault",true);

                            JSONObject obj = null;
                            JSONArray jsonArray = new JSONArray();


                            for (int i = 0; i < documents.size(); i++) {
                                obj = new JSONObject();
                                try {
                                    obj.put("kycdocumentId", documents.get(i).getKycdocumentId());
                                    obj.put("kycdocumentNo", documents.get(i).getKycdocumentNo());
                                    obj.put("kycdocumentScan", documents.get(i).getKycdocumentScan());
                                    obj.put("kycdocumentName", documents.get(i).getKycdocumentName());
                                    obj.put("issueDate", (documents.get(i).getIssueDate()!=null&&!documents.get(i).getIssueDate().equals("null"))?documents.get(i).getIssueDate():null);
                                    obj.put("expiryDate", (documents.get(i).getExpiryDate()!=null&&!documents.get(i).getExpiryDate().equals("null"))?documents.get(i).getExpiryDate():null);

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                jsonArray.put(obj);
                            }



                            jsonObject.put("memberAddres",addressObject);
                            jsonObject.put("memberKycdocuments",jsonArray);

                            String value = jsonObject.toString();


                            apiRequest.set_t(jsonObject);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(MemberEditActivity.this).execute(apiRequest);
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


    }




    public void slideUpFirstLinearLayoutForm()
    {
//        slideDown(secondLinearLayoutForm);
//        slideDown(thirdLinearLayoutForm);
//        slideDown(fourthLinearLayoutForm);
//        slideDown(fifthLinearLayoutForm);
        slideUp(firstLinearLayoutForm);

        firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        thirdLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        fourthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        fifthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
        flagFirst = true;
//        flagThird = false;
//        flagSecond = false;
//        flagFourth = false;
//        flagFifth = false;
    }
    public void slideUpSecondLinearLayoutForm()
    {
//        slideDown(firstLinearLayoutForm);
//        slideDown(thirdLinearLayoutForm);
//        slideDown(fourthLinearLayoutForm);
//        slideDown(fifthLinearLayoutForm);
        slideUp(secondLinearLayoutForm);

        secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        thirdLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        fourthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        fifthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        flagThird = false;
        flagSecond = true;
//        flagFirst = false;
//        flagFourth = false;
//        flagFifth = false;
    }
    public void slideUpThirdLinearLayoutForm()
    {
//        slideDown(firstLinearLayoutForm);
//        slideDown(secondLinearLayoutForm);
//        slideDown(fourthLinearLayoutForm);
//        slideDown(fifthLinearLayoutForm);
        slideUp(thirdLinearLayoutForm);

        thirdLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        fourthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        fifthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
        flagThird = true;
//        flagSecond = false;
//        flagFirst = false;
//        flagFourth = false;
//        flagFifth = false;
    }
    public void slideUpFourthLinearLayoutForm()
    {
//        slideDown(firstLinearLayoutForm);
//        slideDown(secondLinearLayoutForm);
//        slideDown(thirdLinearLayoutForm);
        slideUp(fourthLinearLayoutForm);
//        slideDown(fifthLinearLayoutForm);
//
//        secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        thirdLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
        fourthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        fifthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        flagThird = false;
//        flagSecond = false;
//        flagFirst = false;
        flagFourth = true;
//        flagFifth = false;
    }
    public void slideUpFifthLinearLayoutForm()
    {
//        slideDown(firstLinearLayoutForm);
//        slideDown(secondLinearLayoutForm);
//        slideDown(thirdLinearLayoutForm);
//        slideDown(fourthLinearLayoutForm);
        slideUp(fifthLinearLayoutForm);

//        thirdLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        secondLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        firstLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        fourthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
        fifthLinearLayoutText.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
//        flagThird = false;
//        flagSecond = false;
//        flagFirst = false;
//        flagFourth = false;
        flagFifth = true;
    }

    private void checkAdharCardStatus()
    {
        if(!memberAdharNumberField.getText().toString().isEmpty())
        {
            if(memberAdharNumberField.getText().toString().length() == 12)
            {
                checkMemberAdhar(Long.parseLong(memberAdharNumberField.getText().toString().isEmpty()?"0":memberAdharNumberField.getText().toString()),memberEditId);
                return;
            }
        }
    }

    private void checkMemberAdhar(long adharNumber, String memberEditId) {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("addharNoExists/"+ adharNumber +"/{"+ memberEditId + "}");
    }

    private void setAdharCardCheckAdapter(String  result) throws JSONException
    {


        if(result.equals("true"))
        {
            alertDialogue.showAlertMessage("Addhar No. Already Exists");
            memberAdharNumberField.setText(null);
            return;
        }
    }




    private void populateQualifications()
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("all-qualifications");
    }

    private void populateOccupations()
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("all-occupations");
    }

    private void populateCountries()
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("countries");
    }

    private void populateStates(String stateId)
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("states-by-country/{"+ stateId +"}");
    }

    private void populateDistricts(String districtId)
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("districts-by-state/{"+ districtId +"}");
    }

    private void populatePoliceStations(String policeId)
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("PoliceStation-by-district/{"+ policeId +"}");
    }

    private void populateMunicipalities(String municipalityId)
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("municipalities-by-district/{"+ municipalityId +"}");
    }

    private void populateWards(String wardId)
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("wards-by-municipality/{"+ wardId +"}");
    }

    private void populateBlocks(String blockId)
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("blocks-by-district/{"+ blockId +"}");
    }

    private void populatePanchayets(String gramPanchayatId)
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("panchayats-by-block/{"+ gramPanchayatId +"}");
    }

    private void populateVillages(String villageId)
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("villages-by-panchayat/{"+ villageId +"}");
    }

    private void populateDocuments()
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("all-documents");
    }

    private void populateMemberEditData(String memberEditId)
    {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("member/{" + memberEditId + "}");
    }

    private void populateCollectioPoints(int position) {
        new ApiHandler.GetAsync(MemberEditActivity.this).execute("getCollectionpointByDay/"+ position);
    }


    @Override
    public void onApiRequestStart() throws IOException
    {

    }
    @Override
    public void onApiRequestComplete(String key, final String result) throws IOException
    {
        if (key.equals("countries"))
        {
            setCountriesAdapter(result);
        }
        else if (key.contains("getCollectionpointByDay"))
        {
            setCollectionPointAdapter(result);
        }
        else if(key.contains("states-by-country"))
        {
            setStatesAdapter(result);
        }

        else if (key.contains("addharNoExists"))
        {
            try {
                setAdharCardCheckAdapter(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else if(key.contains("districts-by-state"))
        {
            setDistrictsAdapter(result);
        }

        else if(key.contains("PoliceStation-by-district"))
        {
            setPoliceStationsAdapter(result);
        }

        else if(key.contains("municipalities-by-district"))
        {
            setMunicipalitiesAdapter(result);
        }

        else if(key.contains("wards-by-municipality"))
        {
            setWardsAdapter(result);
        }

        else if(key.contains("blocks-by-district"))
        {
            setBlocksAdapter(result);
        }

        else if(key.contains("panchayats-by-block"))
        {
            setGramPanchayetsAdapter(result);
        }

        else if(key.contains("villages-by-panchayat"))
        {
            setVillagesAdapter(result);
        }

        else if(key.contains("all-qualifications"))
        {
            setQualificationsAdapter(result);
        }

        else if(key.contains("all-occupations"))
        {
            setOccupationsAdapter(result);
        }

        else if(key.contains("all-documents"))
        {
            setDocumentsAdapter(result);
        }
        else if(key.contains("uploadPassbookFile2"))
        {
            setAdapterImageResult(result);
        }

        else if(key.contains("edit-member"))
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberEditActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("Member Edited Successfully");
            builder1.setCancelable(true);
            builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface1, int i)
                {
                    Intent intent = new Intent(getApplicationContext(),MemberActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
        }

        else if(key.contains("member") && !key.contains("member/add")&& !key.contains("member/edit"))
        {
            try {
                setMemberEditAdapter(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (key.equals("uploadPassbookFile"))
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberEditActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("Image successfully uploaded");
            builder1.setCancelable(true);
            builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface1, int i)
                {
                    imageSaveFile = result.substring(1,result.length()-1);
                    memberImageTickButton.setVisibility(View.VISIBLE);
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
        }
        else if (key.equals("uploadPassbookFile3"))
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberEditActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("Signature successfully uploaded");
            builder1.setCancelable(true);
            builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface1, int i)
                {
                    signatureSaveFile = result.substring(1,result.length()-1);
                    memberSignatureTickButton.setVisibility(View.VISIBLE);
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
        }
    }

    private void setAdapterImageResult(final String result)
    {
        if(result!=null&& result.length()>4)
        {

            final AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberEditActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("Image successfully uploaded");
            builder1.setCancelable(true);
            builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface1, int i)
                {

                    for(int n=0;n<memberKycDocumentList.size();n++)
                    {
                        if(n==Integer.parseInt(result.substring(0,1)))
                        {
                            memberKycDocumentList.get(n).setKycdocumentName(result.substring(2,result.length()-1));
                        }
                    }
                    if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
                    {
                        memberDocumentAdapterMobileVersion.notifyDataSetChanged();
                    }
                    else
                    {
                        mdAdapter.notifyDataSetChanged();
                    }
                    //mdAdapter.notifyDataSetChanged();

//                saveFileName = result.substring(1,result.length()-1);
//                tickImageButton.setVisibility(View.VISIBLE);
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
        }
        else
        {

        }

    }

    //---Put all values for member edit---//
    private void setMemberEditAdapter(String result) throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonObject1 = jsonObject.getJSONObject("memberAddres");
            JSONArray jsonArray = jsonObject.getJSONArray("memberKycdocuments");
            //mem_id = jsonObject.getString("id");
            id = jsonObject.getString("id");
            code = jsonObject.getString("code");
            firstName = jsonObject.getString("firstName");
            middleName = jsonObject.getString("middleName");
            lastName = jsonObject.getString("lastName");
            guardianName = jsonObject.getString("guardianName");
            isFather = jsonObject.optBoolean("isFather");
            memberQualificationId = jsonObject.getString("qualificationId");
            maritalStatus = jsonObject.getString("maritalStatus");
            dateOfBirth = jsonObject.getString("dateOfBirth");
            occupationId = jsonObject.getString("occupationId");
            gender = jsonObject.getString("gender");
            religion = jsonObject.getString("religion");
            caste = jsonObject.getString("caste");
            motherFirstName = jsonObject.getString("motherFirstName");
            motherMiddleName = jsonObject.getString("motherMiddleName");
            motherLastName = jsonObject.getString("motherLastName");
            fullName = jsonObject.getString("fullName");
            collectionPointId = jsonObject.getString("collectionPointId");
            collectionPointName = jsonObject.getString("collectionPointName");
            mobileNo = jsonObject.getString("mobileNo");
            scanPicture = jsonObject.getString("scanPicture");
            scanSignature = jsonObject.getString("scanSignature");
            isBorrower = jsonObject.optBoolean("isBorrower");
            isCoBorrower = jsonObject.optBoolean("isCoBorrower");
            collectionDay = jsonObject.optInt("collectionDay");
            aadharNo = jsonObject.optLong("aadharNo");
            visitStatus = jsonObject.getString("visitStatus");
            remarks = jsonObject.getString("remarks");
            isDead = jsonObject.getBoolean("isDead");
            dateOfDeath = jsonObject.getString("dateOfDeath");
            memberAccessCode = jsonObject.getString("memberAccessCode");
            branchCode = jsonObject.getString("branchCode");

            memberAddressId = jsonObject1.getString("id");
            houseNo = jsonObject1.getString("houseNo");
            streetName = jsonObject1.getString("streetName");
            countryId = jsonObject1.getString("countryId");
            memberStateId = jsonObject1.getString("stateId");
            memberDistrictId = jsonObject1.getString("districtId");
            memberBlockId = jsonObject1.getString("blockId");
            memberGramPanchayatId = jsonObject1.getString("gramPanchayatId");
            memberMunicipalityId = jsonObject1.getString("municipalityId");
            memberWardId = jsonObject1.getString("wardId");
            memberPoliceStationId = jsonObject1.getString("policeStationId");
            postOffice = jsonObject1.getString("postOffice");
            wardNo = jsonObject1.getString("wardNo");
            pinCode = jsonObject1.getString("pinCode");
            isDefault = jsonObject1.optBoolean("isDefault");

            imageSaveFile = jsonObject.getString("scanPicture");
            signatureSaveFile = jsonObject.getString("scanSignature");


            SimpleDateFormat dateFormatEdit = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = dateFormat.parse(form_Date);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MM-dd-yyyy");


            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject2 = (JSONObject)jsonArray.get(i);
                MemberKycDocument memberKycDocument = new MemberKycDocument(
                        jsonObject2.getString("id"),
                        jsonObject2.getString("memberId"),
                        jsonObject2.getString("kycdocumentId"),
                        jsonObject2.getString("documentType"),
                        jsonObject2.getString("kycdocumentNo"),
                        jsonObject2.getString("kycdocumentScan"),
                        jsonObject2.getString("kycdocumentName"),
                        (jsonObject2.getString("issueDate") != null && !jsonObject2.getString("issueDate").equals("null"))?fmtOut.format(dateFormatEdit.parse(jsonObject2.getString("issueDate"))):jsonObject2.getString("issueDate"),
                        (jsonObject2.getString("expiryDate")!= null && !jsonObject2.getString("expiryDate").equals("null"))?fmtOut.format(dateFormatEdit.parse(jsonObject2.getString("expiryDate"))):jsonObject2.getString("expiryDate"),
                        jsonObject2.getString("remarks"),
                        null,
                        null,
                        null,
                        documentsList
                );
                memberKycDocumentList.add(memberKycDocument);
            }


            if(memberKycDocumentList.size()==0)
            {
                MemberKycDocument memberKycDocument1 = new MemberKycDocument(
                        null,null,null,null,null,null,
                        null,null,null,null,null,null,null,documentsList
                );
                memberKycDocumentList.add(memberKycDocument1);
            }


            //---Integration---//
            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
            {
                memberDocumentAdapterMobileVersion.notifyDataSetChanged();
            }
            else
            {
                mdAdapter.notifyDataSetChanged();
            }
            //mdAdapter.notifyDataSetChanged();

            if(imageSaveFile != null && !imageSaveFile.equals("null"))
            {
                String passbookFileName = jsonObject.getString("scanPicture");
                int loader = R.drawable.dummy_upload_img;
                String image_url = GlobalImageSettings.API_BASE_URL.getKey() + passbookFileName;
                ImageLoader imgLoader = new ImageLoader(getApplicationContext());

                imgLoader.DisplayImage(image_url, loader, memberImageView);
                memberImageTickButton.setVisibility(View.VISIBLE);
            }

            if(signatureSaveFile!=null && !signatureSaveFile.equals("null"))
            {
                String passbookFileName2 = jsonObject.getString("scanSignature");
                int loader2 = R.drawable.dummy_upload_img;
                String image_url2 = GlobalImageSettings.API_BASE_URL.getKey() + passbookFileName2;
                ImageLoader imgLoader2 = new ImageLoader(getApplicationContext());

                imgLoader2.DisplayImage(image_url2, loader2, memberSignatureView);
                memberSignatureTickButton.setVisibility(View.VISIBLE);
            }







        } catch (JSONException e) {
            e.printStackTrace();
        }
        Date date = dateFormat.parse(dateOfBirth);
        SimpleDateFormat fmtOut = new SimpleDateFormat("MM-dd-yyyy");
        //return fmtOut.format(date);
        memberApplicationNumberField.setText(code);
        memberAdharNumberField.setText(String.valueOf(aadharNo));
        memberFirstNameField.setText(firstName);
        if ((middleName != null && !middleName.equals("NULL") && !middleName.equals("null"))) {
            memberMiddleNameField.setText(middleName);
        } else {
            memberMiddleNameField.setText("");
        }
        memberLastNameField.setText(lastName);
        memberDobField.setText(fmtOut.format(date));
        fatherCheckField.setChecked(isFather);
        coBorrowerCheckField.setChecked(isCoBorrower);
        borrowerCheckFiled.setChecked(isBorrower);
        if ((guardianName != null && !guardianName.equals("NULL") && !guardianName.equals("null"))) {
            memberGurdainNameField.setText(guardianName);
        } else {
            memberGurdainNameField.setText("");
        }

        if ((mobileNo != null && !mobileNo.equals("NULL") && !mobileNo.equals("null"))) {
            memberMobileField.setText(mobileNo);
        } else {
            memberMobileField.setText("");
        }

        if ((houseNo != null && !houseNo.equals("NULL") && !houseNo.equals("null"))) {
            memberHouseField.setText(houseNo);
        } else {
            memberHouseField.setText("");
        }

        if ((postOffice != null && !postOffice.equals("NULL") && !postOffice.equals("null"))) {
            memberPostOfficeField.setText(postOffice);
        } else {
            memberPostOfficeField.setText("");
        }

        if ((streetName != null && !streetName.equals("NULL") && !streetName.equals("null"))) {
            memberStreetField.setText(streetName);
        } else {
            memberStreetField.setText("");
        }

        if ((wardNo != null && !wardNo.equals("NULL") && !wardNo.equals("null"))) {
            memberWardField.setText(wardNo);
        } else {
            memberWardField.setText("");
        }

        if ((remarks != null && !remarks.equals("NULL") && !remarks.equals("null"))) {
            memberGurdainField.setText(remarks);
        } else {
            memberGurdainField.setText("");
        }

        if ((motherMiddleName != null && !motherMiddleName.equals("NULL") && !motherMiddleName.equals("null"))) {
            memberMotherMiddleNameField.setText(motherMiddleName);
        } else {
            memberMotherMiddleNameField.setText("");
        }

        if ((motherFirstName != null && !motherFirstName.equals("NULL") && !motherFirstName.equals("null"))) {
            memberMotherFirstNameField.setText(motherFirstName);
        } else {
            memberMotherFirstNameField.setText("");
        }

        if ((motherLastName != null && !motherLastName.equals("NULL") && !motherLastName.equals("null"))) {
            memberMotherLastNameField.setText(motherLastName);
        } else {
            memberMotherLastNameField.setText("");
        }

        memberPincodeField.setText(pinCode);

        castSpinner.setSelection(((ArrayAdapter<String>)castSpinner.getAdapter()).getPosition(caste));
        maritalStatusSpinner.setSelection(((ArrayAdapter<String>)maritalStatusSpinner.getAdapter()).getPosition(maritalStatus));
        religionSpinner.setSelection(((ArrayAdapter<String>)religionSpinner.getAdapter()).getPosition(religion));
        collectionDaySpinner.setSelection(collectionDay);
        visitStatusSpinner.setSelection(((ArrayAdapter<String>)visitStatusSpinner.getAdapter()).getPosition(visitStatus));
        genderSpinner.setSelection(((ArrayAdapter<String>)genderSpinner.getAdapter()).getPosition(gender));





    }

    private void setCollectionPointAdapter(String result)
    {
        try {
            collectionPointList = new ArrayList<CollPoint>();
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                CollPoint collPoint = new CollPoint(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                collectionPointList.add(collPoint);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerCollectionPointArray = new String[collectionPointList.size()];
        for (int i = 0; i < collectionPointList.size(); i++)
        {
            spinnerCollectionPointNamesMap.put(collectionPointList.get(i).getId(),collectionPointList.get(i).getName());
            spinnerCollectionPointArray[i] = collectionPointList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerCollectionPointArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        collectionPointNameSpinner.setAdapter(adapter);

    }
    //---End of put values---//

    private void setQualificationsAdapter(String result)
    {
        try {
            JSONArray jsonArray = new JSONArray(result);
            Qualifications qualifications1 = new Qualifications(emptyGuid,"Select Qualification");
            qualificationsList.add(qualifications1);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Qualifications qualifications = new Qualifications(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                qualificationsList.add(qualifications);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerQualificationsArray = new String[qualificationsList.size()];
        for (int i = 0; i < qualificationsList.size(); i++)
        {
            spinnerQualificationsMap.put(qualificationsList.get(i).getId(),qualificationsList.get(i).getName());
            spinnerQualificationsArray[i] = qualificationsList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerQualificationsArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        qualificationSpinner.setAdapter(adapter);
    }

    private void setOccupationsAdapter(String result)
    {
        try {
            JSONArray jsonArray = new JSONArray(result);
            Occupation occupation1 = new Occupation(emptyGuid,"Select Occupation");
            occupationList.add(occupation1);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Occupation occupation = new Occupation(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                occupationList.add(occupation);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerOccupationArray = new String[occupationList.size()];
        for (int i = 0; i < occupationList.size(); i++)
        {
            spinnerOccupationMap.put(occupationList.get(i).getId(),occupationList.get(i).getName());
            spinnerOccupationArray[i] = occupationList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerOccupationArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        occupationSpinner.setAdapter(adapter);
    }


    private void setCountriesAdapter(String result)
    {
        try {
            JSONArray jsonArray = new JSONArray(result);

            Country country1 = new Country(emptyGuid,"Select Country");
            countryLists.add(country1);

            for (int i = 0; i < jsonArray.length(); i++) {
                //JSONObject jsonObject = (JSONObject)jsonArray.get(i);

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Country country = new Country(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                countryLists.add(country);



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerCountriesArray = new String[countryLists.size()];
        for (int i = 0; i < countryLists.size(); i++)
        {
            spinnerCountriesMap.put(countryLists.get(i).getId(),countryLists.get(i).getName());
            spinnerCountriesArray[i] = countryLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerCountriesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        countriesSpinner.setAdapter(adapter);
    }
    private void setStatesAdapter(String result)
    {
        try {
            statesLists = new ArrayList<State>();
            JSONArray jsonArray = new JSONArray(result);
            State state1 = new State(emptyGuid,"Select State");
            statesLists.add(state1);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                State state = new State(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                statesLists.add(state);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerStatesArray = new String[statesLists.size()];
        for (int i = 0; i < statesLists.size(); i++)
        {
            spinnerStatesMap.put(statesLists.get(i).getId(),statesLists.get(i).getName());
            spinnerStatesArray[i] = statesLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerStatesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        statesSpinner.setAdapter(adapter);
    }

    private void setDistrictsAdapter(String result)
    {
        try {
            districtList = new ArrayList<District>();
            JSONArray jsonArray = new JSONArray(result);
            District district1 = new District(emptyGuid,"Select District");
            districtList.add(district1);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                District district = new District(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                districtList.add(district);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerDistrictsArray = new String[districtList.size()];
        for (int i = 0; i < districtList.size(); i++)
        {
            spinnerDistrictsMap.put(districtList.get(i).getId(),districtList.get(i).getName());
            spinnerDistrictsArray[i] = districtList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerDistrictsArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        distrcitSpinner.setAdapter(adapter);
    }

    private void setPoliceStationsAdapter(String result)
    {
        try {
            policeStationList = new ArrayList<PoliceStation>();
            JSONArray jsonArray = new JSONArray(result);
            PoliceStation policeStation1 = new PoliceStation(emptyGuid,"Select Police Station");
            policeStationList.add(policeStation1);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                PoliceStation policeStation = new PoliceStation(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                policeStationList.add(policeStation);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerPoliceArray = new String[policeStationList.size()];
        for (int i = 0; i < policeStationList.size(); i++)
        {
            spinnerPoliceMap.put(policeStationList.get(i).getId(),policeStationList.get(i).getName());
            spinnerPoliceArray[i] = policeStationList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerPoliceArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        policeStationSpinner.setAdapter(adapter);
    }


    private void setMunicipalitiesAdapter(String result)
    {
        try {
            municipalityList = new ArrayList<Municipality>();
            JSONArray jsonArray = new JSONArray(result);
            Municipality municipality1 = new Municipality(emptyGuid,"Select Municipality");
            municipalityList.add(municipality1);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Municipality municipality = new Municipality(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                municipalityList.add(municipality);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerMunicipalArray = new String[municipalityList.size()];
        for (int i = 0; i < municipalityList.size(); i++)
        {
            spinnerMunicipalitiesMap.put(municipalityList.get(i).getId(),municipalityList.get(i).getName());
            spinnerMunicipalArray[i] = municipalityList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerMunicipalArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        municipalitySpinner.setAdapter(adapter);
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

    private void setWardsAdapter(String result)
    {
        try {
            wardList = new ArrayList<Ward>();
            JSONArray jsonArray = new JSONArray(result);
            Ward ward1 = new Ward(emptyGuid,"Select Ward");
            wardList.add(ward1);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Ward ward = new Ward(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                wardList.add(ward);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerWardsArray = new String[wardList.size()];
        for (int i = 0; i < wardList.size(); i++)
        {
            spinnerWardsMap.put(wardList.get(i).getId(),wardList.get(i).getName());
            spinnerWardsArray[i] = wardList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerWardsArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        wardSpinner.setAdapter(adapter);
    }

    private void setBlocksAdapter(String result)
    {
        try {
            blockList = new ArrayList<Block>();
            JSONArray jsonArray = new JSONArray(result);
            Block block1 = new Block(emptyGuid,"Select Block");
            blockList.add(block1);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Block block = new Block(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                blockList.add(block);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerBlocksArray = new String[blockList.size()];
        for (int i = 0; i < blockList.size(); i++)
        {
            spinnerBlocksMap.put(blockList.get(i).getId(),blockList.get(i).getName());
            spinnerBlocksArray[i] = blockList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerBlocksArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        blockSpinner.setAdapter(adapter);
    }

    private void setGramPanchayetsAdapter(String result)
    {
        try {
            gramPanchayetList = new ArrayList<GramPanchayet>();
            JSONArray jsonArray = new JSONArray(result);
            GramPanchayet gramPanchayet1 = new GramPanchayet(emptyGuid,"Select Gram Panchayets");
            gramPanchayetList.add(gramPanchayet1);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                GramPanchayet gramPanchayet = new GramPanchayet(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                gramPanchayetList.add(gramPanchayet);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerPanchayetsArray = new String[gramPanchayetList.size()];
        for (int i = 0; i < gramPanchayetList.size(); i++)
        {
            spinnerGramPanchayetMap.put(gramPanchayetList.get(i).getId(),gramPanchayetList.get(i).getName());
            spinnerPanchayetsArray[i] = gramPanchayetList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerPanchayetsArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        gramPanchayetSpinner.setAdapter(adapter);
    }

    private void setVillagesAdapter(String result)
    {
        try {
            villageList = new ArrayList<Village>();
            JSONArray jsonArray = new JSONArray(result);
            Village village1 = new Village(emptyGuid,"Select Village");
            villageList.add(village1);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Village village = new Village(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                villageList.add(village);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerVillagesArray = new String[villageList.size()];
        for (int i = 0; i < villageList.size(); i++)
        {
            spinnerVillageMap.put(villageList.get(i).getId(),villageList.get(i).getName());
            spinnerVillagesArray[i] = villageList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerVillagesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        villageSpinner.setAdapter(adapter);
    }

    private void setDocumentsAdapter(String result)
    {
        try {
            documentsList = new ArrayList<Documents>();
            JSONArray jsonArray = new JSONArray(result);
            Documents documents1 = new Documents(emptyGuid,"Select ID Type");
            documentsList.add(documents1);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Documents documents = new Documents(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                documentsList.add(documents);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


//
//
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberEditActivity.this, android.R.layout.simple_spinner_item,spinnerDocumentsArray);
//        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
//        idTypeSpinner.setAdapter(adapter);
    }

    private void SelectImage()
    {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberEditActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(items[i].equals("Camera"))
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,SELECT_FILE_IMAGE);
                }
                else if(items[i].equals("Gallery"))
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent,SELECT_FILE_IMAGE);
                }
                else if(items[i].equals("Cancel"))
                {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void SelectSignature()
    {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberEditActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(items[i].equals("Camera"))
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,SELECT_FILE_SIGNATURE);
                }
                else if(items[i].equals("Gallery"))
                {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent,SELECT_FILE_SIGNATURE);
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
//        ivImage = (ImageView) findViewById(R.id.passBookImage);
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == CROP_PIC)
            {
                Bundle bundle = data.getExtras();
                bitmapImage = bundle.getParcelable("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
                byte[] b = baos.toByteArray();
                jsonImage = Base64.encodeToString(b, Base64.DEFAULT);
                memberImageView.setImageBitmap(bitmapImage);
                uri =  getImageUri(getApplicationContext(),bitmapImage);
                imageImage = getFileName(uri);
                memberImageTickButton.setVisibility(View.GONE);

            }
            else if (requestCode == CROP_PIC_SIGNATURE)
            {
                Bundle bundle = data.getExtras();
                bitmapSignature = bundle.getParcelable("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapSignature.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
                byte[] b = baos.toByteArray();
                jsonSignature = Base64.encodeToString(b, Base64.DEFAULT);
                memberSignatureView.setImageBitmap(bitmapSignature);
                uri =  getImageUri(getApplicationContext(),bitmapSignature);
                imageSignature = getFileName(uri);
                memberSignatureTickButton.setVisibility(View.GONE);
            }

            else if (requestCode == CROP_PIC_DOCUMENT)
            {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , baos);
                byte[] b = baos.toByteArray();
                json = Base64.encodeToString(b, Base64.DEFAULT);
                uri =  getImageUri(getApplicationContext(),bitmap);
                image = getFileName(uri);
                updateMemberDocumentList(bitmap,json,image,position);
            }

            else if(Integer.parseInt(Integer.toString(requestCode).substring(0, 1)) == SELECT_FILE)
            {
                selectedImageUri = data.getData();
                position = Integer.parseInt(Integer.toString(requestCode).substring(1));
                performCropDocument();

            }
            else if(requestCode == SELECT_FILE_IMAGE)
            {
                selectedImageUri = data.getData();
                performCrop();
            }
            else if(requestCode == SELECT_FILE_SIGNATURE)
            {
                selectedImageUri = data.getData();
                performCropSignature();
            }
        }
    }

    private void performCropDocument() {
        try
        {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(selectedImageUri, "image/*");



            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 265);
            cropIntent.putExtra("outputY", 265);
            cropIntent.putExtra("return-data", true);
//            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            startActivityForResult(cropIntent, CROP_PIC_DOCUMENT);
        }
        catch(ActivityNotFoundException anfe)
        {
            anfe.printStackTrace();
        }
    }

    private void performCropSignature() {
        try
        {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(selectedImageUri, "image/*");



            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 265);
            cropIntent.putExtra("outputY", 265);
            cropIntent.putExtra("return-data", true);
//            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            startActivityForResult(cropIntent, CROP_PIC_SIGNATURE);
        }
        catch(ActivityNotFoundException anfe)
        {
            anfe.printStackTrace();
        }
    }

    private void performCrop()
    {
        try
        {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(selectedImageUri, "image/*");



            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 265);
            cropIntent.putExtra("outputY", 265);
            cropIntent.putExtra("return-data", true);
//            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            startActivityForResult(cropIntent, CROP_PIC);
        }
        catch(ActivityNotFoundException anfe)
        {
            anfe.printStackTrace();
        }
    }

//    public void removeDocument(Integer position)
//    {
//        if((memberKycDocumentList.size()-1)>=position)
//        {
//            memberKycDocumentList.remove(position);
//        }
//
//        mdAdapter.notifyDataSetChanged();
//
//    }

    public void updateMemberDocumentList(Bitmap bitmap1,String json,String image,Integer position)
    {
        for(int i=0;i<memberKycDocumentList.size();i++)
        {
            if(i==position)
            {
                memberKycDocumentList.get(i).setBitmap(bitmap1);
                memberKycDocumentList.get(i).setImage(image);
                memberKycDocumentList.get(i).setJson(json);
                memberKycDocumentList.get(i).setKycdocumentName(null);
            }
        }
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            memberDocumentAdapterMobileVersion.notifyDataSetChanged();
        }
        else
        {
            mdAdapter.notifyDataSetChanged();
        }
        //mdAdapter.notifyDataSetChanged();

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
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
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
