package com.qbent.enfinsapp.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.qbent.enfinsapp.CreditDataCheckActivity;
import com.qbent.enfinsapp.MemberEligibityDetailsActivity;
import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.model.Member;
import com.qbent.enfinsapp.model.MemberEligibility;
import com.qbent.enfinsapp.model.MemberEligibility;

import java.util.List;

//---Developed by Debmalya---//
public class MemberEligibityRecyclerViewAdapterMobileVersion extends RecyclerView.Adapter<MemberEligibityRecyclerViewAdapterMobileVersion.MemberEligibityViewHolder>
{
    private final List<MemberEligibility> memberEligibity;

    public MemberEligibityRecyclerViewAdapterMobileVersion(List<MemberEligibility> memberEligibity)
    {
        this.memberEligibity = memberEligibity;
    }

    @NonNull
    @Override
    public MemberEligibityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_member_eligibity_normal, viewGroup, false);
        return new MemberEligibityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberEligibityRecyclerViewAdapterMobileVersion.MemberEligibityViewHolder memberEligibityViewHolder,final int i)
    {
        memberEligibityViewHolder.mItem = memberEligibity.get(i);
        memberEligibityViewHolder.memberEligibityCodeView.setText(memberEligibity.get(i).getMemberCode());
        memberEligibityViewHolder.memberEligibityNameView.setText(memberEligibity.get(i).getMemberName());
        memberEligibityViewHolder.memberEligibityAadharView.setText(String.valueOf(memberEligibity.get(i).getAadharNo()));
        memberEligibityViewHolder.memberEligibityCollectionPointView.setText(memberEligibity.get(i).getCollectionPoint());
        memberEligibityViewHolder.memberEligibityOrganizationView.setText(memberEligibity.get(i).getOrganizationName());
        memberEligibityViewHolder.memberEligibityLoanProductNameView.setText(memberEligibity.get(i).getLoanProductName());
        memberEligibityViewHolder.memberEligibityCheckedByView.setText(memberEligibity.get(i).getLoanOfficerName());
        memberEligibityViewHolder.memberEligibityCheckedOnView.setText(memberEligibity.get(i).getCheckedOn());
        memberEligibityViewHolder.memberEligibityStatusView.setText(memberEligibity.get(i).getStatus());


        memberEligibityViewHolder.gridLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Toast.makeText(view.getContext(),"You Clicked "+memberEligibity.get(i).getMemberName(), Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(view.getContext(), MemberEligibityDetailsActivity.class);
                intent3.putExtra("member_eligibity_id",memberEligibity.get(i).getMemberId());
                intent3.putExtra("member_eligibity_loanProduct_id",memberEligibity.get(i).getLoanProductId());
                view.getContext().startActivity(intent3);

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return memberEligibity.size();
    }

    public class MemberEligibityViewHolder extends RecyclerView.ViewHolder
    {
        public final View memberEligibityView;
        public final TextView memberEligibityCodeView;
        public final TextView memberEligibityNameView;
        public final TextView memberEligibityAadharView;
        public final TextView memberEligibityCollectionPointView;
        public final TextView memberEligibityOrganizationView;
        public final TextView memberEligibityLoanProductNameView;
        public final TextView memberEligibityCheckedByView;
        public final TextView memberEligibityCheckedOnView;
        public final TextView memberEligibityStatusView;
        public MemberEligibility mItem;
        public GridLayout gridLayout;




        public MemberEligibityViewHolder(View view)
        {
            super(view);
            memberEligibityView = view;
            memberEligibityCodeView = (TextView) view.findViewById(R.id.textMemberEligibityCode);
            memberEligibityNameView = (TextView) view.findViewById(R.id.textMemberEligibityName);
            memberEligibityAadharView = (TextView) view.findViewById(R.id.textMemberEligibityAadhar);
            memberEligibityCollectionPointView = (TextView) view.findViewById(R.id.textMemberEligibityCollectionPoint);
            memberEligibityOrganizationView = (TextView) view.findViewById(R.id.textMemberEligibityOrganizationName);
            memberEligibityLoanProductNameView = (TextView) view.findViewById(R.id.textMemberEligibityLoanProductName);
            memberEligibityCheckedByView = (TextView) view.findViewById(R.id.textMemberEligibityCheckedBy);
            memberEligibityCheckedOnView = (TextView) view.findViewById(R.id.textMemberEligibityCheckedOn);
            memberEligibityStatusView = (TextView) view.findViewById(R.id.textMemberEligibityStatus);
            gridLayout = (GridLayout) view.findViewById(R.id.memberEligibilityCheckGridLayout);
            gridLayout.setClickable(true);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
//---Ended by Debmalya---//
