package com.qbent.enfinsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qbent.enfinsapp.global.AuthHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AuthHelper _authHelper;
    DrawerLayout drawer;
    FloatingActionButton fab;
    NavigationView navigationView;
    private String selectDate, workingDate,date1,date2;
    private Date user_date,work_date;
    String user_name,header_work_date;
    TextView userName,workingDateField,userNameBar,workingDateFieldBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        _authHelper = AuthHelper.getInstance(this);

        selectDate = _authHelper.getIdSlectionDate();
        workingDate = _authHelper.getIdDate().substring(0,10);
        user_name = _authHelper.getIdName();




        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd-yyyy");
        try {
            user_date = simpleDateFormat1.parse(selectDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date1 = simpleDateFormat1.format(user_date);


        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            work_date = simpleDateFormat2.parse(workingDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        date2 = simpleDateFormat3.format(work_date);
        header_work_date = simpleDateFormat4.format(user_date);

        if(android.os.Build.VERSION.SDK_INT>=21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.blue));
        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);

        userName = (TextView) header.findViewById(R.id.user_name);
        workingDateField = (TextView) header.findViewById(R.id.working_date);

        userNameBar = (TextView) findViewById(R.id.headerUserName);
        workingDateFieldBar = (TextView) findViewById(R.id.headerUserWorkingDate);

        userName.setText(user_name);
        workingDateField.setText(header_work_date);
        userNameBar.setText(user_name);
        workingDateFieldBar.setText(header_work_date);
    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.activity_main);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds itgetActionBarems to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), WorkingDateActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startAnimatedActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_collection_point) {
            startAnimatedActivity(new Intent(getApplicationContext(), CollectionPointListActivity.class));
            finish();
        }
        else if (id == R.id.nav_partyledger) {
            startAnimatedActivity(new Intent(getApplicationContext(), PartyLedgerActivity.class));
            finish();
        }
        else if (id == R.id.nav_member) {
            startAnimatedActivity(new Intent(getApplicationContext(), MemberActivity.class));
            finish();
        }
        else if (id == R.id.nav_loanapplication)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), LoanApplicationActivity.class));
            finish();
        }
        else if (id == R.id.nav_demandcollectionposting)
        {

            startAnimatedActivity(new Intent(getApplicationContext(), DemandCollectionPosting.class));
        }
        else if (id == R.id.nav_membereligibitycheck)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), MemberEligibityCheckActivity.class));
            finish();
        }
        else if (id == R.id.nav_overduecollection)
        {
            //---Edited by Debmalya---//
            if(date1.equals(date2))
            {
                startAnimatedActivity(new Intent(getApplicationContext(), OverdueCollectionActivity.class));
            }
            else
            {
                startAnimatedActivity(new Intent(getApplicationContext(), UnauthorisedActivity.class));
            }
            //---Ended by Debmalya---//

        }
        else if (id == R.id.nav_precloseloan)
        {
            //---Edited by Debmalya---//
            if(date1.equals(date2))
            {
                startAnimatedActivity(new Intent(getApplicationContext(), PrecloseLoanActivity.class));
            }
            else
            {
                startAnimatedActivity(new Intent(getApplicationContext(), UnauthorisedActivity.class));
            }
            //---Ended by Debmalya---//
        }
        else if (id == R.id.nav_advancecollection)
        {
            //---Edited by Debmalya---//
            if(date1.equals(date2))
            {
                startAnimatedActivity(new Intent(getApplicationContext(), AdvancedCollectionActivity.class));
            }
            else
            {
                startAnimatedActivity(new Intent(getApplicationContext(), UnauthorisedActivity.class));
            }
            //---Ended by Debmalya---//
        }
        else if (id == R.id.nav_partpaymentcollection)
        {
            //---Edited by Debmalya---//
            if(date1.equals(date2))
            {
                startAnimatedActivity(new Intent(getApplicationContext(), PartPaymentCollectionActivity.class));
            }
            else
            {
                startAnimatedActivity(new Intent(getApplicationContext(), UnauthorisedActivity.class));
            }
            //---Ended by Debmalya---//
        }
        else if (id == R.id.nav_schedulecalculator)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), ScheduleCalculatorActivity.class));
        }
//        else if (id == R.id.nav_collectionsheet)
//        {
//            startAnimatedActivity(new Intent(getApplicationContext(), CollectionSheetActivity.class));
//        }
        else if (id == R.id.nav_changepassword)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), ChangePassword.class));
        }
        else if (id == R.id.nav_loandisbusedreport)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), LoanDisbusedReportActivity.class));
        }
        else if (id == R.id.nav_demandvscollection)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), DemandVsCollectionReport.class));
        }
        else if (id == R.id.nav_loanpredisbusedreport)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), LoanPredisbursedReport.class));
        }
        else if (id == R.id.nav_trialbalance)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), TrialBalanceReport.class));
        }
        else if (id == R.id.nav_overduereport)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), OverdueReportActvity.class));
        }

        else if (id == R.id.nav_adjusment)
        {
            startAnimatedActivity(new Intent(getApplicationContext(), CollectionAdjustmentAndCloseLoan.class));
        }
        else if (id == R.id.nav_logout) {
            _authHelper.clearIdToken();
            finishAffinity();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void startAnimatedActivity(Intent intent) {
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
