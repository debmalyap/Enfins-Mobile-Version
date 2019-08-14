package com.qbent.enfinsapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.TrialBalanceReport;
import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.model.Demand;
import com.qbent.enfinsapp.model.DemandCollection;

import java.util.ArrayList;
import java.util.List;

public class DemandCollectionChildAdapter extends RecyclerView.Adapter<DemandCollectionChildAdapter.MyViewHolder>
{

    private DemandCollection demandCollection;
    private ArrayList<Demand> demandArrayList;
    private List<Demand> _retData;
    private AlertDialogue alertDialogue;

    //private Activity activity;

    public DemandCollectionChildAdapter(ArrayList<Demand> eventsArrayList)
    {
        this.demandArrayList = null;
        this.demandArrayList = eventsArrayList;
        this._retData = eventsArrayList;


        //this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_collection_posting_entry, viewGroup, false);
        alertDialogue = new AlertDialogue(viewGroup.getContext());
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i)
    {

        final Demand demand = demandArrayList.get(i);

        myViewHolder.mItem = demandArrayList.get(i);
        myViewHolder.loan_bond_number.setText(demand.getLoanBondNo());
        myViewHolder.borrower_name.setText(demand.getBorrowerName());
        myViewHolder.loan_product.setText(demand.getLoanProductName());
        myViewHolder.loan_amount.setText(String.valueOf(demand.getLoanAmount()));
        myViewHolder.outstanding_amount.setText(String.valueOf(demand.getOutstandingAmount()));
        myViewHolder.instalment_number.setText(String.valueOf(demand.getInstallmentNo()));
        myViewHolder.instalment_amount.setText(String.valueOf(demand.getTotalInstallmentAmount()));
        myViewHolder.overdue_pricipal_amount.setText(String.valueOf(demand.getOverDuePrincipal()));
        myViewHolder.overdue_interest_amount.setText(String.valueOf(demand.getOverDueInterest()));
        myViewHolder.prinicipal_amount.setText(String.valueOf(demand.getInstallmentPrincipal()));
        myViewHolder.interest_amount.setText(String.valueOf(demand.getInstallmentInterest()));
        myViewHolder.paid_amount.setText(String.valueOf(demand.getTotalPaidAmount()));
        myViewHolder.remarks_section.setText(demand.getRemarks());
        myViewHolder.isMemberPresent.setChecked(demand.getMemberPresent());

        myViewHolder.loan_bond_number.setEnabled(false);
        myViewHolder.borrower_name.setEnabled(false);
        myViewHolder.loan_product.setEnabled(false);
        myViewHolder.loan_amount.setEnabled(false);
        myViewHolder.loan_bond_number.setEnabled(false);
        myViewHolder.outstanding_amount.setEnabled(false);
        myViewHolder.instalment_number.setEnabled(false);
        myViewHolder.instalment_amount.setEnabled(false);
        myViewHolder.overdue_pricipal_amount.setEnabled(false);
        myViewHolder.overdue_interest_amount.setEnabled(false);
        myViewHolder.prinicipal_amount.setEnabled(false);
        myViewHolder.interest_amount.setEnabled(false);
        myViewHolder.status_section.setEnabled(false);

        if(demand.getCollected() || demand.getPreClose() || demand.getAdvendeCollection())
        {
            myViewHolder.paid_amount.setEnabled(false);
            myViewHolder.remarks_section.setEnabled(false);
            myViewHolder.isMemberPresent.setEnabled(false);
        }


//        _retData = new ArrayList<Demand>(demandArrayList.size());



        myViewHolder.paid_amount.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String test = (s.toString().isEmpty())?"0":s.toString();
                if(Integer.parseInt(test)>demand.getOutstandingAmount())
                {
                    alertDialogue.showAlertMessage("Total paid amount should be less or equal than outstanding amount");
                    myViewHolder.paid_amount.setText("");
                    _retData.get(i).setTotalPaidAmount(0);
                }
                else
                {

                    _retData.get(i).setTotalPaidAmount(Integer.parseInt(test));
                }

            }
        });
        myViewHolder.remarks_section.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _retData.get(i).setRemarks(s.toString());
            }
        });
        myViewHolder.isMemberPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _retData.get(i).setMemberPresent(myViewHolder.isMemberPresent.isChecked());
            }
        });

        //myViewHolder.status_section.setText(demand.getDemandIsCollected());

    }

    public List<Demand> retrieveData()
    {

        return _retData;
    }

    @Override
    public int getItemCount()
    {
        return demandArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView loan_bond_number;
        public EditText borrower_name;
        public EditText co_borrower_name;
        public EditText loan_product;
        public EditText loan_amount;
        public EditText outstanding_amount;
        public EditText instalment_number;
        public EditText instalment_amount;
        public EditText overdue_pricipal_amount;
        public EditText overdue_interest_amount;
        public EditText prinicipal_amount;
        public EditText interest_amount;
        public EditText paid_amount;
        public EditText remarks_section;
        public EditText status_section;
        public CheckBox isMemberPresent;
        public Demand mItem;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            loan_bond_number = (TextView) itemView.findViewById(R.id.loanBondNumberId);
            borrower_name = (EditText) itemView.findViewById(R.id.demand_Borrower_Name);
            loan_product = (EditText) itemView.findViewById(R.id.demand_Loan_Product);
            loan_amount = (EditText) itemView.findViewById(R.id.demand_Loan_Amt);
            outstanding_amount = (EditText) itemView.findViewById(R.id.demand_Outstanding_Amt);
            instalment_number = (EditText) itemView.findViewById(R.id.demand_Installment_No);
            instalment_amount = (EditText) itemView.findViewById(R.id.demand_Installment_Amt);
            overdue_pricipal_amount = (EditText) itemView.findViewById(R.id.demand_ODPriAmt);
            overdue_interest_amount = (EditText) itemView.findViewById(R.id.demand_ODIntAmt);
            prinicipal_amount = (EditText) itemView.findViewById(R.id.demand_PrinAmt);
            interest_amount = (EditText) itemView.findViewById(R.id.demand_IntAmt);
            paid_amount = (EditText) itemView.findViewById(R.id.demand_PaidAmt);
            remarks_section = (EditText) itemView.findViewById(R.id.demand_Remarks);
            status_section = (EditText) itemView.findViewById(R.id.demand_Status);
            isMemberPresent = (CheckBox) itemView.findViewById(R.id.isMemberPresent);
        }
    }
}
