package com.qbent.enfinsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qbent.enfinsapp.adapter.CollectionPointListRecyclerViewAdapter;
import com.qbent.enfinsapp.adapter.HouseVisitQuestionAdapter;
import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.CollectionPoint;
import com.qbent.enfinsapp.model.LoanHouseVisit;
import com.qbent.enfinsapp.model.LoanHouseVisitDetail;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoanHouseVisitActivity extends MainActivity implements ApiCallback {

    TextView totalScoreField;
    EditText applicationNo, borrowerName, loanProduct, loanPurpose;
    Button savehv, submithv, cancelhv;
    String loanApplicationId, appNo, borName, loProduct, loPurpose, totalScore, loanEventId;
    HouseVisitQuestionAdapter houseVisitQuestionAdapter;
    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    private RecyclerView recyclerView;
    private List<LoanHouseVisitDetail> detail = new ArrayList<LoanHouseVisitDetail>();

    LocationManager lm;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_loan_house_visit_normal, null, false);
            drawer.addView(contentView, 0);
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View contentView = inflater.inflate(R.layout.activity_loan_house_visit, null, false);
            drawer.addView(contentView, 0);
        }

        loanApplicationId = getIntent().getStringExtra("loan_application_id");

        final AlertDialogue alertDialogue = new AlertDialogue(LoanHouseVisitActivity.this);


        applicationNo = (EditText) findViewById(R.id.houseVisit_Application_No);
        borrowerName = (EditText) findViewById(R.id.houseVisit_Borrower_Name);
        loanProduct = (EditText) findViewById(R.id.houseVisit_Loan_product);
        loanPurpose = (EditText) findViewById(R.id.houseVisit_Loan_purpose);
        savehv = (Button) findViewById(R.id.houseVisitSaveButton);
        submithv = (Button) findViewById(R.id.houseVisitSubmitButton);
        cancelhv = (Button) findViewById(R.id.houseVisitCancelButton);

        applicationNo.setEnabled(false);
        borrowerName.setEnabled(false);
        loanProduct.setEnabled(false);
        loanPurpose.setEnabled(false);

        totalScoreField = (TextView) findViewById(R.id.houseVisitTotalScore);
        totalScoreField.setText("-15");
        fab.setVisibility(View.GONE);

        populateHouseVisits(loanApplicationId);

        checkLocationPermission();

        lm = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);



        cancelhv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent memberIntent = new Intent(getApplicationContext(), LoanApplicationActivity.class);
                startActivity(memberIntent);
            }
        });

        submithv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoanHouseVisitActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to submit house visit?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k) {
                        new ApiHandler.GetAsync(LoanHouseVisitActivity.this).execute("submit-loanApplication/{" + loanApplicationId + "}/{" + loanEventId + "}/" + 2);
                    }
                });
                builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                //populateCollectionPoints(collectionPointId);
            }
        });

        savehv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);






                if (ActivityCompat.checkSelfPermission(LoanHouseVisitActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoanHouseVisitActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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



//                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                double longitude = location.getLongitude();
//                double latitude = location.getLatitude();
                detail = houseVisitQuestionAdapter.totalData();
                for(int l = 0 ; l<detail.size();l++)
                {
                    if(detail.get(l).getTotalScore() == -1)
                    {
                        alertDialogue.showAlertMessage("Kindly select answer of all questions.");
                        return;
                    }
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoanHouseVisitActivity.this);
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Are you sure to save house visit?");
                builder.setCancelable(true);
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int k)
                    {
                        ApiRequest apiRequest = new ApiRequest("save-housevisitQuestion-lo");

                        JSONObject obj = null;
                        JSONArray jsonArray = new JSONArray();


                        for (int i = 0; i < detail.size(); i++) {
                            obj = new JSONObject();
                            try {
                                obj.put("questionId", detail.get(i).getQuestionId());
                                obj.put("question", detail.get(i).getQuestion());
                                obj.put("selectedScore", detail.get(i).getTotalScore());
                                obj.put("answer1", !detail.get(i).getAnswer1().equals("null")?detail.get(i).getAnswer1():null);
                                obj.put("answer2", !detail.get(i).getAnswer2().equals("null")?detail.get(i).getAnswer2():null);
                                obj.put("answer3", !detail.get(i).getAnswer3().equals("null")?detail.get(i).getAnswer3():null);
                                obj.put("answer4", !detail.get(i).getAnswer4().equals("null")?detail.get(i).getAnswer4():null);
                                obj.put("answer5", !detail.get(i).getAnswer5().equals("null")?detail.get(i).getAnswer5():null);
                                obj.put("answer6", !detail.get(i).getAnswer6().equals("null")?detail.get(i).getAnswer6():null);
                                obj.put("score1", !detail.get(i).getAnswer1().equals("null")?detail.get(i).getScore1():null);
                                obj.put("score2", !detail.get(i).getAnswer2().equals("null")?detail.get(i).getScore2():null);
                                obj.put("score3", !detail.get(i).getAnswer3().equals("null")?detail.get(i).getScore3():null);
                                obj.put("score4", !detail.get(i).getAnswer4().equals("null")?detail.get(i).getScore4():null);
                                obj.put("score5", !detail.get(i).getAnswer5().equals("null")?detail.get(i).getScore5():null);
                                obj.put("score6", !detail.get(i).getAnswer6().equals("null")?detail.get(i).getScore6():null);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            jsonArray.put(obj);
                        }
                        JSONObject finalobject = new JSONObject();
                        try {
                            finalobject.put("detail", jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            finalobject.put("loanApplicationId",loanApplicationId);
                            finalobject.put("latByLo",latitude);
                            finalobject.put("lonByLo",longitude);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        apiRequest.set_t(finalobject);
                        new ApiHandler.PostAsync(LoanHouseVisitActivity.this).execute(apiRequest);
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




        //houseVisitQuestionAdapter = new HouseVisitQuestionAdapter(detail,getApplicationContext());


    }



    private void populateHouseVisits(String loanApplicationId) {
        new ApiHandler.GetAsync(LoanHouseVisitActivity.this).execute("getQusetionByApplicationLo/{"+ loanApplicationId +"}");
    }

    @Override
    public void onApiRequestStart() throws IOException {

    }


    @Override
    public void onApiRequestComplete(String key, String result) throws IOException {
        if (key.contains("getQusetionByApplicationLo"))
        {
            setHouseVisitQuestion(result);
        }
        else if(key.contains("save-housevisitQuestion-lo"))
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(LoanHouseVisitActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("House Visit Saved Successfully!");
            builder1.setCancelable(true);
            builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface1, int i)
                {
                    Intent intent = new Intent(getApplicationContext(),LoanApplicationActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();

        }
        else if(key.contains("submit-loanApplication"))
        {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(LoanHouseVisitActivity.this);
            builder1.setTitle("ENFIN Admin");
            builder1.setMessage("House Visit Submitted Successfully!");
            builder1.setCancelable(true);
            builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface1, int i)
                {
                    Intent intent = new Intent(getApplicationContext(),LoanApplicationActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();

        }
    }

    public void totalScore(int total)
    {
        totalScoreField.setText(String.valueOf(total));
    }

    private void setHouseVisitQuestion(String result) {
        try {
                JSONObject jsonObject1 = new JSONObject(result);
                appNo = jsonObject1.getString("applicationNo");
                borName = jsonObject1.getString("borrowerName");
                loProduct = jsonObject1.getString("loanProduct");
                loPurpose = jsonObject1.getString("loanPurpose");
                loanEventId = jsonObject1.getString("loanEventId");

                if(jsonObject1.getString("id").equals(emptyGuid))
                {
                    submithv.setVisibility(View.GONE);
                }

//            "loanEventId":"d9d5b6d0-9ced-4d9c-b0b0-5e8437395a6c",

                applicationNo.setText(appNo);
                borrowerName.setText(borName);
                loanProduct.setText(loProduct);
                loanPurpose.setText(loPurpose);


                int test = -1;
                JSONArray jsonArray = jsonObject1.getJSONArray("detail");
                System.out.println(jsonArray.length());
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    LoanHouseVisitDetail loanHouseVisitDetail = new LoanHouseVisitDetail(
                            jsonObject.getString("houseVisitId"),
                            jsonObject.getString("questionId"),
                            jsonObject.getString("question"),
                            jsonObject.getString("answer1"),
                            jsonObject.optInt("score1"),
                            jsonObject.getString("answer2"),
                            jsonObject.optInt("score2"),
                            jsonObject.getString("answer3"),
                            jsonObject.optInt("score3"),
                            jsonObject.getString("answer4"),
                            jsonObject.optInt("score4"),
                            jsonObject.getString("answer5"),
                            jsonObject.optInt("score5"),
                            jsonObject.getString("answer6"),
                            jsonObject.optInt("score6"),
                            jsonObject.optInt("selectedScore")
                    );
                    detail.add(loanHouseVisitDetail);
                }


                recyclerView = findViewById(R.id.houseVisit_recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(LoanHouseVisitActivity.this));
                houseVisitQuestionAdapter = new HouseVisitQuestionAdapter(detail,LoanHouseVisitActivity.this);
                recyclerView.setAdapter(houseVisitQuestionAdapter);





               // houseAdapter

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("ENFINS ADMIN")
                        .setMessage("Kindly Give permission of location")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LoanHouseVisitActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
