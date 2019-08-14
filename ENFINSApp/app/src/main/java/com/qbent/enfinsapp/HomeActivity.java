package com.qbent.enfinsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qbent.enfinsapp.adapter.HomepageAdapter;
import com.qbent.enfinsapp.adapter.HomepageAdapterMobileVersion;
import com.qbent.enfinsapp.model.Dashboard;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends MainActivity implements ApiCallback
{

    private RecyclerView recyclerView;
    private List<Dashboard> dashboardLists = new ArrayList<Dashboard>();

    LocationManager lm1;

    private String loname;
    private int totalDemand,totalCollection,totalCp,totalPrincipalDemand,totalInterestDemand,totalPrincipalCollection,totalInterestCollection;

    TextInputEditText loNameField,todayTotalDemandField,todayTotalCollectionField,totalCpField,totalPrincipalDemandField,totalInterestDemandField,
            totalPrincipalCollectionField,totalInterestCollectionField;

    TextView demandView,collectionView;

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
            View contentView = inflater.inflate(R.layout.activity_home_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_home);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_home, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_home);
        }

        loNameField = (TextInputEditText) findViewById(R.id.loNameId);
        todayTotalDemandField = (TextInputEditText) findViewById(R.id.totalDemandId);
        todayTotalCollectionField = (TextInputEditText) findViewById(R.id.totalCollectionId);
        totalCpField = (TextInputEditText) findViewById(R.id.totalCpId);
        totalPrincipalDemandField = (TextInputEditText) findViewById(R.id.totalPrincipalDemandId);
        totalInterestDemandField = (TextInputEditText) findViewById(R.id.totalInterestDemandId);
        totalPrincipalCollectionField = (TextInputEditText) findViewById(R.id.totalPrincipalCollectionId);
        totalInterestCollectionField = (TextInputEditText) findViewById(R.id.totalInterestCollectionId);

        demandView = (TextView) findViewById(R.id.demandId);
        collectionView = (TextView) findViewById(R.id.collectionId);

        lm1 = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        if( !lm1.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("GPS not found")  // GPS not found
                    .setMessage("Kindly enable GPS") // Want to enable?
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent memberIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(memberIntent);
                            finish();
                        }
                    })
                    .show();
        }

        populateDashboard();

        fab.setVisibility(View.GONE);


    }



    private void populateDashboard()
    {
        new ApiHandler.GetAsync(HomeActivity.this).execute("dashboardForLo");
    }

    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if(key.equals("dashboardForLo"))
        {
            setDashBoardAdapter(result);
        }
    }

    private void setDashBoardAdapter(String result)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(result);

            loname = jsonObject.getString("loName");
            totalDemand = jsonObject.optInt("toadyTotalDemand");
            totalCollection = jsonObject.optInt("toadyTotalCollection");
            totalCp = jsonObject.optInt("totalCp");
            totalPrincipalDemand = jsonObject.optInt("totalPrincipalDemand");
            totalInterestDemand = jsonObject.optInt("totalInterestDemand");
            totalPrincipalCollection = jsonObject.optInt("totalPrincipalColl");
            totalInterestCollection = jsonObject.optInt("totalInterestColl");

            loNameField.setText(String.valueOf(loname));
            todayTotalDemandField.setText(String.valueOf(totalDemand));
            todayTotalCollectionField.setText(String.valueOf(totalCollection));
            totalCpField.setText(String.valueOf(totalCp));
            totalPrincipalDemandField.setText(String.valueOf(totalPrincipalDemand));
            totalInterestDemandField.setText(String.valueOf(totalInterestDemand));
            totalPrincipalCollectionField.setText(String.valueOf(totalPrincipalCollection));
            totalInterestCollectionField.setText(String.valueOf(totalInterestCollection));

            demandView.setText(String.valueOf(totalDemand));
            collectionView.setText(String.valueOf(totalCollection));

            loNameField.setEnabled(false);
            todayTotalDemandField.setEnabled(false);
            todayTotalCollectionField.setEnabled(false);
            totalCpField.setEnabled(false);
            totalPrincipalDemandField.setEnabled(false);
            totalInterestDemandField.setEnabled(false);
            totalPrincipalCollectionField.setEnabled(false);
            totalInterestCollectionField.setEnabled(false);


            JSONArray jsonArray = new JSONArray(jsonObject.getString("demandColletionData"));
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Dashboard dashboard = new Dashboard(
                        jsonObject1.getString("cpName"),
                        jsonObject1.optDouble("principalDemand"),
                        jsonObject1.optDouble("interestDemand"),
                        jsonObject1.optDouble("principalCollection"),
                        jsonObject1.optDouble("interestCollection")
                );
                dashboardLists.add(dashboard);
            }

            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
            {
                recyclerView = findViewById(R.id.homeRecyclerId);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                HomepageAdapterMobileVersion homepageAdapterMobileVersion = new HomepageAdapterMobileVersion(dashboardLists);
                recyclerView.setAdapter(homepageAdapterMobileVersion);
            }
            else
            {
                recyclerView = findViewById(R.id.homeRecyclerId);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                HomepageAdapter homepageAdapter = new HomepageAdapter(dashboardLists);
                recyclerView.setAdapter(homepageAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
