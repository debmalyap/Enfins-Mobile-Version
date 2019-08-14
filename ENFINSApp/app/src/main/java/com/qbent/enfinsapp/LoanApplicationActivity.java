package com.qbent.enfinsapp;

//---Developed by Debmalya---//
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.qbent.enfinsapp.adapter.HomepageAdapter;
import com.qbent.enfinsapp.adapter.HomepageAdapterMobileVersion;
import com.qbent.enfinsapp.adapter.LoanApplicationListRecyclerViewAdapter;
import com.qbent.enfinsapp.adapter.LoanApplicationListRecyclerViewAdapterMobile;
import com.qbent.enfinsapp.model.LoanApplication;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanApplicationActivity extends MainActivity implements ApiCallback
{
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private List<LoanApplication> loanApplications = new ArrayList<LoanApplication>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_loan_application);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_loan_application, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_loanapplication);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Hello First Activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(getApplicationContext(), LoanApplicationDetailsAddActivity.class));

            }
        });

        populateloanApplications();


    }





    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if(key.equals("all-loan-Applications"))
        {
            setLoanApplicationAdapter(result);
        }
    }

    private void setLoanApplicationAdapter(String result)
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            JSONArray jsonArray = new JSONArray(result);
            System.out.println(jsonArray.length());
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                String form_Date = jsonObject.getString("applicationDate").substring(0,10);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(form_Date);
                SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");

                LoanApplication loanApplication = new LoanApplication(
                        jsonObject.getString("id"),
                        jsonObject.getString("applicationNo"),
                        fmtOut.format(date),
                        jsonObject.getString("branchName"),
                        jsonObject.getString("collectionPoint"),
                        jsonObject.getString("loanOfficer"),
                        jsonObject.getString("borrowerName"),
                        jsonObject.getString("coBorrowerName"),
                        jsonObject.getString("loanProductName"),
                        jsonObject.getString("loanPurposeDesc"),
                        jsonObject.getString("status"),
                        jsonObject.getString("nextAction")
                );
                loanApplications.add(loanApplication);
            }

            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
            {
                recyclerView = findViewById(R.id.recyclerViewLoanApplications);
                recyclerView.setLayoutManager(new LinearLayoutManager(LoanApplicationActivity.this));
                LoanApplicationListRecyclerViewAdapterMobile loanApplicationListRecyclerViewAdapterMobile = new LoanApplicationListRecyclerViewAdapterMobile(loanApplications);
                recyclerView.setAdapter(loanApplicationListRecyclerViewAdapterMobile);
            }
            else
            {
                recyclerView = findViewById(R.id.recyclerViewLoanApplications);
                recyclerView.setLayoutManager(new LinearLayoutManager(LoanApplicationActivity.this));
                LoanApplicationListRecyclerViewAdapter loanApplicationListRecyclerViewAdapter = new LoanApplicationListRecyclerViewAdapter(loanApplications);
                recyclerView.setAdapter(loanApplicationListRecyclerViewAdapter);
            }





        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void populateloanApplications()
    {
        new ApiHandler.GetAsync(LoanApplicationActivity.this).execute("all-loan-Applications");
    }

    private String convertTime(String time) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm aa MM-dd");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);

        return convertedDate;
    }
}
//---Ended by Debmalya---//