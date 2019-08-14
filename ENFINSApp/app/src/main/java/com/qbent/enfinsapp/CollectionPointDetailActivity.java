package com.qbent.enfinsapp;

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
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.global.AuthHelper;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Block;
import com.qbent.enfinsapp.model.CollectionPoint;
import com.qbent.enfinsapp.model.Country;
import com.qbent.enfinsapp.model.District;
import com.qbent.enfinsapp.model.GramPanchayet;
import com.qbent.enfinsapp.model.Municipality;
import com.qbent.enfinsapp.model.State;
import com.qbent.enfinsapp.model.Village;
import com.qbent.enfinsapp.model.Ward;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class CollectionPointDetailActivity extends MainActivity implements ApiCallback
{
    int PERMISSIONS_REQUEST_FINE_LOCATION = 0;

    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    LocationManager lm;
    String collectionId;
    Boolean editCountryChanged = false;
    Boolean editStateChanged = false;
    Boolean editDistrictChanged = false;
    Boolean editMunChanged = false;
    Boolean editWardChanged = false;
    Boolean editPanchayetChanged = false;
    Boolean editVillChanged = false;
    Boolean editBlockChanged = false;
    String us_id, us_Name, form_Date, user_Address, user_Pincode, user_Place, mobile_No, user_country, user_state, user_district, user_municipal, user_ward, user_block, user_panchayet, user_village;
    int collection_day;
    String emptyGuid = "00000000-0000-0000-0000-000000000000";
    //----Developed by Debmalya----//
    Button backButton, saveButton;

    //----Developed by Debmalya----//
    Spinner collectionDays, countriesSpinner, statesSpinner, distrcitSpinner, municipalitySpinner, wardSpinner, blockSpinner, gramPanchayetSpinner, villageSpinner;

    //----Developed by Debmalya----//
    private List<Country> countryLists;
    private List<State> statesLists;
    private List<District> districtList;
    private List<Municipality> municipalityList;
    private List<Ward> wardList;
    private List<Block> blockList;
    private List<GramPanchayet> gramPanchayetList;
    private List<Village> villageList;

    //----Developed by Debmalya----//
    private String stateId = " ";
    private String districtId = " ";
    private String municipalityId = " ";
    private String wardId = " ";
    private String blockId = " ";
    private String gramPanchayatId = " ";
    private String villageId = " ";
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    //----Developed by Debmalya----//
    EditText userNameField, formationDateField, userAddressField, userPincodeField, userPlaceField, userMobileField;

    TextView headerField;

    //----Developed by Debmalya----//
    HashMap<String, String> spinnerCountriesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerStatesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerDistrictsMap = new HashMap<String, String>();
    HashMap<String, String> spinnerMunicipalitiesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerWardsMap = new HashMap<String, String>();
    HashMap<String, String> spinnerBlocksMap = new HashMap<String, String>();
    HashMap<String, String> spinnerGramPanchayetMap = new HashMap<String, String>();
    HashMap<String, String> spinnerVillageMap = new HashMap<String, String>();

    double longitude,latitude;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_collection_point_detail_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_collection_point);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_collection_point_detail, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_collection_point);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_FINE_LOCATION);
            }
            else {
                getLocation();
            }
        }
        else
        {
            getLocation();
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                        PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//            } else {
//                getLocation();
//            }
//        }
//        else
//        {
//            getLocation();
//        }

        final AlertDialogue alertDialogue = new AlertDialogue(CollectionPointDetailActivity.this);

//        lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);



        collectionDays = (Spinner) findViewById(R.id.spinnerCollectionDay);
        countriesSpinner = (Spinner) findViewById(R.id.spinnerCountry);
        statesSpinner = (Spinner) findViewById(R.id.spinnerState);
        distrcitSpinner = (Spinner) findViewById(R.id.spinnerDistrict);
        municipalitySpinner = (Spinner) findViewById(R.id.spinnerMunicipality);
        wardSpinner = (Spinner) findViewById(R.id.spinnerWard);
        blockSpinner = (Spinner) findViewById(R.id.spinnerBlock);
        gramPanchayetSpinner = (Spinner) findViewById(R.id.spinnerGramPanchayat);
        villageSpinner = (Spinner) findViewById(R.id.spinnerVillage);
        headerField = (TextView) findViewById(R.id.collectionPointHeader);

        countryLists = new ArrayList<Country>();
        statesLists = new ArrayList<State>();
        districtList = new ArrayList<District>();
        municipalityList = new ArrayList<Municipality>();
        wardList = new ArrayList<Ward>();
        blockList = new ArrayList<Block>();
        gramPanchayetList = new ArrayList<GramPanchayet>();
        villageList = new ArrayList<Village>();

        userNameField = findViewById(R.id.editTextName);
        userNameField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        formationDateField = findViewById(R.id.editTextFormationDate);
        formationDateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        userAddressField = findViewById(R.id.editTextAddress);
        userAddressField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        userPincodeField = findViewById(R.id.editTextPincode);
        userPincodeField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });


        userPlaceField = findViewById(R.id.editTextPlace);
        userPlaceField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        userMobileField = findViewById(R.id.editTextMobile);
        userMobileField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        saveButton = (Button) findViewById(R.id.submitButton);
        backButton = (Button) findViewById(R.id.backButton);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.design_default_color_primary)), image, null);
        saveButton.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.design_default_color_primary)), image1, null);
        backButton.setBackground(rippledBg1);

        fab.setVisibility(View.GONE);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        formationDateField.setHint(dateFormat.format(new Date()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        formationDateField.setText((String) dateFormat.format(new Date()));

        formationDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CollectionPointDetailActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year) {
                month += 1;
                String date = month + "-" + year + "-" + day;
                formationDateField.setText(date);
            }
        };

        collectionId = getIntent().getStringExtra("collection_id");

        if (collectionId != null) {
            headerField.setText("Edit Collection Point");
        }

        //----Collection point save button----//
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userNameField.getText().toString().length() == 0) {
                    userNameField.requestFocus();
                    userNameField.setError("COLLECTION NAME CANNOT BE EMPTY");
                    return;
                }
                if (formationDateField.getText().toString().length() == 0) {
                    formationDateField.requestFocus();
                    formationDateField.setError("FORMATION DATE CANNOT BE EMPTY");
                    return;
                }
                if (collectionDays.getSelectedItemPosition() == 0) {
                    alertDialogue.showAlertMessage("PLEASE SELECT A COLLECTION DAY");
                    return;
                }
                if (userAddressField.getText().toString().length() == 0) {
                    userAddressField.requestFocus();
                    userAddressField.setError("ADDRESS CANNOT BE EMPTY");
                    return;
                }
                if (userPincodeField.getText().toString().length() == 0) {
                    userPincodeField.requestFocus();
                    userPincodeField.setError("PINCODE CANNOT BE EMPTY");
                    return;
                }
                if (userPincodeField.getText().toString().length() < 6) {
                    userPincodeField.requestFocus();
                    userPincodeField.setError("PINCODE MUST BE 6 DIGITS");
                    return;
                }
                if (userPlaceField.getText().toString().length() == 0) {
                    userPlaceField.requestFocus();
                    userPlaceField.setError("PLACE CANNOT BE EMPTY");
                    return;
                }
                if (userMobileField.getText().toString().length() == 0) {
                    userMobileField.requestFocus();
                    userMobileField.setError("MOBILE NUMBER CANNOT BE EMPTY");
                    return;
                }
                if (userMobileField.getText().toString().length() < 10) {
                    userMobileField.requestFocus();
                    userMobileField.setError("MOBILE NUMBER MUST BE 10 DIGITS");
                    return;
                }
                if ((new ArrayList<String>(spinnerCountriesMap.keySet())).get(new ArrayList<String>(spinnerCountriesMap.values()).indexOf(countriesSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)) {
                    alertDialogue.showAlertMessage("PLEASE SELECT A COUNTRY");
                    return;
                }
                if ((new ArrayList<String>(spinnerStatesMap.keySet())).get(new ArrayList<String>(spinnerStatesMap.values()).indexOf(statesSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)) {
                    alertDialogue.showAlertMessage("PLEASE SELECT A STATE");
                    return;
                }
                if ((new ArrayList<String>(spinnerDistrictsMap.keySet())).get(new ArrayList<String>(spinnerDistrictsMap.values()).indexOf(distrcitSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)) {
                    alertDialogue.showAlertMessage("PLEASE SELECT A DISTRICT");
                    return;
                }

                if ((new ArrayList<String>(spinnerVillageMap.keySet())).get(new ArrayList<String>(spinnerVillageMap.values()).indexOf(villageSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)
                        && (new ArrayList<String>(spinnerWardsMap.keySet())).get(new ArrayList<String>(spinnerWardsMap.values()).indexOf(wardSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)) {
                    //userMobileField.requestFocus();
                    if ((new ArrayList<String>(spinnerVillageMap.keySet())).get(new ArrayList<String>(spinnerVillageMap.values()).indexOf(villageSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)) {
                        alertDialogue.showAlertMessage("PLEASE SELECT A VILLAGE");
                    } else {
                        alertDialogue.showAlertMessage("PLEASE SELECT A WARD");
                    }

                    //userMobileField.setError("MOBILE NUMBER CANNOT BE EMPTY");
                    return;
                }

//                //TODO Edited by Debmalya//

//
//                //TODO End//
//                getLocation();


                getLocation();


                ApiRequest apiRequest = new ApiRequest("collection-point/add");
                try{
                    JSONObject jsonObject = new JSONObject();
                    if(collectionId != null)
                    {
                        apiRequest.set_restApiUrl("collection-point/edit/{" + collectionId + "}");
                        jsonObject.accumulate("id",collectionId);
                    }


                    jsonObject.accumulate("name",userNameField.getText().toString());
                    jsonObject.accumulate("formationDate",formationDateField.getText().toString());
                    jsonObject.accumulate("collectionDay",collectionDays.getSelectedItemPosition());
                    jsonObject.accumulate("address",userAddressField.getText().toString());
                    jsonObject.accumulate("pincode",userPincodeField.getText().toString());
                    jsonObject.accumulate("place",userPlaceField.getText().toString());
                    jsonObject.accumulate("mobileNo",userMobileField.getText().toString());
                    jsonObject.accumulate("lon",longitude);
                    jsonObject.accumulate("lat",latitude);
                    jsonObject.accumulate("countryId",!(new ArrayList<String>(spinnerCountriesMap.keySet())).get(new ArrayList<String>(spinnerCountriesMap.values()).indexOf(countriesSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerCountriesMap.keySet())).get(new ArrayList<String>(spinnerCountriesMap.values()).indexOf(countriesSpinner.getSelectedItem().toString())).toString():null);
                    jsonObject.accumulate("stateId",!(new ArrayList<String>(spinnerStatesMap.keySet())).get(new ArrayList<String>(spinnerStatesMap.values()).indexOf(statesSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerStatesMap.keySet())).get(new ArrayList<String>(spinnerStatesMap.values()).indexOf(statesSpinner.getSelectedItem().toString())).toString():null);
                    jsonObject.accumulate("districtId",!(new ArrayList<String>(spinnerDistrictsMap.keySet())).get(new ArrayList<String>(spinnerDistrictsMap.values()).indexOf(distrcitSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerDistrictsMap.keySet())).get(new ArrayList<String>(spinnerDistrictsMap.values()).indexOf(distrcitSpinner.getSelectedItem().toString())).toString():null);
                    jsonObject.accumulate("municipalityId",(spinnerMunicipalitiesMap.size() != 0)?!(new ArrayList<String>(spinnerMunicipalitiesMap.keySet())).get(new ArrayList<String>(spinnerMunicipalitiesMap.values()).indexOf(municipalitySpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerMunicipalitiesMap.keySet())).get(new ArrayList<String>(spinnerMunicipalitiesMap.values()).indexOf(municipalitySpinner.getSelectedItem().toString())).toString():null:null);
                    jsonObject.accumulate("wardId",(spinnerWardsMap.size() != 0)?!(new ArrayList<String>(spinnerWardsMap.keySet())).get(new ArrayList<String>(spinnerWardsMap.values()).indexOf(wardSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerWardsMap.keySet())).get(new ArrayList<String>(spinnerWardsMap.values()).indexOf(wardSpinner.getSelectedItem().toString())).toString():null:null);
                    jsonObject.accumulate("villageId",(spinnerVillageMap.size() != 0)?!(new ArrayList<String>(spinnerVillageMap.keySet())).get(new ArrayList<String>(spinnerVillageMap.values()).indexOf(villageSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerVillageMap.keySet())).get(new ArrayList<String>(spinnerVillageMap.values()).indexOf(villageSpinner.getSelectedItem().toString())).toString():null:null);
                    jsonObject.accumulate("blockId",(spinnerBlocksMap.size() != 0)?!(new ArrayList<String>(spinnerBlocksMap.keySet())).get(new ArrayList<String>(spinnerBlocksMap.values()).indexOf(blockSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerBlocksMap.keySet())).get(new ArrayList<String>(spinnerBlocksMap.values()).indexOf(blockSpinner.getSelectedItem().toString())).toString():null:null);
                    jsonObject.accumulate("gramPanchayatId",(spinnerGramPanchayetMap.size() != 0)?!(new ArrayList<String>(spinnerGramPanchayetMap.keySet())).get(new ArrayList<String>(spinnerGramPanchayetMap.values()).indexOf(gramPanchayetSpinner.getSelectedItem().toString())).toString().equals(emptyGuid)?(new ArrayList<String>(spinnerGramPanchayetMap.keySet())).get(new ArrayList<String>(spinnerGramPanchayetMap.values()).indexOf(gramPanchayetSpinner.getSelectedItem().toString())).toString():null:null);


                    apiRequest.set_t(jsonObject);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new ApiHandler.PostAsync(CollectionPointDetailActivity.this).execute(apiRequest);

            }
        });
        //----End of collection point save button----//

        //---Cancel button---//
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),CollectionPointListActivity.class));
            }
        });
        //---End of cancel button---//

        if(collectionId != null)
        {
            populateCollectionPointEditData(collectionId);
        }

        //-----Developer by Debmalya-------//
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.collection_days, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        collectionDays.setAdapter(arrayAdapter);

        countriesSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editCountryChanged = true;
                }
                return false;
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
        distrcitSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editDistrictChanged = true;
                }
                return false;
            }
        });
        municipalitySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editMunChanged= true;
                }
                return false;
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
        gramPanchayetSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editPanchayetChanged = true;
                }
                return false;
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
        villageSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editVillChanged = true;
                }
                return false;
            }
        });

        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String test = "a";
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(user_country!=null && !user_country.equals("null")&& !user_country.equals(emptyGuid) && editCountryChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerCountriesMap.keySet());
                    int test = indexes1.indexOf(user_country);
                    String test2 = (new ArrayList<String>(spinnerCountriesMap.values())).get(test);
                    countriesSpinner.setSelection(((ArrayAdapter<String>)countriesSpinner.getAdapter()).getPosition(test2));
                    stateId = user_country;
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
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//
            }

        });

        //-----Developed by Debmalya-----//
        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if( user_state!=null && !user_state.equals("null")&& !user_state.equals(emptyGuid)&& !stateId.equals(emptyGuid) && editStateChanged==false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerStatesMap.keySet());
                    int test = indexes1.indexOf(user_state);
                    String test2 = (new ArrayList<String>(spinnerStatesMap.values())).get(test);
                    statesSpinner.setSelection(((ArrayAdapter<String>)statesSpinner.getAdapter()).getPosition(test2));
                    districtId = user_state;
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

        //-----Developed by Debmalya-----//
        distrcitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(user_district!=null && !user_district.equals("null")&& !user_district.equals(emptyGuid) &&  !districtId.equals(emptyGuid) && editDistrictChanged==false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerDistrictsMap.keySet());
                    int test = indexes1.indexOf(user_district);
                    String test2 = (new ArrayList<String>(spinnerDistrictsMap.values())).get(test);
                    distrcitSpinner.setSelection(((ArrayAdapter<String>)distrcitSpinner.getAdapter()).getPosition(test2));
                    municipalityId = user_district;
                }
                else
                {
                    String name = distrcitSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerDistrictsMap.values());
                    int a = indexes.indexOf(name);
                    municipalityId = (new ArrayList<String>(spinnerDistrictsMap.keySet())).get(indexes.indexOf(name));
                }
                populateMunicipalities(municipalityId);
                populateBlocks(municipalityId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        //-----Developed by Debmalya-----//
        municipalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(user_municipal!=null && !user_municipal.equals("null")&& !user_municipal.equals(emptyGuid) &&  !municipalityId.equals(emptyGuid) && editMunChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerMunicipalitiesMap.keySet());
                    int test = indexes1.indexOf(user_municipal);
                    String test2 = (new ArrayList<String>(spinnerMunicipalitiesMap.values())).get(test);
                    municipalitySpinner.setSelection(((ArrayAdapter<String>)municipalitySpinner.getAdapter()).getPosition(test2));
                    wardId = user_municipal;
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
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        //-----Developed by Debmalya-----//
        blockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(user_block!=null && !user_block.equals("null")&& !user_block.equals(emptyGuid) &&  !municipalityId.equals(emptyGuid) && editBlockChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerBlocksMap.keySet());
                    int test = indexes1.indexOf(user_block);
                    String test2 = (new ArrayList<String>(spinnerBlocksMap.values())).get(test);
                    blockSpinner.setSelection(((ArrayAdapter<String>)blockSpinner.getAdapter()).getPosition(test2));
                    gramPanchayatId = user_block;
                }
                else
                {
                    String name = blockSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerBlocksMap.values());
                    int a = indexes.indexOf(name);
                    gramPanchayatId = (new ArrayList<String>(spinnerBlocksMap.keySet())).get(indexes.indexOf(name));
                }
                populateGramPanchayets(gramPanchayatId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        //-----Developed by Debmalya-----//
        gramPanchayetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(user_panchayet!=null && !user_panchayet.equals("null")&& !user_panchayet.equals(emptyGuid) &&  !gramPanchayatId.equals(emptyGuid) && editPanchayetChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerGramPanchayetMap.keySet());
                    int test = indexes1.indexOf(user_panchayet);
                    String test2 = (new ArrayList<String>(spinnerGramPanchayetMap.values())).get(test);
                    gramPanchayetSpinner.setSelection(((ArrayAdapter<String>)gramPanchayetSpinner.getAdapter()).getPosition(test2));
                    villageId = user_panchayet;
                }
                else{
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

        wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(user_ward!=null && !user_ward.equals("null")&& !user_ward.equals(emptyGuid) &&  !wardId.equals(emptyGuid) && editWardChanged ==false)
                {
                    List<String> indexes = new ArrayList<String>(spinnerWardsMap.keySet());
                    int test = indexes.indexOf(user_ward);
                    String test2 = (new ArrayList<String>(spinnerWardsMap.values())).get(test);
                    wardSpinner.setSelection(((ArrayAdapter<String>)wardSpinner.getAdapter()).getPosition(test2));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(user_village!=null &&!user_village.equals("null")&& !user_village.equals(emptyGuid) &&  !villageId.equals(emptyGuid) && editVillChanged == false)
                {
                    List<String> indexes = new ArrayList<String>(spinnerVillageMap.keySet());
                    int test = indexes.indexOf(user_village);
                    String test2 = (new ArrayList<String>(spinnerVillageMap.values())).get(test);
                    villageSpinner.setSelection(((ArrayAdapter<String>)villageSpinner.getAdapter()).getPosition(test2));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Yet to be completed//
            }
        });

        populateCountries();



    }

//    private void getLocation()
//    {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            return;
//        }
//        try
//        {
//            longitude = bestLocation.getLongitude();
//            latitude = bestLocation.getLatitude();
//        }
//        catch (NullPointerException npe)
//        {
//            npe.printStackTrace();
//        }
//    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                       int[] grantResults)
    {


        if (requestCode == PERMISSIONS_REQUEST_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            getLocation();
        }

        else
        {
            Intent intent = new Intent(getApplicationContext(),CollectionPointListActivity.class);
            startActivity(intent);
        }
    }

    private void getLocation()
    {
        lm = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(CollectionPointDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CollectionPointDetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }

        if(bestLocation == null)
        {
            Intent memberIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(memberIntent);
        }

//                Criteria criteria = new Criteria();
//                String bestProvider = lm.getBestProvider(criteria, false);
//                Location loc = lm.getLastKnownLocation(bestProvider);
        try
        {
            longitude = bestLocation.getLongitude();
            latitude = bestLocation.getLatitude();
        }
        catch (NullPointerException npe)
        {
            npe.printStackTrace();
        }

    }

    @Override
    public void onApiRequestStart() throws IOException {

    }

    //----Developed by Debmalya----//
    @Override
    public void onApiRequestComplete(String key,String result) throws IOException
    {
        if (key.equals("countries"))
        {
            setCountriesAdapter(result);
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

        else if(key.contains("collection-point/add") || key.contains("collection-point/edit"))
        {
            setCollectionPointsAdapter(result);
        }
        else if(key.contains("collection-point") && !key.contains("collection-point/add")&& !key.contains("collection-point/edit"))
        {
            try {
                setEditCollectionPointsAdapter(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private void populateCollectionPointEditData(String id) {
        new ApiHandler.GetAsync(CollectionPointDetailActivity.this).execute("collection-point/{" + id + "}");
    }

    private void populateCountries() {
        new ApiHandler.GetAsync(CollectionPointDetailActivity.this).execute("countries");
    }

    private void populateStates(String stateId) {
        new ApiHandler.GetAsync(CollectionPointDetailActivity.this).execute("states-by-country/{"+ stateId +"}");
    }

    //-----Developed by Debmalya-----//
    private void populateDistricts(String districtId) {
        new ApiHandler.GetAsync(CollectionPointDetailActivity.this).execute("districts-by-state/{"+ districtId +"}");
    }

    //-----Developed by Debmalya-----//
    private void populateMunicipalities(String municipalityId) {
        new ApiHandler.GetAsync(CollectionPointDetailActivity.this).execute("municipalities-by-district/{"+ municipalityId +"}");
    }

    //-----Developed by Debmalya-----//
    private void populateWards(String wardId) {
        new ApiHandler.GetAsync(CollectionPointDetailActivity.this).execute("wards-by-municipality/{"+ wardId +"}");
    }

    //-----Developed by Debmalya-----//
    private void populateBlocks(String blockId) {
        new ApiHandler.GetAsync(CollectionPointDetailActivity.this).execute("blocks-by-district/{"+ blockId +"}");
    }

    //-----Developed by Debmalya-----//
    private void populateGramPanchayets(String gramPanchayatId) {
        new ApiHandler.GetAsync(CollectionPointDetailActivity.this).execute("panchayats-by-block/{"+ gramPanchayatId +"}");
    }

    //-----Developed by Debmalya-----//
    private void populateVillages(String villageId) {
        new ApiHandler.GetAsync(CollectionPointDetailActivity.this).execute("villages-by-panchayat/{"+ villageId +"}");
    }


    private void setCountriesAdapter(String result) {
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionPointDetailActivity.this, android.R.layout.simple_spinner_item,spinnerCountriesArray);
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



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionPointDetailActivity.this, android.R.layout.simple_spinner_item,spinnerStatesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        statesSpinner.setAdapter(adapter);
    }

    //-----Developed by Debmalya-----//
    private void setDistrictsAdapter(String result)
    {
        try {
            districtList = new ArrayList<District>();
            JSONArray jsonArray = new JSONArray(result);
            District district1 = new District(emptyGuid,"Select District");
            districtList.add(district1);
            for (int i = 0; i < jsonArray.length(); i++)
            {
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



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionPointDetailActivity.this, android.R.layout.simple_spinner_item,spinnerDistrictsArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        distrcitSpinner.setAdapter(adapter);
    }

    //-----Developed by Debmalya-----//
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



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionPointDetailActivity.this, android.R.layout.simple_spinner_item,spinnerMunicipalitiesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        municipalitySpinner.setAdapter(adapter);
    }

    //-----Developed by Debmalya-----//
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



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionPointDetailActivity.this, android.R.layout.simple_spinner_item,spinnerWardsArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        wardSpinner.setAdapter(adapter);
    }

    //-----Developed by Debmalya-----//
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



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionPointDetailActivity.this, android.R.layout.simple_spinner_item,spinnerBlocksArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        blockSpinner.setAdapter(adapter);
    }

    //-----Developed by Debmalya-----//
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionPointDetailActivity.this, android.R.layout.simple_spinner_item,spinnerGramPanchayetsArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        gramPanchayetSpinner.setAdapter(adapter);
    }

    //-----Developed by Debmalya-----//
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionPointDetailActivity.this, android.R.layout.simple_spinner_item,spinnerVillagesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        villageSpinner.setAdapter(adapter);
    }

    private void setEditCollectionPointsAdapter(String result) throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            JSONObject jsonObject = new JSONObject(result);
            us_id = jsonObject.getString("id");
            us_Name = jsonObject.getString("name");
            user_Address = jsonObject.getString("address");
            form_Date = jsonObject.getString("formationDate");
            collection_day = jsonObject.getInt("collectionDay");
            user_Pincode = jsonObject.getString("pincode");
            user_Place = jsonObject.getString("place");
            mobile_No = jsonObject.getString("mobileNo");
            user_country = jsonObject.getString("countryId");
            user_state = jsonObject.getString("stateId");
            user_district = jsonObject.getString("districtId");
            user_municipal = jsonObject.getString("municipalityId");
            user_ward = jsonObject.getString("wardId");
            user_block = jsonObject.getString("blockId");
            user_panchayet = jsonObject.getString("gramPanchayatId");
            user_village = jsonObject.getString("villageId");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Date date = dateFormat.parse(form_Date);

        SimpleDateFormat fmtOut = new SimpleDateFormat("MM-dd-yyyy");

        userNameField.setText(us_Name);
        formationDateField.setText(fmtOut.format(date));
        userAddressField.setText(user_Address);
        userPincodeField.setText(user_Pincode);
        userPlaceField.setText(user_Place);
        userMobileField.setText(mobile_No);
        collectionDays.setSelection(collection_day);
    }

    //----Developed by Debmalya----//
    private void setCollectionPointsAdapter(String result)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CollectionPointDetailActivity.this);
        builder.setTitle("ENFIN Admin");
        builder.setMessage("Collection Point Saved Successfully");
        builder.setCancelable(true);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Intent intent = new Intent(getApplicationContext(),CollectionPointListActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}




