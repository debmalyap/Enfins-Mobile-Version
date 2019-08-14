package com.qbent.enfinsapp.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.qbent.enfinsapp.LoanApplicationDetailsAddActivity;
import com.qbent.enfinsapp.LoanHouseVisitActivity;
import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.model.LoanApplication;

import java.util.List;

public class LoanApplicationListRecyclerViewAdapterMobile extends RecyclerView.Adapter<LoanApplicationListRecyclerViewAdapterMobile.LoanApplicationListViewHolderMobile>
{
    private final List<LoanApplication> loanApplications;

    public LoanApplicationListRecyclerViewAdapterMobile(List<LoanApplication> loanApplications)
    {
        this.loanApplications = loanApplications;
    }



    @NonNull
    @Override
    public LoanApplicationListViewHolderMobile onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_loan_application_normal, viewGroup, false);
        return new LoanApplicationListRecyclerViewAdapterMobile.LoanApplicationListViewHolderMobile(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanApplicationListViewHolderMobile loanApplicationListViewHolderMobile, final int i)
    {

        loanApplicationListViewHolderMobile.mItem = loanApplications.get(i);

        loanApplicationListViewHolderMobile.applicationNoView.setText(loanApplications.get(i).getApplicationNo());
        loanApplicationListViewHolderMobile.applicationDateView.setText(loanApplications.get(i).getApplicationDate());
        loanApplicationListViewHolderMobile.branchView.setText(loanApplications.get(i).getBranchName());
        loanApplicationListViewHolderMobile.collectionPointView.setText(loanApplications.get(i).getCollectionPoint());
        loanApplicationListViewHolderMobile.loanOfficerView.setText(loanApplications.get(i).getLoanOfficer());
        loanApplicationListViewHolderMobile.borrowerNameView.setText(loanApplications.get(i).getBorrowerName());
        loanApplicationListViewHolderMobile.coBorrowerNameView.setText(loanApplications.get(i).getCoBorrowerName());
        loanApplicationListViewHolderMobile.loanProductView.setText(loanApplications.get(i).getLoanProductName());
        loanApplicationListViewHolderMobile.loanPurposeView.setText(loanApplications.get(i).getLoanPurposeDesc());
        loanApplicationListViewHolderMobile.statusView.setText(loanApplications.get(i).getStatus());

        if(loanApplications.get(i).getNextAction().equals("Visit House"))
        {
            loanApplicationListViewHolderMobile.loanEditButton.setVisibility(View.GONE);
            loanApplicationListViewHolderMobile.pendingHouseVisit.setVisibility(View.VISIBLE);
            loanApplicationListViewHolderMobile.pendingHouseVisit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(view.getContext(), LoanHouseVisitActivity.class);
                    intent.putExtra("loan_application_id",loanApplications.get(i).getId());
                    view.getContext().startActivity(intent);

                }
            });
        }
        else
        {
            loanApplicationListViewHolderMobile.loanEditButton.setVisibility(View.VISIBLE);
            loanApplicationListViewHolderMobile.pendingHouseVisit.setVisibility(View.GONE);
            loanApplicationListViewHolderMobile.loanEditButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //Toast.makeText(view.getContext(),"You Clicked "+members.get(i).getFullName(), Toast.LENGTH_LONG).show();

                    LoanApplicationDetailsAddActivity loanApplicationDetailsAddActivity = new LoanApplicationDetailsAddActivity();
                    Intent intent3 = new Intent(view.getContext(), LoanApplicationDetailsAddActivity.class);
                    intent3.putExtra("loan_edit_id",loanApplications.get(i).getId());
                    //intent3.putExtra("emp_mobile",listItem.getMobileNo());
                    //intent3.putExtra("emp_company",listItem.getCompany());
                    view.getContext().startActivity(intent3);

                }
            });
        }



//        loanApplicationListViewHolder.tableLayout.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                //Toast.makeText(view.getContext(),"You Clicked "+members.get(i).getFullName(), Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(view.getContext(), LoanHouseVisitActivity.class);
//                intent.putExtra("loan_application_id",loanApplications.get(i).getId());
////                intent.putExtra("loan_officer_id",loanApplications.get(i).getLoanOfficer());
//                view.getContext().startActivity(intent);
//
//            }
//        });

    }

    @Override
    public int getItemCount()
    {
        return loanApplications.size();
    }

    public class LoanApplicationListViewHolderMobile extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final View mView;
        public final TextView applicationNoView;
        public final TextView applicationDateView;
        public final TextView branchView;
        public final TextView collectionPointView;
        public final TextView loanOfficerView;
        public final TextView borrowerNameView;
        public final TextView coBorrowerNameView;
        public final TextView loanProductView;
        public final TextView loanPurposeView;
        public final TextView statusView;
        public LoanApplication mItem;
        public final ImageButton loanEditButton;
        public final ImageButton pendingHouseVisit;

        //public ConstraintLayout constraintLayout;

        public TableLayout tableLayout;

        public LoanApplicationListViewHolderMobile(View view) {
            super(view);
            mView = view;
            applicationNoView = (TextView) view.findViewById(R.id.textApplicationNo);
            applicationDateView = (TextView) view.findViewById(R.id.textApplicationDate);
            branchView = (TextView) view.findViewById(R.id.textBranch);
            collectionPointView = (TextView) view.findViewById(R.id.textCollectionPoint);
            loanOfficerView = (TextView) view.findViewById(R.id.textLoanOfficer);
            borrowerNameView = (TextView) view.findViewById(R.id.textBorrowerName);
            coBorrowerNameView = (TextView) view.findViewById(R.id.textCoBorrowerName);
            loanProductView = (TextView) view.findViewById(R.id.textLoanProduct);
            loanPurposeView = (TextView) view.findViewById(R.id.textLoanPurpose);
            statusView = (TextView) view.findViewById(R.id.textStatus);
            loanEditButton =(ImageButton) view.findViewById(R.id.buttonEditLoan);
            pendingHouseVisit = (ImageButton) view.findViewById(R.id.buttonPendingHouseVisit);

            //constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.loanConstraint);
            //tableLayout = (TableLayout) view.findViewById(R.id.tabLoanApplicationLayout);

        }

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        public void onClick(View view) {

        }
    }
}
