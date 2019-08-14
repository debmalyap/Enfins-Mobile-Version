package com.qbent.enfinsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.qbent.enfinsapp.adapter.AutoCompleteAdapter;
import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Branch;
import com.qbent.enfinsapp.model.CollPoint;
import com.qbent.enfinsapp.model.Country;
import com.qbent.enfinsapp.model.PartyLedger;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartyLedgerActivity extends MainActivity implements ApiCallback
{
    String emptyGuid = "00000000-0000-0000-0000-000000000000";

    Boolean editBranchNameChanged = false;
    Boolean editCollectionPointChanged = false;

    String user_branch,user_collection;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;

    Spinner branchesSpinner, partyLedgerCollectionSpinner;

    private List<CollPoint> partyLedgerCollectionPointLists;
    private List<Branch> userWiseBranchesLists;

    Button partyCancelButton,partyPrintButton;

    EditText partyLoanBondNoField;

    HashMap<String, String> spinnerPartyLedgerCollectionPointNamesMap = new HashMap<String, String>();
    HashMap<String, String> spinnerUserWiseBranchesMap = new HashMap<String, String>();

    private String branchId = " ";
    private String collectionPointId = " ";
    private String loanId;
    private int instalAmount;

    private Handler partyHandler;
    private AutoCompleteAdapter partyAdapter;

    private AppCompatAutoCompleteTextView autoCompletePartyLoanBondNo;

    private List<PartyLedger> partyCollectionList;
    private List<String> partySearchText;

    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_party_ledger);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_party_ledger, null, false);
        drawer.addView(contentView, 0);
        navigationView.setCheckedItem(R.id.nav_partyledger);

        final AlertDialogue alertDialogue = new AlertDialogue(PartyLedgerActivity.this);

        userWiseBranchesLists = new ArrayList<Branch>();

        branchesSpinner = (Spinner) findViewById(R.id.branches);
        partyLedgerCollectionSpinner = (Spinner) findViewById(R.id.collectionPoints);

        fab.setVisibility(View.GONE);

        partyLoanBondNoField = (EditText) findViewById(R.id.partyLoanBondNumber);
        partyPrintButton = (Button) findViewById(R.id.printButton);
        partyCancelButton = (Button) findViewById(R.id.cancelButton);

        Drawable image = getDrawable(R.drawable.save_btn);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image, null);
        partyPrintButton.setBackground(rippledBg);

        Drawable image1 = getDrawable(R.drawable.cancel_btn);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        partyCancelButton.setBackground(rippledBg1);


        partyLoanBondNoField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                view.setFocusable(false);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        //partyLoanBondNoField.clearFocus();


        partyLoanBondNoField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                saveAfterText(alertDialogue);
            }
        });

        //---Branch Name Spinner---//
        branchesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(user_branch!=null && !user_branch.equals("null")&& !user_branch.equals(emptyGuid) && !branchId.equals(emptyGuid) && editBranchNameChanged == false)
                {
                    List<String> indexes1 = new ArrayList<String>(spinnerUserWiseBranchesMap.keySet());
                    int test = indexes1.indexOf(user_branch);
                    String test2 = (new ArrayList<String>(spinnerUserWiseBranchesMap.values())).get(test);
                    branchesSpinner.setSelection(((ArrayAdapter<String>)branchesSpinner.getAdapter()).getPosition(test2));
                    collectionPointId = user_branch;
                }
                else
                {
                    String name = branchesSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerUserWiseBranchesMap.values());
                    int a = indexes.indexOf(name);
                    collectionPointId = (new ArrayList<String>(spinnerUserWiseBranchesMap.keySet())).get(indexes.indexOf(name));
                }

                populatePartyLedgerCollectionPointNames(collectionPointId);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//
            }
        });

        branchesSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editBranchNameChanged = true;
                }
                return false;
            }
        });
        //---End of Branch Name Spinner---//


        //---Collection Point Spinner---//
        partyLedgerCollectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                loanId = null;
                if (user_collection != null && !user_collection.equals("null") && !user_collection.equals(emptyGuid) && editCollectionPointChanged == false) {
                    List<String> indexes1 = new ArrayList<String>(spinnerPartyLedgerCollectionPointNamesMap.keySet());
                    int test = indexes1.indexOf(user_collection);
                    String test2 = (new ArrayList<String>(spinnerPartyLedgerCollectionPointNamesMap.values())).get(test);
                    partyLedgerCollectionSpinner.setSelection(((ArrayAdapter<String>) partyLedgerCollectionSpinner.getAdapter()).getPosition(test2));
                    collectionPointId = user_collection;
                }
                else
                {
                    String name = partyLedgerCollectionSpinner.getSelectedItem().toString();
                    List<String> indexes = new ArrayList<String>(spinnerPartyLedgerCollectionPointNamesMap.values());
                    int a = indexes.indexOf(name);
                    collectionPointId = (new ArrayList<String>(spinnerPartyLedgerCollectionPointNamesMap.keySet())).get(indexes.indexOf(name));
                }
                if(!collectionPointId.equals(loanId))
                {
                    partyLoanBondNoField.setText(null);
                    //partyLoanBondNoField.getText().clear();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                //Yet to be completed//
            }
        });
        partyLedgerCollectionSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editCollectionPointChanged = true;
                }
                return false;
            }
        });

        //---End of Collection Point Spinner---//

        //---AutoComplete of Party Ledger Loan Bond code---//
        autoCompletePartyLoanBondNo = (AppCompatAutoCompleteTextView) findViewById (R.id.partyLoanBondNumber);
        partyAdapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
        autoCompletePartyLoanBondNo.setThreshold(3);
        autoCompletePartyLoanBondNo.setAdapter (partyAdapter);
        autoCompletePartyLoanBondNo.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id)
                    {
                        partyLoanBondNoField.setText(partyAdapter.getObject(position));
                        PartyLedger partyLedger = null;

                        for (int i = 0; i < partyCollectionList.size(); i++)
                        {
                            if(partyCollectionList.get(i).getLoanBondNo() == partyAdapter.getObject(position))
                            {
                                partyLedger = partyCollectionList.get(i);
                            }
                        }
                        loanId = partyLedger.getLoanId();


                    }
                });

        autoCompletePartyLoanBondNo.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
                partyHandler.removeMessages(TRIGGER_AUTO_COMPLETE);
                partyHandler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);

                //loanBondText();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        partyHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompletePartyLoanBondNo.getText()))
                    {
                        ApiRequest apiRequest = new ApiRequest("search-loanBondNo");
                        try
                        {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.accumulate("collectionPointId", collectionPointId);
                            jsonObject.accumulate("searchText", (!partyLoanBondNoField.getText().toString().isEmpty())?partyLoanBondNoField.getText().toString():null);

                            apiRequest.set_t(jsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ApiHandler.PostAsync(PartyLedgerActivity.this).execute(apiRequest);

                    }
                }
                return false;
            }
        });
        //---End of AutoComplete of Party Ledger Loan Bond code---//

        populateUserWiseBranches();
        //---Print button code---//

        //partyPrintButton.setEnabled(false);
        partyPrintButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(branchesSpinner.getSelectedItemPosition() == 0)
                {
                    alertDialogue.showAlertMessage("Please select branch");
                    return;
                }
                else if(partyLedgerCollectionSpinner.getSelectedItemPosition() == 0)
                {
                    alertDialogue.showAlertMessage("Please select collection point");
                    return;
                }
                else if(partyLoanBondNoField.getText().toString().length() == 0)
                {
                    alertDialogue.showAlertMessage("Enter a specific loan bond number");
                    return;
                }
                else if(partyLoanBondNoField.getText().toString().length() != 0 && branchesSpinner.getSelectedItemPosition() != 0
                        && partyLedgerCollectionSpinner.getSelectedItemPosition() != 0)
                {
                    if (loanId != null) {
                        populatePartyLedgerReport(loanId);
                    } else {
                        alertDialogue.showAlertMessage("Please search and select appropriate loan bond number");
                    }
                }

            }
        });
        //---End of print button code---//


        partyCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });

    }



//    //---Show alert method---//
//    private void showAlert(String error)
//    {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(PartyLedgerActivity.this);
//        builder.setTitle("ENFIN Admin");
//        builder.setMessage(error);
//        builder.setCancelable(true);
//        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i)
//            {
//                dialogInterface.cancel();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//    //---end of alert method---//

    private void saveAfterText(AlertDialogue alertDialogue)
    {

        if(partyLoanBondNoField.getText().toString().length() == 0)
        {

        }
        else if(branchesSpinner.getSelectedItemPosition() == 0)
        {
            alertDialogue.showAlertMessage("Please select branch");
            //showAlert("Please select branch");
        }
        else if(partyLedgerCollectionSpinner.getSelectedItemPosition() == 0)
        {
            alertDialogue.showAlertMessage("Please select collection point");
        }
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
            setUserWiseBranchAdapter(result);
        }
        else if(key.contains("getBranchWiseCP"))
        {
            setPartyLedgerCollectionPointAdapter(result);
        }
        else if(key.contains("search-loanBondNo"))
        {
            setPartyLedgerLoanBondNoAdapter(result);
        }
        else if(key.contains("printPartyLedger"))
        {
            setPrintPartyLedgerAdapter(result);
        }
    }

    private void populateUserWiseBranches()
    {
        new ApiHandler.GetAsync(PartyLedgerActivity.this).execute("getUserWiseBranch");
    }

    private void populatePartyLedgerCollectionPointNames(String branchId)
    {
        new ApiHandler.GetAsync(PartyLedgerActivity.this).execute("getBranchWiseCP/{"+ branchId +"}");
    }

    private void populatePartyLedgerReport(String loanId)
    {
        new ApiHandler.GetAsync(PartyLedgerActivity.this).execute("printPartyLedger/{"+ loanId +"}");
    }

    //---Branch Adapter---//
    private void setUserWiseBranchAdapter(String result)
    {
        try {
            JSONArray jsonArray = new JSONArray(result);
            Branch branch1 = new Branch(emptyGuid,"Select Branch Name");
            userWiseBranchesLists.add(branch1);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                Branch branch = new Branch(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                userWiseBranchesLists.add(branch);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerUserWiseBranchesArray = new String[userWiseBranchesLists.size()];
        for (int i = 0; i < userWiseBranchesLists.size(); i++)
        {
            spinnerUserWiseBranchesMap.put(userWiseBranchesLists.get(i).getId(),userWiseBranchesLists.get(i).getName());
            spinnerUserWiseBranchesArray[i] = userWiseBranchesLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PartyLedgerActivity.this, android.R.layout.simple_spinner_item,spinnerUserWiseBranchesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        branchesSpinner.setAdapter(adapter);
    }
    //---End of Branch Adapter---//

    //---Collection Point adapter---//
    private void setPartyLedgerCollectionPointAdapter(String result)
    {
        try {

            partyLedgerCollectionPointLists = new ArrayList<CollPoint>();
            JSONArray jsonArray = new JSONArray(result);
            CollPoint collPoint1 = new CollPoint(emptyGuid,"Select Collection Point Name");
            partyLedgerCollectionPointLists.add(collPoint1);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                CollPoint collPoint = new CollPoint(
                        jsonObject.getString("id"),
                        jsonObject.getString("name")
                );
                partyLedgerCollectionPointLists.add(collPoint);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] spinnerPartyLedgerCollectionPointNamesArray = new String[partyLedgerCollectionPointLists.size()];
        for (int i = 0; i < partyLedgerCollectionPointLists.size(); i++)
        {
            spinnerPartyLedgerCollectionPointNamesMap.put(partyLedgerCollectionPointLists.get(i).getId(),partyLedgerCollectionPointLists.get(i).getName());
            spinnerPartyLedgerCollectionPointNamesArray[i] = partyLedgerCollectionPointLists.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PartyLedgerActivity.this, android.R.layout.simple_spinner_item,spinnerPartyLedgerCollectionPointNamesArray);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        partyLedgerCollectionSpinner.setAdapter(adapter);
    }
    //---End of Collection Point adapter---//

    //---Loan bond number adapter---//
    private void setPartyLedgerLoanBondNoAdapter(String result)
    {
        try
        {
            partyCollectionList = new ArrayList<PartyLedger>();
            partySearchText = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray(result);
            for (int k = 0; k < jsonArray.length(); k++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(k);
                PartyLedger partyLedger = new PartyLedger(
                        jsonObject.getString("loanId"),
                        jsonObject.getString("loanBondNo")
                );
                partyCollectionList.add(partyLedger);
                partySearchText.add(jsonObject.getString("loanBondNo"));

            }
            partyAdapter.setData(partySearchText);
            partyAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //---End of Loan bond number adapter---//

    private void setPrintPartyLedgerAdapter(String result)
    {
        //Toast.makeText(getApplicationContext(),"Party Ledger report generated successfully", Toast.LENGTH_SHORT).show();

        String test = "http://"+result.substring(1, result.length()-1);
        test = test.replace("\\\\","/");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(test));
        request.setDescription("Party Ledger");
        request.setTitle("Party Ledger Report");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "PartyLedgerReport"+" "+".xlsx");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        final AlertDialog.Builder builder = new AlertDialog.Builder(PartyLedgerActivity.this);
        builder.setTitle("ENFINS Admin");
        builder.setMessage("Party Ledger Print Successfully");
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
