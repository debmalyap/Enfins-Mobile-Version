package com.qbent.enfinsapp.adapter;

//---Developed by Debmalya---//

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.qbent.enfinsapp.CollectionPointDetailActivity;
import com.qbent.enfinsapp.LoanApplicationDetailsAddActivity;
import com.qbent.enfinsapp.LoanHouseVisitActivity;
import com.qbent.enfinsapp.MemberEditActivity;
import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.model.LoanApplication;

import java.util.List;

public class LoanApplicationListRecyclerViewAdapter extends RecyclerView.Adapter<LoanApplicationListRecyclerViewAdapter.LoanApplicationListViewHolder>
{
    private List<LoanApplication> listItems;
    private final List<LoanApplication> loanApplications;

    public LoanApplicationListRecyclerViewAdapter(List<LoanApplication> loanApplications)
    {
        this.loanApplications = loanApplications;
    }

    @NonNull
    @Override
    public LoanApplicationListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_loan_application, viewGroup, false);
        return new LoanApplicationListRecyclerViewAdapter.LoanApplicationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanApplicationListViewHolder loanApplicationListViewHolder, final int i)
    {

        loanApplicationListViewHolder.mItem = loanApplications.get(i);

        loanApplicationListViewHolder.applicationNoView.setText(loanApplications.get(i).getApplicationNo());
        loanApplicationListViewHolder.applicationDateView.setText(loanApplications.get(i).getApplicationDate());
        loanApplicationListViewHolder.branchView.setText(loanApplications.get(i).getBranchName());
        loanApplicationListViewHolder.collectionPointView.setText(loanApplications.get(i).getCollectionPoint());
        loanApplicationListViewHolder.loanOfficerView.setText(loanApplications.get(i).getLoanOfficer());
        loanApplicationListViewHolder.borrowerNameView.setText(loanApplications.get(i).getBorrowerName());
        loanApplicationListViewHolder.coBorrowerNameView.setText(loanApplications.get(i).getCoBorrowerName());
        loanApplicationListViewHolder.loanProductView.setText(loanApplications.get(i).getLoanProductName());
        loanApplicationListViewHolder.loanPurposeView.setText(loanApplications.get(i).getLoanPurposeDesc());
        loanApplicationListViewHolder.statusView.setText(loanApplications.get(i).getStatus());

        if(loanApplications.get(i).getNextAction().equals("Visit House"))
        {
            loanApplicationListViewHolder.loanEditButton.setVisibility(View.GONE);
            loanApplicationListViewHolder.pendingHouseVisit.setVisibility(View.VISIBLE);
            loanApplicationListViewHolder.pendingHouseVisit.setOnClickListener(new View.OnClickListener()
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
            loanApplicationListViewHolder.loanEditButton.setVisibility(View.VISIBLE);
            loanApplicationListViewHolder.pendingHouseVisit.setVisibility(View.GONE);
            loanApplicationListViewHolder.loanEditButton.setOnClickListener(new View.OnClickListener()
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

    public class LoanApplicationListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
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

        public LoanApplicationListViewHolder(View view) {
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
//---Ended by Debmalya---//
