package com.qbent.enfinsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qbent.enfinsapp.CreditDataCheckActivity;
import com.qbent.enfinsapp.CreditSummaryDetailsActivity;
import com.qbent.enfinsapp.MemberDeatilsActivity;
import com.qbent.enfinsapp.MemberEditActivity;
import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.model.CollectionPoint;
import com.qbent.enfinsapp.model.Member;

import java.util.List;

public class MemberRecyclerViewAdapter extends
        RecyclerView.Adapter<MemberRecyclerViewAdapter.MemberViewHolder> {
    private final List<Member> members;


    public MemberRecyclerViewAdapter(List<Member> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_member, viewGroup, false);


        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MemberRecyclerViewAdapter.MemberViewHolder memberViewHolder, final int i) {
        memberViewHolder.mItem = members.get(i);
        memberViewHolder.memberCodeView.setText(members.get(i).getCode());
        memberViewHolder.memberNameView.setText(members.get(i).getFullName());
        memberViewHolder.memberAadharView.setText(String.valueOf(members.get(i).getAadharNo()));
        memberViewHolder.memberGuardianView.setText(members.get(i).getGuardianName());
        memberViewHolder.memberCollectionPointView.setText(members.get(i).getCollectionPointName());
        memberViewHolder.memberStatusView.setText(members.get(i).getVisitStatus());

        Drawable image = memberViewHolder.itemView.getContext().getDrawable(R.drawable.edit_btn_bg);
        RippleDrawable rippledBg = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(memberViewHolder.itemView.getContext().getApplicationContext(),R.color.design_default_color_primary)), image, null);
        memberViewHolder.memberEditButton.setBackground(rippledBg);

        Drawable image1 = memberViewHolder.itemView.getContext().getDrawable(R.drawable.download_btn_bg);
        RippleDrawable rippledBg1 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(memberViewHolder.itemView.getContext().getApplicationContext(),R.color.design_default_color_primary)), image1, null);
        memberViewHolder.memberCreditSummaryButton.setBackground(rippledBg1);

        Drawable image2 = memberViewHolder.itemView.getContext().getDrawable(R.drawable.info_btn_bg);
        RippleDrawable rippledBg2 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(memberViewHolder.itemView.getContext().getApplicationContext(),R.color.design_default_color_primary)), image2, null);
        memberViewHolder.memberCreditCheckButton.setBackground(rippledBg2);

        Drawable image3 = memberViewHolder.itemView.getContext().getDrawable(R.drawable.edit_btn_bg);
        RippleDrawable rippledBg3 = new RippleDrawable(ColorStateList.valueOf(ContextCompat.getColor(memberViewHolder.itemView.getContext().getApplicationContext(),R.color.design_default_color_primary)), image3, null);
        memberViewHolder.memberLocationButton.setBackground(rippledBg3);

        if(members.get(i).getLatByLo().equals("null"))
        {
            memberViewHolder.memberLocationButton.setVisibility(View.GONE);
        }
        else
        {
            memberViewHolder.memberLocationButton.setVisibility(View.VISIBLE);
        }

        memberViewHolder.memberLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String geoUri = null;
                if(!members.get(i).getLatByBm().equals("null"))
                {
                    geoUri = "http://maps.google.com/maps?q=loc:" + members.get(i).getLatByBm() + "," + members.get(i).getLonByBm();
                }
                else
                {
                    geoUri = "http://maps.google.com/maps?q=loc:" + members.get(i).getLatByLo() + "," + members.get(i).getLonByLo();
                }
                
//                String uri = "http://maps.google.com/maps?saddr=" + collectionPoints.get(i).getLat() + "," + collectionPoints.get(i).getLon() + "&daddr=" + collectionPoints.get(i).getLat() + "," + collectionPoints.get(i).getLon();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                view.getContext().startActivity(intent);

//                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", collectionPoints.get(i).getLat(), collectionPoints.get(i).getLon());
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                view.getContext().startActivity(intent);

            }
        });

        memberViewHolder.memberEditButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //Toast.makeText(view.getContext(),"You Clicked "+members.get(i).getFullName(), Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(view.getContext(), MemberEditActivity.class);
                intent3.putExtra("emp_id",members.get(i).getId());
                //intent3.putExtra("emp_mobile",listItem.getMobileNo());
                //intent3.putExtra("emp_company",listItem.getCompany());
                view.getContext().startActivity(intent3);

            }
        });


        memberViewHolder.memberCreditCheckButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Toast.makeText(view.getContext(),"You Clicked "+members.get(i).getFullName(), Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(view.getContext(), CreditDataCheckActivity.class);
                intent3.putExtra("emp_id",members.get(i).getId());
                //intent3.putExtra("emp_mobile",listItem.getMobileNo());
                //intent3.putExtra("emp_company",listItem.getCompany());
                view.getContext().startActivity(intent3);

            }
        });


        memberViewHolder.memberCreditSummaryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Toast.makeText(view.getContext(),"You Clicked "+members.get(i).getFullName(), Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(view.getContext(), CreditSummaryDetailsActivity.class);
                intent3.putExtra("member_id",members.get(i).getId());
                //intent3.putExtra("emp_mobile",listItem.getMobileNo());
                //intent3.putExtra("emp_company",listItem.getCompany());
                view.getContext().startActivity(intent3);

            }
        });

//---Add this inside onBindViewHolder---//
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        public final View memberView;
        public final TextView memberCodeView;
        public final TextView memberNameView;
        public final TextView memberAadharView;
        public final TextView memberGuardianView;
        public final TextView memberCollectionPointView;
        public final TextView memberStatusView;
        public Member mItem;
        public final Button memberCreditCheckButton;
        public final Button memberCreditSummaryButton;
        public final Button memberEditButton;
        public final Button memberLocationButton;


        public GridLayout gridLayout;
//---Add this inside MemberViewHolder class---//

        public MemberViewHolder(View view) {
            super(view);
            memberView = view;
            memberCodeView = (TextView) view.findViewById(R.id.textMemberCode);
            memberNameView = (TextView) view.findViewById(R.id.textMemberName);
            memberAadharView = (TextView) view.findViewById(R.id.textMemberAadhar);
            memberGuardianView = (TextView) view.findViewById(R.id.textMemberGuardianName);
            memberCollectionPointView = (TextView) view.findViewById(R.id.textMemberCollectionPoint);
            memberStatusView = (TextView) view.findViewById(R.id.textMemberVisitStatus);
            memberCreditCheckButton = (Button) view.findViewById(R.id.memberCreditCheck);
            gridLayout =(GridLayout)view.findViewById(R.id.memberGridLayout);
            memberEditButton =(Button) view.findViewById(R.id.buttonEditMember);
            memberCreditSummaryButton =(Button) view.findViewById(R.id.buttonDownloadMemberCredit);
            memberLocationButton = (Button) view.findViewById(R.id.buttonLocationMember);

//            memberCreditCheckButton.setBackground(rippledBg);
//
//            memberCreditSummaryButton.setBackground(rippledBg);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mAddressView.getText() + "'";
//        }
    }
}
