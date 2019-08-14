package com.qbent.enfinsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.qbent.enfinsapp.adapter.DemandCollectionParentAdapter;
import com.qbent.enfinsapp.adapter.HomepageAdapter;
import com.qbent.enfinsapp.adapter.HomepageAdapterMobileVersion;
import com.qbent.enfinsapp.global.AuthHelper;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.CollectionPoint;
import com.qbent.enfinsapp.model.Demand;
import com.qbent.enfinsapp.model.DemandCollection;
import com.qbent.enfinsapp.model.DemandDate;
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
import java.util.List;
import java.util.TimeZone;

public class DemandCollectionPosting extends MainActivity implements ApiCallback
{
    RecyclerView first_recycler_view;
    DemandCollectionParentAdapter demand_list_parent_adapter;
    private AuthHelper _authHelper;

    LocationManager lm;

    EditText collectionPointDate,collectionPointDay;
    private Date workingDate,selectionDate;
    public ProgressBar progressBar;

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_demand_collection_posting);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_demand_collection_posting, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_demandcollectionposting);

        _authHelper = AuthHelper.getInstance(this);

        ArrayList<DemandDate> demandDateArrayList;
        DemandCollection demandCollection = new DemandCollection();

        fab.setVisibility(View.GONE);

        lm = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
//        if( !lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
//            new AlertDialog.Builder(DemandCollectionPosting.this)
//                    .setTitle("GPS not found")  // GPS not found
//                    .setMessage("Kindly enable GPS for Demand Collection") // Want to enable?
//                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        }
//                    })
//                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent memberIntent = new Intent(getApplicationContext(), HomeActivity.class);
//                            startActivity(memberIntent);
//                        }
//                    })
//                    .show();
//        }

        collectionPointDate = (EditText) findViewById(R.id.demandCollection_date);
        collectionPointDay = (EditText) findViewById(R.id.demandCollection_day);
        progressBar = (ProgressBar) findViewById(R.id.progressbarDemandCollection);

        collectionPointDate.setText(_authHelper.getIdSlectionDate());
        collectionPointDate.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);

        //---Date field---//

        try {
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd-yyyy");
            workingDate = simpleDateFormat1.parse(_authHelper.getIdSlectionDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(workingDate);
            String[] days = new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };

            String day = days[calendar.get(Calendar.DAY_OF_WEEK)-1];
            collectionPointDay.setText(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }




        if(collectionPointDate.getText().toString().length()!=0)
        {
            ApiRequest apiRequest = new ApiRequest("all-loan-demand");
            try{

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("collectionDate",_authHelper.getIdSlectionDate());


                apiRequest.set_t(jsonObject);

                //JSONArray jsonDatesArray = jsonObject.getJSONArray("loanDemandDetails");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            new ApiHandler.PostAsync(DemandCollectionPosting.this).execute(apiRequest);
        }


    }
    //---End of onCreate() method---//



    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if(key.contains("all-loan-demand"))
        {
            try {
                setDemandCollectionAdapter(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(key.contains("collectCashAndUpdate"))
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(DemandCollectionPosting.this);
            builder.setTitle("ENFIN Admin");
            builder.setMessage("Demand Collection Saved Successfully");
            builder.setCancelable(true);
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Intent intent = new Intent(DemandCollectionPosting.this, DemandCollectionPosting.class);
                    startActivity(intent);

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }

    private void setDemandCollectionAdapter(String result) throws JSONException
    {
        ArrayList<DemandDate> demandDateArrayList;
        DemandCollection demandCollection = new DemandCollection();
        try {
            //---Fetch the collection point name as heading---//
            JSONArray jsonArray = new JSONArray(result);
            demandDateArrayList = new ArrayList<>();
            if (ActivityCompat.checkSelfPermission(DemandCollectionPosting.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DemandCollectionPosting.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            List<String> providers = lm.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
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

            final double longitude = bestLocation.getLongitude();
            final double latitude = bestLocation.getLatitude();

            for(int i=0;i<jsonArray.length();i++)
            {

                JSONObject jsonObject = (JSONObject)jsonArray.get(i);


                //---Fetching another values corresponding to C.P names---//
                JSONArray jsonArray2 =  jsonObject.getJSONArray("loanDemandDetails");
                ArrayList<Demand> demandArrayList = new ArrayList<>();
                for(int j=0;j<jsonArray2.length();j++)
                {
                    JSONObject jsonObject2 = (JSONObject)jsonArray2.get(j);
                    Demand demand = new Demand(
                            jsonObject2.getString("loanId"),
                            jsonObject2.getString("loanBondNo"),
                            jsonObject2.getString("borrowerName"),
                            jsonObject2.getString("coBorrowerName"),
                            jsonObject2.getString("loanProductName"),
                            jsonObject2.optInt("loanAmount"),
                            jsonObject2.optInt("outstandingAmount"),
                            jsonObject2.optInt("installmentNo"),
                            jsonObject2.optInt("totalInstallmentAmount"),
                            jsonObject2.optInt("overDuePrincipal"),
                            jsonObject2.optInt("overDueInterest"),
                            jsonObject2.optInt("installmentPrincipal"),
                            jsonObject2.optInt("installmentInterest"),
                            jsonObject2.optInt("totalPaidAmount"),
                            jsonObject2.getString("remarks"),
                            jsonObject2.optBoolean("isCollected"),
                            jsonObject2.optBoolean("isPreClose"),
                            jsonObject2.optBoolean("isAdvendeCollection"),
                            jsonObject2.optBoolean("isMemberPresent"),
                            latitude,
                            longitude
                    );
                    demandArrayList.add(demand);

                }
                DemandDate demandDate = new DemandDate(
                        jsonObject.getString("collectionPointName"),
                        false,
                        false,
                        demandArrayList
                );


                demandDateArrayList.add(demandDate);
                //---End of fetching other values---//

            }

            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
            {
                demandCollection.setDemandDatesList(demandDateArrayList);
                first_recycler_view = (RecyclerView) findViewById(R.id.firstRecyclerView);
                demand_list_parent_adapter = new DemandCollectionParentAdapter(demandCollection,DemandCollectionPosting.this);
                first_recycler_view.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                first_recycler_view.setLayoutManager(mLayoutManager);
                first_recycler_view.setItemAnimator(new DefaultItemAnimator());
                first_recycler_view.setAdapter(demand_list_parent_adapter);
            }
            else
            {
                demandCollection.setDemandDatesList(demandDateArrayList);
                first_recycler_view = (RecyclerView) findViewById(R.id.firstRecyclerView);
                demand_list_parent_adapter = new DemandCollectionParentAdapter(demandCollection,DemandCollectionPosting.this);
                first_recycler_view.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                first_recycler_view.setLayoutManager(mLayoutManager);
                first_recycler_view.setItemAnimator(new DefaultItemAnimator());
                first_recycler_view.setAdapter(demand_list_parent_adapter);
            }



           progressBar.setVisibility(View.GONE);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }




    }


}
