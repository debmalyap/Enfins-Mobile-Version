package com.qbent.enfinsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.qbent.enfinsapp.model.Branch;
import com.qbent.enfinsapp.model.CollPoint;
import com.qbent.enfinsapp.model.LoanOfficer;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class CollectionSheetActivity extends MainActivity implements ApiCallback
{
    private DatePickerDialog.OnDateSetListener collectionDateListener;

    String emptyGuid = "00000000-0000-0000-0000-000000000000",CollectionDate;

    private Date collectionDate;

    Boolean editBranchChanged = false;
    Boolean editLoanOfficerChanged = false;
    Boolean editCollectionPointChanged = false;

    Spinner collectionBranchSpinner;
    Spinner loanOfficerNameSpinner;
    Spinner collectionPointNameSpinner;

    EditText collectionDateField;

    Button generateCollection,cancelCollection;

    private List<Branch> collectionBranchLists;
    private List<LoanOfficer> loanOfficerList;
    private List<CollPoint> collectionPointNamesList;

    HashMap<String, String> spinnerCollectionSheetBranchMap = new HashMap<String, String>();
    HashMap<String, String> spinnerLoanOfficerMap = new HashMap<String, String>();
    HashMap<String, String> spinnerCollectionPointNamesMap = new HashMap<String, String>();

    private String branchId = " ",loanOfficerId = " ",CollectionPointId = " ";

    private String user_branch,user_loan_officer,user_collection_point;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_collection_sheet, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_schedulecalculator);

        collectionBranchSpinner = (Spinner) findViewById(R.id.collectionBranchSpinnerId);
        loanOfficerNameSpinner = (Spinner) findViewById(R.id.collectionLoanOfficerNameId);
        collectionPointNameSpinner = (Spinner) findViewById(R.id.collectionPointsNamesId);

        collectionBranchLists = new ArrayList<Branch>();
        loanOfficerList = new ArrayList<LoanOfficer>();
        collectionPointNamesList = new ArrayList<CollPoint>();

        generateCollection = (Button) findViewById(R.id.generateSheetButtonId);
        cancelCollection = (Button) findViewById(R.id.cancelSheetButtonId);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        generateCollection.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        cancelCollection.setBackground(rippledBg1);

        collectionDateField = (EditText) findViewById(R.id.collectionDateId);
        collectionDateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        //---Collection date field ---//
        collectionDateField.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CollectionSheetActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        collectionDateListener,
                        year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        collectionDateListener = new DatePickerDialog.OnDateSetListener()
        {
            @SuppressLint("LongLogTag")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month += 1;
                CollectionDate = day + "/" +month+ "/" +year;
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                try
                {
                    collectionDate = simpleDateFormat1.parse(CollectionDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                collectionDateField.setText(simpleDateFormat1.format(collectionDate));

            }
        };
        //---End of Collection date field ---//

        //---Branch spinner---//
        collectionBranchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_branch!=null && !user_branch.equals("null")&& !user_branch.equals(emptyGuid) && editBranchChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerCollectionSheetBranchMap.keySet());
                    int test = indexes1.indexOf(user_branch);
                    String test2 = (new ArrayList<String>(spinnerCollectionSheetBranchMap.values())).get(test);
                    collectionBranchSpinner.setSelection(((ArrayAdapter<String>)collectionBranchSpinner.getAdapter()).getPosition(test2));
                    branchId = user_branch;
                }
                else
                {
                    String name = collectionBranchSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerCollectionSheetBranchMap.values());
                    int a = indexes.indexOf(name);
                    branchId = (new ArrayList<String>(spinnerCollectionSheetBranchMap.keySet())).get(indexes.indexOf(name));
                }
                populateLoanOfficerNames(branchId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//
            }
        });
        collectionBranchSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editBranchChanged = true;
                }
                return false;
            }
        });
        //---End of branch spinner---//

        //---Loan officer spinner---//
        loanOfficerNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_loan_officer!=null && !user_loan_officer.equals("null")&& !user_loan_officer.equals(emptyGuid) && editLoanOfficerChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerLoanOfficerMap.keySet());
                    int test = indexes1.indexOf(user_loan_officer);
                    String test2 = (new ArrayList<String>(spinnerLoanOfficerMap.values())).get(test);
                    loanOfficerNameSpinner.setSelection(((ArrayAdapter<String>)loanOfficerNameSpinner.getAdapter()).getPosition(test2));
                    loanOfficerId = user_loan_officer;
                }
                else
                {
                    String name = loanOfficerNameSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerLoanOfficerMap.values());
                    int a = indexes.indexOf(name);
                    loanOfficerId = (new ArrayList<String>(spinnerLoanOfficerMap.keySet())).get(indexes.indexOf(name));
                }
                populateCollectionPointNames(loanOfficerId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        loanOfficerNameSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editLoanOfficerChanged = true;
                }
                return false;
            }
        });
        //---End of loan officer spinner---//

        //---Collection point names spinner---//
        collectionPointNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_collection_point!=null && !user_collection_point.equals("null")&& !user_collection_point.equals(emptyGuid) && editCollectionPointChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerCollectionPointNamesMap.keySet());
                    int test = indexes1.indexOf(user_collection_point);
                    String test2 = (new ArrayList<String>(spinnerCollectionPointNamesMap.values())).get(test);
                    collectionPointNameSpinner.setSelection(((ArrayAdapter<String>)collectionPointNameSpinner.getAdapter()).getPosition(test2));
                    CollectionPointId = user_loan_officer;
                }
                else
                {
                    String name = collectionPointNameSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerCollectionPointNamesMap.values());
                    int a = indexes.indexOf(name);
                    CollectionPointId = (new ArrayList<String>(spinnerCollectionPointNamesMap.keySet())).get(indexes.indexOf(name));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        //---End of collection point spinner---//



        //---generate collection sheet button---//

        generateCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(collectionBranchSpinner.getSelectedItemPosition() == 0)
                {
                    showAlert("Please select a branch");
                    return;
                }
                else if(loanOfficerNameSpinner.getSelectedItemPosition() == 0)
                {
                    showAlert("Please select a loan officer");
                    return;
                }
                else if(collectionPointNameSpinner.getSelectedItemPosition() == 0)
                {
                    showAlert("Please select a collection point");
                    return;
                }
                else if(collectionDateField.getText().toString().length() == 0)
                {
                    showAlert("Please select collection date");
                    return;
                }
                else if(!collectionDate.toString().startsWith("M"))
                {
                    showAlert("Please select monday");
                    return;
                }
                else if(collectionBranchSpinner.getSelectedItemPosition() !=0 && loanOfficerNameSpinner.getSelectedItemPosition() !=0
                        && collectionPointNameSpinner.getSelectedItemPosition() !=0 && collectionDateField.getText().toString().length() != 0
                        && collectionDate.toString().startsWith("M"))
                {

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://192.168.0.65:80/reportView/installment-collection/" + CollectionPointId + "/" + CollectionDate ));
                    request.setDescription("Instalment Calculator Info");
                    request.setTitle("Instalment Report");
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "CollectionReport_" + CollectionPointId + ".pdf");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    DownloadManager manager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);

                    final AlertDialog.Builder builder = new AlertDialog.Builder(CollectionSheetActivity.this);
                    builder.setTitle("ENFIN Admin");
                    builder.setMessage("Instalment report successfully downloaded");
                    builder.setCancelable(true);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });
        //---End of generate button---//

        cancelCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
        populateCollectionSheetBranchNames();

        fab.setVisibility(View.GONE);
    }

    private void showAlert(String error)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CollectionSheetActivity.this);
        builder.setTitle("ENFIN Admin");
        builder.setMessage(error);
        builder.setCancelable(true);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //---End of onActivity()---//

    private void populateCollectionSheetBranchNames()
    {
        new ApiHandler.GetAsync(CollectionSheetActivity.this).execute("getUserWiseBranch");
    }

    private void populateLoanOfficerNames(String branchId)
    {
        new ApiHandler.GetAsync(CollectionSheetActivity.this).execute("getLoByBranchId/{"+ branchId +"}");
    }

    private void populateCollectionPointNames(String loanOfficerId)
    {
        new ApiHandler.GetAsync(CollectionSheetActivity.this).execute("getCpByLo/{"+ loanOfficerId +"}");
    }

    @Override
    public void onApiRequestStart() throws IOException
    {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException
    {
        if(key.contains("getUserWiseBranch"))
        {
            setCollectionSheetBranchAdapter(result);
        }
        else if(key.contains("getLoByBranchId"))
        {
            setCollectionSheetLoanOfficerAdapter(result);
        }
        else if(key.contains("getCpByLo"))
        {
            setCollectionSheetCollectionPointNamesAdapter(result);
        }
    }

    private void setCollectionSheetBranchAdapter(String result)
    {
        try {

            collectionBranchLists = new ArrayList<Branch>();
            JSONArray jsonArray = new JSONArray(result);
            Branch branch1 = new Branch(emptyGuid, "Select Branch");
            collectionBranchLists.add(branch1);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                Branch branch = new Branch(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                collectionBranchLists.add(branch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerCollectionSheetBranchArray = new String[collectionBranchLists.size()];
        for (int i = 0; i < collectionBranchLists.size(); i++)
        {
            spinnerCollectionSheetBranchMap.put(collectionBranchLists.get(i).getId(),collectionBranchLists.get(i).getName());
            spinnerCollectionSheetBranchArray[i] = collectionBranchLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionSheetActivity.this, android.R.layout.simple_spinner_item,spinnerCollectionSheetBranchArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        collectionBranchSpinner.setAdapter(adapter);
    }

    private void setCollectionSheetLoanOfficerAdapter(String result)
    {
        try {

            loanOfficerList = new ArrayList<LoanOfficer>();
            JSONArray jsonArray = new JSONArray(result);
            LoanOfficer loanOfficer1 = new LoanOfficer(emptyGuid, "Select loan officer name");
            loanOfficerList.add(loanOfficer1);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                LoanOfficer loanOfficer = new LoanOfficer(
                        jsonObject.getString("loanOfficerId"),
                        jsonObject.getString("fullName")
                );
                loanOfficerList.add(loanOfficer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerCollectionSheetLoanOfficerArray = new String[loanOfficerList.size()];
        for (int i = 0; i < loanOfficerList.size(); i++)
        {
            spinnerLoanOfficerMap.put(loanOfficerList.get(i).getLoanOfficerId(),loanOfficerList.get(i).getFullName());
            spinnerCollectionSheetLoanOfficerArray[i] = loanOfficerList.get(i).getFullName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionSheetActivity.this, android.R.layout.simple_spinner_item,spinnerCollectionSheetLoanOfficerArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        loanOfficerNameSpinner.setAdapter(adapter);
    }

    private void setCollectionSheetCollectionPointNamesAdapter(String result)
    {
        try {

            collectionPointNamesList = new ArrayList<CollPoint>();
            JSONArray jsonArray = new JSONArray(result);
            CollPoint collPoint1 = new CollPoint(emptyGuid, "Select collection point names");
            collectionPointNamesList.add(collPoint1);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                CollPoint collPoint = new CollPoint(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                collectionPointNamesList.add(collPoint);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerCollectionPointNamesArray = new String[collectionPointNamesList.size()];
        for (int i = 0; i < collectionPointNamesList.size(); i++)
        {
            spinnerCollectionPointNamesMap.put(collectionPointNamesList.get(i).getId(),collectionPointNamesList.get(i).getName());
            spinnerCollectionPointNamesArray[i] = collectionPointNamesList.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionSheetActivity.this, android.R.layout.simple_spinner_item,spinnerCollectionPointNamesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        collectionPointNameSpinner.setAdapter(adapter);
    }
}
