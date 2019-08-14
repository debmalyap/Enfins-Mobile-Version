package com.qbent.enfinsapp;

//---Developed by Debmalya---//
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.qbent.enfinsapp.adapter.HomepageAdapter;
import com.qbent.enfinsapp.adapter.HomepageAdapterMobileVersion;
import com.qbent.enfinsapp.adapter.MemberEligibityRecyclerViewAdapter;
import com.qbent.enfinsapp.adapter.MemberEligibityRecyclerViewAdapterMobileVersion;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.MemberEligibility;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemberEligibityCheckActivity extends MainActivity implements ApiCallback
{
//    TelephonyManager mTelephonyManager;
//    int PERMISSIONS_REQUEST_INTERNET,PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 0;



    EditText memberName;
    EditText memberAadharNo;
    ImageButton searchMember;
    private RecyclerView myRecyclerView;
    private List<MemberEligibility> memberEligibities = new ArrayList<MemberEligibility>();
    Boolean isListScrolling = false;
    int memberCurrentItems, memberTotalItems, memberScrollOutItems;
    LinearLayoutManager manager;
    MemberEligibityRecyclerViewAdapter memberEligibityRecyclerViewAdapter;
    MemberEligibityRecyclerViewAdapterMobileVersion memberEligibityRecyclerViewAdapterMobileVersion;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_member_eligibity_check_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_membereligibitycheck);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_member_eligibity_check, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_membereligibitycheck);
        }

        memberName = (EditText)findViewById(R.id.searchMemberEligibityName);
        memberName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        memberAadharNo = (EditText)findViewById(R.id.searchMemberEligibityAadhar);
        searchMember = (ImageButton) findViewById(R.id.searchMemberEligibity);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            myRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMembersEligibity);
            manager = new LinearLayoutManager(MemberEligibityCheckActivity.this);
            myRecyclerView.setLayoutManager(manager);
            memberEligibityRecyclerViewAdapterMobileVersion = new MemberEligibityRecyclerViewAdapterMobileVersion(memberEligibities);
            myRecyclerView.setAdapter(memberEligibityRecyclerViewAdapterMobileVersion);

        }
        else
        {
            myRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMembersEligibity);
            manager = new LinearLayoutManager(MemberEligibityCheckActivity.this);
            myRecyclerView.setLayoutManager(manager);
            memberEligibityRecyclerViewAdapter = new MemberEligibityRecyclerViewAdapter(memberEligibities);
            myRecyclerView.setAdapter(memberEligibityRecyclerViewAdapter);

        }

//        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMembersEligibity);
//        manager = new LinearLayoutManager(MemberEligibityCheckActivity.this);
//        myRecyclerView.setLayoutManager(manager);
//        memberEligibityRecyclerViewAdapter = new MemberEligibityRecyclerViewAdapter(memberEligibities);
//        myRecyclerView.setAdapter(memberEligibityRecyclerViewAdapter);

        searchMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                memberEligibities.clear();
                populateMembersEligibity(0);
            }
        });
        populateMembersEligibity(0);

        fab.setVisibility(View.GONE);


    }

    @Override
    public void onApiRequestStart() throws IOException {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        try {
            if (key.equals("get-creditData-list")) {

                JSONObject resultJson = new JSONObject(result);
                JSONArray jsonArray = new JSONArray(resultJson.getString("result"));
                //System.out.println(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String form_Date = jsonObject.getString("checkedOn").substring(0,10);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = dateFormat.parse(form_Date);
                    SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy");

                    MemberEligibility memberEligibity = new MemberEligibility(
                            jsonObject.getString("memberId"),
                            jsonObject.getString("loanProductId"),
                            jsonObject.getString("memberCode"),
                            jsonObject.getString("memberName"),
                            jsonObject.getLong("aadharNo"),
                            jsonObject.getString("collectionPoint"),
                            jsonObject.getString("organizationName"),
                            jsonObject.getString("loanProductName"),
                            jsonObject.getString("loanOfficerName"),
                            fmtOut.format(date),
                            jsonObject.getString("status")
                    );
                    memberEligibities.add(memberEligibity);
                }


                if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
                {
                    memberEligibityRecyclerViewAdapterMobileVersion.notifyDataSetChanged();
                }
                else
                {
                    memberEligibityRecyclerViewAdapter.notifyDataSetChanged();
                }

                //memberEligibityRecyclerViewAdapter.notifyDataSetChanged();


                myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            isListScrolling = true;
                        }
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView myRecyclerView, int dx, int dy) {
                        super.onScrolled(myRecyclerView, dx, dy);
                        memberCurrentItems = manager.getChildCount();
                        memberTotalItems = manager.getItemCount();
                        memberScrollOutItems = manager.findFirstVisibleItemPosition();

                        if (isListScrolling && (memberCurrentItems + memberScrollOutItems == memberTotalItems)) {
                            isListScrolling = false;
                            populateMembersEligibity(memberTotalItems);
                        }
                    }
                });

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateMembersEligibity(int memberTotalItems)
    {
        ApiRequest apiRequest = new ApiRequest("get-creditData-list");
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("aadharNo", memberAadharNo.getText().toString());
            String test = memberName.getText().toString();
            if(TextUtils.isEmpty(test))
            {
                jsonObject.accumulate("fullName", null);
            }
            else
            {
                jsonObject.accumulate("fullName", memberName.getText().toString());
            }
            jsonObject.accumulate("limit", 50);
            jsonObject.accumulate("order", "");
            jsonObject.accumulate("page", ((memberTotalItems+50)/50));
            apiRequest.set_t(jsonObject);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        new ApiHandler.PostAsync(MemberEligibityCheckActivity.this).execute(apiRequest);
    }
}
//---Ended by Debmalya---//
