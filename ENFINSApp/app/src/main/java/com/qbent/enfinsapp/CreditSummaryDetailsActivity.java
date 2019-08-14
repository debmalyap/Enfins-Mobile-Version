package com.qbent.enfinsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qbent.enfinsapp.adapter.CreditSummartListRecyclerViewMobile;
import com.qbent.enfinsapp.adapter.CreditSummaryListRecyclerViewAdapter;
import com.qbent.enfinsapp.model.CreditSummary;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreditSummaryDetailsActivity extends MainActivity implements ApiCallback
{
    private String memberId = " ";

    private RecyclerView recyclerView;
    private List<CreditSummary> creditSummaries = new ArrayList<CreditSummary>();

    TextView textView;
    Button button;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_credit_summary_details, null, false);
        drawer.addView(contentView, 0);
        textView = (TextView) findViewById(R.id.textCreditSummaryNoData);
        button = (Button) findViewById(R.id.backToMemberSummary);

        Drawable image2 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg2 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image2, null);
        button.setBackground(rippledBg2);

        fab.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent memberIntent = new Intent(getApplicationContext(),MemberActivity.class);
                startActivity(memberIntent);
            }
        });

        //setContentView(R.layout.activity_credit_summary_details);

        memberId = getIntent().getStringExtra("member_id");

        populateCreditSummaryDetails(memberId);
    }

    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if (key.contains("getCreditDataChkDate"))
        {
            setCreditSummaryAdapter(result);
        }
    }

    private void setCreditSummaryAdapter(String result)
    {
        try {
            creditSummaries = new ArrayList<CreditSummary>();
            JSONArray jsonArray = new JSONArray(result);

            if(jsonArray.length() == 0)
            {
                textView.setVisibility(View.VISIBLE);
                return;
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String form_Date = jsonObject.getString("checkingDate");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(form_Date);

                SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
                CreditSummary creditSummary = new CreditSummary(
                        jsonObject.getString("id"),
                        fmtOut.format(date),
                        jsonObject.getString("organizationName")
                );
                creditSummaries.add(creditSummary);
            }

            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
            {
                recyclerView = findViewById(R.id.recyclerViewCreditSummaryDetails);
                recyclerView.setLayoutManager(new LinearLayoutManager(CreditSummaryDetailsActivity.this));
                CreditSummartListRecyclerViewMobile creditSummartListRecyclerViewMobile = new CreditSummartListRecyclerViewMobile(creditSummaries);
                recyclerView.setAdapter(creditSummartListRecyclerViewMobile);
            }
            else
            {
                recyclerView = findViewById(R.id.recyclerViewCreditSummaryDetails);
                recyclerView.setLayoutManager(new LinearLayoutManager(CreditSummaryDetailsActivity.this));
                CreditSummaryListRecyclerViewAdapter creditSummaryListRecyclerViewAdapter = new CreditSummaryListRecyclerViewAdapter(creditSummaries);
                recyclerView.setAdapter(creditSummaryListRecyclerViewAdapter);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateCreditSummaryDetails(String memberId)
    {
        new ApiHandler.GetAsync(CreditSummaryDetailsActivity.this).execute("getCreditDataChkDate/{" + memberId + "}");
    }
}
