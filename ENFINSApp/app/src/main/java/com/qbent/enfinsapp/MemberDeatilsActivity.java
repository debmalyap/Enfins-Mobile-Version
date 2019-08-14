package com.qbent.enfinsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.TextView;

import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Block;
import com.qbent.enfinsapp.model.CollPoint;
import com.qbent.enfinsapp.model.Country;
import com.qbent.enfinsapp.model.DataAttributes;
import com.qbent.enfinsapp.model.District;
import com.qbent.enfinsapp.model.GramPanchayet;
import com.qbent.enfinsapp.model.Municipality;
import com.qbent.enfinsapp.model.PoliceStation;
import com.qbent.enfinsapp.model.State;
import com.qbent.enfinsapp.model.Village;
import com.qbent.enfinsapp.model.Ward;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;


public class MemberDeatilsActivity extends MainActivity implements ApiCallback {

    final AlertDialogue alertDialogue = new AlertDialogue(MemberDeatilsActivity.this);
    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    String aadhar,name,gender,yearOfBirth,dateOfBirth,careOf,villageTehsil,post,district,state,postCode,firstName,middleName,lastName,house,landmark,street,location,gName;
    EditText uid,fName,mName,lName,dob,guardian,postOffice,pinCode,mobileNo,houseNo,streetName,wardNo,remarks,memberCode,fullAddress;

    Button backButton;
    Button saveButton;

    Spinner collectionDays;
    Spinner countriesSpinner;
    Spinner statesSpinner;
    Spinner distrcitSpinner;
    Spinner municipalitySpinner;
    Spinner wardSpinner;
    Spinner blockSpinner;
    Spinner gramPanchayetSpinner;
    Spinner villageSpinner;
    Spinner collectionPointSpinner;
    Spinner genderSpinner;
    Spinner policeStationSpinner;
    Spinner visitStatusSpinner;

    private List<Country> countryLists;
    private List<State> statesLists;
    private List<District> districtList;
    private List<Municipality> municipalityList;
    private List<Ward> wardList;
    private List<Block> blockList;
    private List<GramPanchayet> gramPanchayetList;
    private List<Village> villageList;
    private List<PoliceStation> policeStationList;
    private List<CollPoint> collectionPointList;

    private String stateId = " ";
    private String districtId = " ";
    private String municipalityId = " ";
    private String wardId = " ";
    private String blockId = " ";
    private String gramPanchayatId = " ";
    private String villageId = " ";
    private String policeStationId= " ";
    private String memberId;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    HashMap<String, String> spinnerCountriesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerStatesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerDistrictsMap = new HashMap<String, String>();
    HashMap<String, String> spinnerMunicipalitiesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerWardsMap = new HashMap<String, String>();
    HashMap<String, String> spinnerBlocksMap = new HashMap<String, String>();
    HashMap<String, String> spinnerGramPanchayetMap = new HashMap<String, String>();
    HashMap<String, String> spinnerVillageMap = new HashMap<String, String>();
    HashMap<String, String> collectionPointMap = new HashMap<String, String>();
    HashMap<String, String> policeStationMap = new HashMap<String, String>();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_member_details_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_member);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_member_deatils, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_member);
        }

        final AlertDialogue alertDialogue = new AlertDialogue(MemberDeatilsActivity.this);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");

        memberCode = (EditText)findViewById(R.id.editTextMemberCode);
        memberCode.setEnabled(false);
        memberCode.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        uid = (EditText)findViewById(R.id.editTextMemberAadhar);
        uid.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        fName = (EditText)findViewById(R.id.editTextMemberFirstName);
        fName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        mName = (EditText)findViewById(R.id.editTextMemberMiddleName);
        mName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        lName = (EditText)findViewById(R.id.editTextMemberLastName);
        lName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        guardian = (EditText)findViewById(R.id.editTextMemberGuardianName);
        guardian.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        dob = (EditText)findViewById(R.id.editTextMemberBirthDate);
        postOffice = (EditText)findViewById(R.id.editTextMemberPostOffice);
        postOffice.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        pinCode = (EditText)findViewById(R.id.editTextMemberPincode);
        mobileNo = (EditText)findViewById(R.id.editMemberTextMobile);
        houseNo = (EditText)findViewById(R.id.editTextMemberHouseNo);
        houseNo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        streetName = (EditText)findViewById(R.id.editTextMemberStreetName);
        streetName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        wardNo = (EditText)findViewById(R.id.editTextMemberWard);
        wardNo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        remarks = (EditText)findViewById(R.id.editTextMemberRemarks);

        collectionDays = (Spinner) findViewById(R.id.spinnerMemberCollectionDay);
        countriesSpinner = (Spinner) findViewById(R.id.spinnerMemberCountry);
        statesSpinner = (Spinner) findViewById(R.id.spinnerMemberState);
        distrcitSpinner = (Spinner) findViewById(R.id.spinnerMemberDistricts);
        municipalitySpinner = (Spinner) findViewById(R.id.spinnerMemberMunicipal);
        wardSpinner = (Spinner) findViewById(R.id.spinnerMemberWards);
        blockSpinner = (Spinner) findViewById(R.id.spinnerMemberBlocks);
        gramPanchayetSpinner = (Spinner) findViewById(R.id.spinnerMemberPanchayets);
        villageSpinner = (Spinner) findViewById(R.id.spinnerMemberVillages);
        collectionPointSpinner = (Spinner) findViewById(R.id.spinnerMemberCollectionPointName);
        genderSpinner = (Spinner) findViewById(R.id.spinnerMemberGender);
        policeStationSpinner = (Spinner) findViewById(R.id.spinnerMemberPolice);
        visitStatusSpinner = (Spinner) findViewById(R.id.spinnerMemberVisitStatus);
        fullAddress = (EditText) findViewById(R.id.memberFullAddress);
        fullAddress.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        backButton = (Button) findViewById(R.id.backMemberButton);
        saveButton = (Button) findViewById(R.id.submitMemberButton);

        fab.setVisibility(View.GONE);

        countryLists = new ArrayList<Country>();
        statesLists = new ArrayList<State>();
        districtList = new ArrayList<District>();
        municipalityList = new ArrayList<Municipality>();
        wardList = new ArrayList<Ward>();
        blockList = new ArrayList<Block>();
        gramPanchayetList = new ArrayList<GramPanchayet>();
        villageList = new ArrayList<Village>();
        policeStationList = new ArrayList<PoliceStation>();
        collectionPointList = new ArrayList<CollPoint>();

        uid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        uid.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (getCurrentFocus() == uid)
                {
                    checkAdharCardStatus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        memberCode.setEnabled(false);

        memberId = getIntent().getStringExtra("emp_id");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent memberIntent = new Intent(getApplicationContext(),MemberActivity.class);
                startActivity(memberIntent);
            }
        });

        dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(false);
                return false;
            }
        });
        fName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        mName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        lName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        guardian.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        mobileNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        remarks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        houseNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        streetName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        postOffice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        pinCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        wardNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });



        dob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MemberDeatilsActivity.this,
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
                dob.setText(date);
            }
        };

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(collectionPointMap.size()==0||(new ArrayList<String>(collectionPointMap.keySet())).get(new ArrayList<String>(collectionPointMap.values())
                        .indexOf(collectionPointSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {
                    alertDialogue.showAlertMessage("Please select a collection point name");
                    setSpinnerError(collectionPointSpinner,"Select Collection Point Name");
                    collectionPointSpinner.setFocusable(true);
                    collectionPointSpinner.requestFocus();
                    return;
                }
                if(fName.getText().toString().length()==0)
                {
                    fName.setError("FIRST NAME CANNOT BE EMPTY");
                    fName.setFocusableInTouchMode(true);
                    return;
                }
                if(lName.getText().toString().length()==0)
                {
                    lName.setFocusableInTouchMode(true);
                    lName.setError("LAST NAME CANNOT BE EMPTY");
                    return;
                }
                if(guardian.getText().toString().length()==0)
                {
                    guardian.setFocusableInTouchMode(true);
                    guardian.setError("GUARDIAN NAME CANNOT BE EMPTY");
                    return;
                }
                if(dob.getText().toString().length()==0)
                {
                    dob.setFocusableInTouchMode(true);
                    dob.setError("DATE OF BIRTH CANNOT BE EMPTY");
                    return;
                }
                if(uid.getText().toString().length()==0)
                {
                    uid.setFocusableInTouchMode(true);
                    uid.setError("AADHAAR NUMBER CANNOT BE EMPTY");
                    return;
                }
                if(mobileNo.getText().toString().length()==0)
                {
                    mobileNo.setFocusableInTouchMode(true);
                    mobileNo.setError("MOBILE NUMBER CANNOT BE EMPTY");
                    return;
                }
                if((new ArrayList<String>(policeStationMap.keySet())).get(new ArrayList<String>(policeStationMap.values()).indexOf(policeStationSpinner.getSelectedItem().toString())).toString().equals(emptyGuid))
                {
                    alertDialogue.showAlertMessage("Please select police station");
                    setSpinnerError(policeStationSpinner,"Please select police station");
                    policeStationSpinner.setFocusable(true);
                    policeStationSpinner.requestFocus();
                    return;
                }
                if(postOffice.getText().toString().length()==0)
                {
                    postOffice.setFocusableInTouchMode(true);
                    postOffice.setError("POST OFFICE CANNOT BE EMPTY");
                    return;
                }
                if(pinCode.getText().toString().length()==0)
                {
                    pinCode.setFocusableInTouchMode(true);
                    pinCode.setError("PIN CODE CANNOT BE EMPTY");
                    return;
                }
                if(!visitStatusSpinner.getSelectedItem().toString().equals("Interested")  && remarks.getText().toString().length()==0)
                {
                    remarks.setFocusableInTouchMode(true);
                    remarks.setError("REMARKS CANNOT BE EMPTY");
                    return;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(MemberDeatilsActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to add member?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k)
                    {
                        ApiRequest apiRequest = new ApiRequest("add-member");
                        try{
                            JSONObject jsonObject = new JSONObject();
                            JSONObject addressObject = new JSONObject();

                            jsonObject.put("firstName",(!fName.getText().toString().isEmpty())?fName.getText().toString():null);
                            jsonObject.put("middleName",(!mName.getText().toString().isEmpty())?mName.getText().toString():null);
                            jsonObject.put("lastName",(!lName.getText().toString().isEmpty())?lName.getText().toString():null);
                            jsonObject.put("guardianName",(!guardian.getText().toString().isEmpty())?guardian.getText().toString():null);
                            jsonObject.put("dateOfBirth",(!dob.getText().toString().isEmpty())?dob.getText().toString():null);
                            jsonObject.put("collectionDay",collectionDays.getSelectedItemPosition());
                            jsonObject.put("gender",genderSpinner.getSelectedItem().toString());
                            jsonObject.put("visitStatus",visitStatusSpinner.getSelectedItem().toString());
                            jsonObject.put("mobileNo",(!mobileNo.getText().toString().isEmpty())?mobileNo.getText().toString():null);
                            jsonObject.put("aadharNo",(!uid.getText().toString().isEmpty())?uid.getText().toString():null);
                            jsonObject.put("remarks",(!remarks.getText().toString().isEmpty())?remarks.getText().toString():null);
                            jsonObject.put("collectionPointId",(new ArrayList<String>(collectionPointMap.keySet())).get(new ArrayList<String>(collectionPointMap.values()).indexOf(collectionPointSpinner.getSelectedItem().toString())).toString());
                            //jsonObject.accumulate("memberAddres");

                            addressObject.put("houseNo",(!houseNo.getText().toString().isEmpty())?houseNo.getText().toString():null);
                            addressObject.put("streetName",(!streetName.getText().toString().isEmpty())?streetName.getText().toString():null);
                            addressObject.put("postOffice",(!postOffice.getText().toString().isEmpty())?postOffice.getText().toString():null);
                            addressObject.put("wardNo",(!wardNo.getText().toString().isEmpty())?wardNo.getText().toString():null);
                            addressObject.put("pinCode",(!pinCode.getText().toString().isEmpty())?pinCode.getText().toString():null);
                            addressObject.put("policeStationId",(new ArrayList<String>(policeStationMap.keySet())).get(new ArrayList<String>(policeStationMap.values()).indexOf(policeStationSpinner.getSelectedItem().toString())).toString());
                            addressObject.put("countryId",!(new ArrayList<String>(spinnerCountriesMap.keySet())).get(new ArrayList<String>(spinnerCountriesMap.values()).indexOf(countriesSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerCountriesMap.keySet())).get(new ArrayList<String>(spinnerCountriesMap.values()).indexOf(countriesSpinner.getSelectedItem().toString())).toString():null);
                            addressObject.put("stateId",!(new ArrayList<String>(spinnerStatesMap.keySet())).get(new ArrayList<String>(spinnerStatesMap.values()).indexOf(statesSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerStatesMap.keySet())).get(new ArrayList<String>(spinnerStatesMap.values()).indexOf(statesSpinner.getSelectedItem().toString())).toString():null);
                            addressObject.put("districtId",!(new ArrayList<String>(spinnerDistrictsMap.keySet())).get(new ArrayList<String>(spinnerDistrictsMap.values()).indexOf(distrcitSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerDistrictsMap.keySet())).get(new ArrayList<String>(spinnerDistrictsMap.values()).indexOf(distrcitSpinner.getSelectedItem().toString())).toString():null);
                            addressObject.put("municipalityId",(spinnerMunicipalitiesMap.size() != 0)?!(new ArrayList<String>(spinnerMunicipalitiesMap.keySet())).get(new ArrayList<String>(spinnerMunicipalitiesMap.values()).indexOf(municipalitySpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerMunicipalitiesMap.keySet())).get(new ArrayList<String>(spinnerMunicipalitiesMap.values()).indexOf(municipalitySpinner.getSelectedItem().toString())).toString():null:null);
                            addressObject.put("wardId",(spinnerWardsMap.size() != 0)?!(new ArrayList<String>(spinnerWardsMap.keySet())).get(new ArrayList<String>(spinnerWardsMap.values()).indexOf(wardSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerWardsMap.keySet())).get(new ArrayList<String>(spinnerWardsMap.values()).indexOf(wardSpinner.getSelectedItem().toString())).toString():null:null);
                            addressObject.put("villageId",(spinnerVillageMap.size() != 0)?!(new ArrayList<String>(spinnerVillageMap.keySet())).get(new ArrayList<String>(spinnerVillageMap.values()).indexOf(villageSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerVillageMap.keySet())).get(new ArrayList<String>(spinnerVillageMap.values()).indexOf(villageSpinner.getSelectedItem().toString())).toString():null:null);
                            addressObject.put("blockId",(spinnerBlocksMap.size() != 0)?!(new ArrayList<String>(spinnerBlocksMap.keySet())).get(new ArrayList<String>(spinnerBlocksMap.values()).indexOf(blockSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerBlocksMap.keySet())).get(new ArrayList<String>(spinnerBlocksMap.values()).indexOf(blockSpinner.getSelectedItem().toString())).toString():null:null);
                            addressObject.put("gramPanchayatId",(spinnerGramPanchayetMap.size() != 0)?!(new ArrayList<String>(spinnerGramPanchayetMap.keySet())).get(new ArrayList<String>(spinnerGramPanchayetMap.values()).indexOf(gramPanchayetSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerGramPanchayetMap.keySet())).get(new ArrayList<String>(spinnerGramPanchayetMap.values()).indexOf(gramPanchayetSpinner.getSelectedItem().toString())).toString():null:null);


                            jsonObject.put("memberAddres",addressObject);

                            String value = jsonObject.toString();


                            apiRequest.set_t(jsonObject);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(MemberDeatilsActivity.this).execute(apiRequest);
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



                //populateCollectionPoints(collectionPointId);
            }
        });
//        if(memberId != null )
//        {
//            //populateMemberEditData(memberId);
//        }


        ArrayAdapter<CharSequence> collectionDayAdapter = ArrayAdapter.createFromResource(this, R.array.collection_days, android.R.layout.simple_spinner_item);
        collectionDayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        collectionDays.setAdapter(collectionDayAdapter);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> visitStatusAdapter = ArrayAdapter.createFromResource(this, R.array.visit_status, android.R.layout.simple_spinner_item);
        visitStatusAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        visitStatusSpinner.setAdapter(visitStatusAdapter);

        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String name = countriesSpinner.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerCountriesMap.values());
                int a = indexes.indexOf(name);
                stateId = (new ArrayList<String>(spinnerCountriesMap.keySet())).get(indexes.indexOf(name));
                populateStates(stateId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });

        collectionDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int position = collectionDays.getSelectedItemPosition();
                populateCollectioPoints(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }

        });

        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String name = statesSpinner.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerStatesMap.values());
                int a = indexes.indexOf(name);
                districtId = (new ArrayList<String>(spinnerStatesMap.keySet())).get(indexes.indexOf(name));
                populateDistricts(districtId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        distrcitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String name = distrcitSpinner.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerDistrictsMap.values());
                int a = indexes.indexOf(name);
                municipalityId = (new ArrayList<String>(spinnerDistrictsMap.keySet())).get(indexes.indexOf(name));
                populateMunicipalities(municipalityId);

                String name2 = distrcitSpinner.getSelectedItem().toString();
                List<String> indexes2 = new ArrayList<String>(spinnerDistrictsMap.values());
                int b = indexes.indexOf(name2);
                blockId = (new ArrayList<String>(spinnerDistrictsMap.keySet())).get(indexes.indexOf(name));
                populateBlocks(blockId);
                populatePoliceStation(municipalityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        municipalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String name = municipalitySpinner.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerMunicipalitiesMap.values());
                int a = indexes.indexOf(name);
                wardId = (new ArrayList<String>(spinnerMunicipalitiesMap.keySet())).get(indexes.indexOf(name));
                populateWards(wardId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        blockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String name = blockSpinner.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerBlocksMap.values());
                int a = indexes.indexOf(name);
                gramPanchayatId = (new ArrayList<String>(spinnerBlocksMap.keySet())).get(indexes.indexOf(name));
                populateGramPanchayets(gramPanchayatId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        gramPanchayetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String name = gramPanchayetSpinner.getSelectedItem().toString();
                List<String> indexes = new ArrayList<String>(spinnerGramPanchayetMap.values());
                int a = indexes.indexOf(name);
                villageId = (new ArrayList<String>(spinnerGramPanchayetMap.keySet())).get(indexes.indexOf(name));
                populateVillages(villageId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        populateCountries();

        if(message!=null)
        {
            XmlPullParserFactory pullParserFactory;

            try{

                // init the parserfactory
                pullParserFactory = XmlPullParserFactory.newInstance();
                // get the parser
                XmlPullParser parser = pullParserFactory.newPullParser();

                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(new StringReader(message));
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                        // extract data from tag
                        //uid
                        aadhar = parser.getAttributeValue(null,DataAttributes.AADHAR_UID_ATTR);
                        //name
                        name = parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR);
                        //gender
                        gender = parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR);
                        // year of birth
                        yearOfBirth = parser.getAttributeValue(null,DataAttributes.AADHAR_YOB_ATTR);
                        dateOfBirth = parser.getAttributeValue(null,DataAttributes.AADHAR_DOB_ATTR);
                        // care of
                        careOf = parser.getAttributeValue(null,DataAttributes.AADHAR_CO_ATTR);
                        gName = parser.getAttributeValue(null,DataAttributes.AADHAR_GUARDIAN_ATTR);
                        // village Tehsil
                        villageTehsil = parser.getAttributeValue(null,DataAttributes.AADHAR_VTC_ATTR);
                        // Post Office
                        post = parser.getAttributeValue(null,DataAttributes.AADHAR_PO_ATTR);
                        // district
                        district = parser.getAttributeValue(null,DataAttributes.AADHAR_DIST_ATTR);
                        // state
                        state = parser.getAttributeValue(null,DataAttributes.AADHAR_STATE_ATTR);
                        // Post Code
                        postCode = parser.getAttributeValue(null,DataAttributes.AADHAR_PC_ATTR);
                        // house
                        house = parser.getAttributeValue(null,DataAttributes.AADHAR_HOUSE_ATTR);
                        // street
                        street = parser.getAttributeValue(null,DataAttributes.AADHAR_STREET_ATTR);
                        // location
                        location = parser.getAttributeValue(null,DataAttributes.AADHAR_LOCATION_ATTR);
                        // landmark
                        landmark = parser.getAttributeValue(null,DataAttributes.AADHAR_LANDMARK_ATTR);

                    }
                    eventType = parser.next();
                }
                displayScannedData();

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }

    private void checkAdharCardStatus()
    {
        if(!uid.getText().toString().isEmpty())
        {
            if(uid.getText().toString().length() == 12)
            {
                checkMemberAdhar(Long.parseLong(uid.getText().toString().isEmpty()?"0":uid.getText().toString()),emptyGuid);
                return;
            }
        }
    }

    private void checkMemberAdhar(long adharNumber, String memberEditId) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("addharNoExists/"+ adharNumber +"/{"+ memberEditId + "}");
    }

    private void setAdharCardCheckAdapter(String  result) throws JSONException
    {

//        response = new JSONObject().optBoolean(String.valueOf(result));
        if(result.equals("true"))
        {
            alertDialogue.showAlertMessage("Addhar No. Already Exists");
            uid.setText(null);
            return;
        }



    }

    @Override
    public void onApiRequestStart() throws IOException {

    }


    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
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

        else if(key.contains("districts-by-state"))
        {
            setDistrictsAdapter(result);
        }

        else if(key.contains("municipalities-by-district"))
        {
            setMunicipalitiesAdapter(result);
        }

        else if(key.contains("PoliceStation-by-district"))
        {
            setPoliceStationsAdapter(result);
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

        else if (key.contains("addharNoExists"))
        {
            try {
                setAdharCardCheckAdapter(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else if(key.contains("add-member"))
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberDeatilsActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("Member added successfully");
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
    }

    private void populateMemberEditData(String id) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("countries");
    }

    private void populateCountries() {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("countries");
    }

    private void populateCollectioPoints(int position) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("getCollectionpointByDay/"+ position);
    }

    private void populateStates(String stateId) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("states-by-country/{"+ stateId +"}");
    }


    private void populateDistricts(String districtId) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("districts-by-state/{"+ districtId +"}");
    }


    private void populatePoliceStation(String municipalityId) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("PoliceStation-by-district/{"+ municipalityId +"}");
    }

    private void populateMunicipalities(String municipalityId) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("municipalities-by-district/{"+ municipalityId +"}");
    }


    private void populateWards(String wardId) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("wards-by-municipality/{"+ wardId +"}");
    }


    private void populateBlocks(String blockId) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("blocks-by-district/{"+ blockId +"}");
    }


    private void populateGramPanchayets(String gramPanchayatId) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("panchayats-by-block/{"+ gramPanchayatId +"}");
    }


    private void populateVillages(String villageId) {
        new ApiHandler.GetAsync(MemberDeatilsActivity.this).execute("villages-by-panchayat/{"+ villageId +"}");
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



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDeatilsActivity.this, android.R.layout.simple_spinner_item,spinnerCountriesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        countriesSpinner.setAdapter(adapter);

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
            collectionPointMap.put(collectionPointList.get(i).getId(),collectionPointList.get(i).getName());
            spinnerCollectionPointArray[i] = collectionPointList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDeatilsActivity.this, android.R.layout.simple_spinner_item,spinnerCollectionPointArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        collectionPointSpinner.setAdapter(adapter);

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



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDeatilsActivity.this, android.R.layout.simple_spinner_item,spinnerStatesArray);
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



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDeatilsActivity.this, android.R.layout.simple_spinner_item,spinnerDistrictsArray);
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
                //JSONObject jsonObject = (JSONObject)jsonArray.get(i);
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

        String[] spinnerPoliceStationArray = new String[policeStationList.size()];
        for (int i = 0; i < policeStationList.size(); i++)
        {
            policeStationMap.put(policeStationList.get(i).getId(),policeStationList.get(i).getName());
            spinnerPoliceStationArray[i] = policeStationList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDeatilsActivity.this, android.R.layout.simple_spinner_item,spinnerPoliceStationArray);
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
                //JSONObject jsonObject = (JSONObject)jsonArray.get(i);
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

        String[] spinnerMunicipalitiesArray = new String[municipalityList.size()];
        for (int i = 0; i < municipalityList.size(); i++)
        {
            spinnerMunicipalitiesMap.put(municipalityList.get(i).getId(),municipalityList.get(i).getName());
            spinnerMunicipalitiesArray[i] = municipalityList.get(i).getName();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDeatilsActivity.this, android.R.layout.simple_spinner_item,spinnerMunicipalitiesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        municipalitySpinner.setAdapter(adapter);
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



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDeatilsActivity.this, android.R.layout.simple_spinner_item,spinnerWardsArray);
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



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDeatilsActivity.this, android.R.layout.simple_spinner_item,spinnerBlocksArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        blockSpinner.setAdapter(adapter);
    }


    private void setGramPanchayetsAdapter(String result)
    {
        try {
            gramPanchayetList = new ArrayList<GramPanchayet>();
            JSONArray jsonArray = new JSONArray(result);
            GramPanchayet gramPanchayet1 = new GramPanchayet(emptyGuid,"Select Gram-Panchayet");
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

        String[] spinnerGramPanchayetsArray = new String[gramPanchayetList.size()];
        for (int i = 0; i < gramPanchayetList.size(); i++)
        {
            spinnerGramPanchayetMap.put(gramPanchayetList.get(i).getId(),gramPanchayetList.get(i).getName());
            spinnerGramPanchayetsArray[i] = gramPanchayetList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDeatilsActivity.this, android.R.layout.simple_spinner_item,spinnerGramPanchayetsArray);
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberDeatilsActivity.this, android.R.layout.simple_spinner_item,spinnerVillagesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        villageSpinner.setAdapter(adapter);
    }

    public void displayScannedData() throws ParseException {

        String[] animals;

        middleName = "";
        if(name.contains(" ")){
            animals = name.split(" ");
            int animalIndex = 0;
            for (String animal : animals) {
                //System.out.println(animalIndex + ". " + animal);
                if(animalIndex == 0)
                {
                    firstName = animal;
                }
                if((animals.length-1) == animalIndex && animalIndex!=0)
                {
                    lastName = animal;
                }
                if((animals.length-1) != animalIndex && animalIndex!=0)
                {
                    middleName = middleName + animal;
                }
                animalIndex++;
            }

        }






        uid.setText("");
        fName.setText("");
        mName.setText("");
        lName.setText("");
        guardian.setText("");
        dob.setText("");
        postOffice.setText("");
        pinCode.setText("");
        houseNo.setText("");
        streetName.setText("");
        fullAddress.setText("");

        if(gender.equals("M"))
        {
            genderSpinner.setSelection(1);
        }
        else if(gender.equals("F"))
        {
            genderSpinner.setSelection(2);
        }
        else
        {
            genderSpinner.setSelection(0);
        }

        if(dateOfBirth!=null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = dateFormat.parse(dateOfBirth);

            SimpleDateFormat fmtOut = new SimpleDateFormat("MM-dd-yyyy");
            dob.setText(fmtOut.format(date));
        }
        if(gName!=null)
        {
            guardian.setText(gName);
        }
        else
        {
            guardian.setText(careOf);
        }

        houseNo.setText(house);
        streetName.setText(street);

        house = (house==null)?"":house+",";
        street = (street==null)?"":street+",";
        landmark = (landmark == null)?"":landmark+",";
        location = (location == null)?"":location+",";
        villageTehsil = (villageTehsil == null)?"":villageTehsil+",";

        uid.setText(aadhar);
        fName.setText(firstName);
        mName.setText(middleName);
        lName.setText(lastName);
        fullAddress.setText(house+street+landmark+location+villageTehsil+post+","+district+","+state+"-"+postCode);

        postOffice.setText(post);
        pinCode.setText(postCode);
        if(!uid.getText().toString().isEmpty())
        {
            uid.setEnabled(false);
        }
        if(!fName.getText().toString().isEmpty())
        {
            fName.setEnabled(false);
        }
        if(!mName.getText().toString().isEmpty())
        {
            mName.setEnabled(false);
        }
        if(!lName.getText().toString().isEmpty())
        {
            lName.setEnabled(false);
        }
        checkAdharCardStatus();
    }
    public void goMemberBack(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MemberActivity.class);
        startActivity(intent);
    }

}
