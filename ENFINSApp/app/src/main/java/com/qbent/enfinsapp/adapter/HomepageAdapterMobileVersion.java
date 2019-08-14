package com.qbent.enfinsapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.model.Dashboard;

import java.util.List;

public class HomepageAdapterMobileVersion extends RecyclerView.Adapter<HomepageAdapterMobileVersion.HomePageViewHolder>
{
    private final List<Dashboard> dashboards;

    private Double totalDemandAmount,totalCollectionAmount;

    public HomepageAdapterMobileVersion(List<Dashboard> dashboards)
    {
        this.dashboards = dashboards;
    }




    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_table_data_normal, viewGroup, false);
        return new HomepageAdapterMobileVersion.HomePageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder homePageViewHolder,final int i)
    {
        totalDemandAmount = dashboards.get(i).getPrincipalDemand() + dashboards.get(i).getInterestDemand();
        totalCollectionAmount = dashboards.get(i).getPrincipalCollection() + dashboards.get(i).getInterestCollection();

        homePageViewHolder.mItem = dashboards.get(i);
        homePageViewHolder.mcpName.setText(dashboards.get(i).getCpName());
        homePageViewHolder.mprincipalDemand.setText(String.valueOf(dashboards.get(i).getPrincipalDemand()));
        homePageViewHolder.minterestDemand.setText(String.valueOf(dashboards.get(i).getInterestDemand()));
        homePageViewHolder.mprincipalCollection.setText(String.valueOf(dashboards.get(i).getPrincipalCollection()));
        homePageViewHolder.minterestCollection.setText(String.valueOf(dashboards.get(i).getInterestCollection()));
        homePageViewHolder.mtotalDemand.setText(String.valueOf(totalDemandAmount));
        homePageViewHolder.mtotalCollection.setText(String.valueOf(totalCollectionAmount));
    }

    @Override
    public int getItemCount()
    {
        return dashboards.size();
    }

    public class HomePageViewHolder extends RecyclerView.ViewHolder
    {

        public final View mView;
        public final TextView mcpName;
        public final TextView mprincipalDemand;
        public final TextView minterestDemand;
        public final TextView mprincipalCollection;
        public final TextView minterestCollection;
        public final TextView mtotalDemand;
        public final TextView mtotalCollection;


        public Dashboard mItem;

        public HomePageViewHolder(@NonNull View view)
        {
            super(view);
            mView = view;
            mcpName = (TextView) view.findViewById(R.id.cpNameId);
            mprincipalDemand = (TextView) view.findViewById(R.id.principalDemandId);
            minterestDemand = (TextView) view.findViewById(R.id.interestDemandId);
            mprincipalCollection = (TextView) view.findViewById(R.id.principalCollectionId);
            minterestCollection = (TextView) view.findViewById(R.id.interestCollectionId);
            mtotalDemand = (TextView) view.findViewById(R.id.totalDemandId2);
            mtotalCollection = (TextView) view.findViewById(R.id.totalCollectionId2);
        }
    }
}