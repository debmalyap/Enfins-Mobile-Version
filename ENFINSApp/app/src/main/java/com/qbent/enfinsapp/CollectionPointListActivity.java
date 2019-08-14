package com.qbent.enfinsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;

import com.qbent.enfinsapp.adapter.CollectionPointListRecyclerViewAdapter;
import com.qbent.enfinsapp.adapter.MemberRecyclerViewAdapter;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.CollectionPoint;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CollectionPointListActivity extends MainActivity implements ApiCallback
{
    private List<CollectionPoint> collectionPoints = new ArrayList<CollectionPoint>();

    //---Edited by Debmalya---//
    EditText collectionPointName;
    Button searchCp;

    private RecyclerView recyclerView;
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    LinearLayoutManager linearLayoutManager;
    CollectionPointListRecyclerViewAdapter searchAdapter;
    //---Ended by Debmalya---//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_collection_point_list, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_collection_point);

        //---Edited by Debmalya---//
        collectionPointName = (EditText)findViewById(R.id.searchCollectionPointNameId);
        searchCp = (Button) findViewById(R.id.searchCollectionPointButtonId);

        recyclerView = findViewById(R.id.recyclerViewCollectionPoints);
        linearLayoutManager = new LinearLayoutManager(CollectionPointListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        searchAdapter = new CollectionPointListRecyclerViewAdapter(collectionPoints);
        recyclerView.setAdapter(searchAdapter);

        searchCp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                collectionPoints.clear();
                populateCollectionPointNames(0);
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Hello First Activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(getApplicationContext(), CollectionPointDetailActivity.class));
            }
        });



        populateCollectionPointNames(0);
    }



    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        try {
            if (key.equals("collection-points")) {

                JSONObject resultJson = new JSONObject(result);
                JSONArray jsonArray = new JSONArray(resultJson.getString("result"));
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    CollectionPoint collectionPoint = new CollectionPoint(
                            jsonObject.getString("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("address"),
                            jsonObject.getString("mobileNo"),
                            jsonObject.getString("collectionDayName"),
                            jsonObject.getString("lat"),
                            jsonObject.getString("lon")
                    );
                    collectionPoints.add(collectionPoint);
                }

                searchAdapter.notifyDataSetChanged();

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
                        currentItems = linearLayoutManager.getChildCount();
                        totalItems = linearLayoutManager.getItemCount();
                        scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                        if(isScrolling && (currentItems + scrollOutItems == totalItems) && totalItems%20==0)
                        {
                            isScrolling = false;
                            populateCollectionPointNames(totalItems);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



//    private void populateCollectionPoints()
//    {
//        new ApiHandler.GetAsync(CollectionPointListActivity.this).execute("collection-points");
//    }

    private void populateCollectionPointNames(int totalItems)
    {
        ApiRequest apiRequest = new ApiRequest("collection-points");
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("filter", collectionPointName.getText().toString());

            jsonObject.accumulate("name", "");
            jsonObject.accumulate("limit", 20);
            jsonObject.accumulate("order", "");
            jsonObject.accumulate("page", ((totalItems+20)/20));
            apiRequest.set_t(jsonObject);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        new ApiHandler.PostAsync(CollectionPointListActivity.this).execute(apiRequest);
    }
}
