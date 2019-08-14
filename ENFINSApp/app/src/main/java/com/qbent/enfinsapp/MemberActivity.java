package com.qbent.enfinsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;

import com.qbent.enfinsapp.adapter.MemberRecyclerViewAdapter;
import com.qbent.enfinsapp.adapter.MemberRecyclerViewAdapterMobile;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Member;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemberActivity extends MainActivity implements ApiCallback
{
//    TelephonyManager mTelephonyManager;
//    int PERMISSIONS_REQUEST_INTERNET,PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 0;
//    private String imei;

    EditText Name;
    EditText AadharNo;
    Button Search;
    private RecyclerView recyclerView;
    private List<Member> members = new ArrayList<Member>();
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    LinearLayoutManager manager;
    MemberRecyclerViewAdapter mAdapter;
    MemberRecyclerViewAdapterMobile memberRecyclerViewAdapterMobile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Configuration config = getResources().getConfiguration();

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_member_normal, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_member);
        }
        else
        {
            System.out.println("Large Screen");
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_member, null, false);
            drawer.addView(contentView, 0);
            navigationView.setCheckedItem(R.id.nav_member);
        }



        Name = (EditText)findViewById(R.id.searchMemberName);
        AadharNo = (EditText)findViewById(R.id.searchMemberAadhar);
        Search = (Button) findViewById(R.id.searchMember);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            recyclerView = findViewById(R.id.recyclerViewMembers);
            manager = new LinearLayoutManager(MemberActivity.this);
            recyclerView.setLayoutManager(manager);
            memberRecyclerViewAdapterMobile = new MemberRecyclerViewAdapterMobile(members);
            recyclerView.setAdapter(memberRecyclerViewAdapterMobile);
        }
        else
        {
            recyclerView = findViewById(R.id.recyclerViewMembers);
            manager = new LinearLayoutManager(MemberActivity.this);
            recyclerView.setLayoutManager(manager);
            mAdapter = new MemberRecyclerViewAdapter(members);
            recyclerView.setAdapter(mAdapter);
        }


        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                members.clear();
                populateMembers(0);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QBarCodeActivity.class));
            }
        });

        Drawable image = getDrawable(R.drawable.download_btn_bg);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        Search.setBackground(rippledBg);

        populateMembers(0);



    }



    @Override
    public void onApiRequestStart() throws IOException {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException {
//        members.clear();
        try {
            if (key.equals("search-member")) {

                JSONObject resultJson = new JSONObject(result);
                JSONArray jsonArray = new JSONArray(resultJson.getString("result"));
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    Member member = new Member(
                        jsonObject.getString("id"),
                        jsonObject.getString("code"),
                        jsonObject.getString("fullName"),
                        jsonObject.optLong("aadharNo"),
                        jsonObject.getString("dateOfDeath"),
                        jsonObject.getString("guardianName"),
                        jsonObject.getString("collectionPointName"),
                        jsonObject.getString("visitStatus"),
                        jsonObject.getString("latByLo"),
                        jsonObject.getString("lonByLo"),
                        jsonObject.getString("latByBm"),
                        jsonObject.getString("lonByBm")
                    );
                    members.add(member);
                }


                if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
                {
                    memberRecyclerViewAdapterMobile.notifyDataSetChanged();
                }
                else
                {
                    mAdapter.notifyDataSetChanged();
                }





                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                        {
                            isScrolling = true;
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        currentItems = manager.getChildCount();
                        totalItems = manager.getItemCount();
                        scrollOutItems = manager.findFirstVisibleItemPosition();

                        if(isScrolling && (currentItems + scrollOutItems == totalItems) && totalItems%50==0)
                        {
                            isScrolling = false;
                            populateMembers(totalItems);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateMembers(int totalItems) {
        ApiRequest apiRequest = new ApiRequest("search-member");
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("fullName", Name.getText().toString());
            String test = AadharNo.getText().toString();
            if(TextUtils.isEmpty(test))
            {
                jsonObject.accumulate("aadharNo", null);
            }
            else
            {
                jsonObject.accumulate("aadharNo", Long.parseLong(AadharNo.getText().toString()));
            }
            jsonObject.accumulate("collectionPointName", "");
            jsonObject.accumulate("limit", 50);
            jsonObject.accumulate("order", "");
            jsonObject.accumulate("page", ((totalItems+50)/50));
            apiRequest.set_t(jsonObject);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        new ApiHandler.PostAsync(MemberActivity.this).execute(apiRequest);
    }
}
