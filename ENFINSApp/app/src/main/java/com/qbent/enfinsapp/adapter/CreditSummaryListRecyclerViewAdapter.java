package com.qbent.enfinsapp.adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qbent.enfinsapp.CollectionPointListActivity;
import com.qbent.enfinsapp.CreditDataCheckActivity;
import com.qbent.enfinsapp.CreditSummaryDetailsActivity;
import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.model.CreditSummary;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import java.io.IOException;
import java.util.List;

public class CreditSummaryListRecyclerViewAdapter extends RecyclerView.Adapter<CreditSummaryListRecyclerViewAdapter.CreditSummaryListViewHolder> implements ApiCallback
{
    private final List<CreditSummary> creditSummary;

    public CreditSummaryListRecyclerViewAdapter(List<CreditSummary> creditSummary)
    {
        this.creditSummary = creditSummary;
    }

    @NonNull
    @Override
    public CreditSummaryListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_credit_summary_details, viewGroup, false);
        return new CreditSummaryListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditSummaryListRecyclerViewAdapter.CreditSummaryListViewHolder creditSummaryListViewHolder, final int i)
    {
        creditSummaryListViewHolder.creditSummaryItem = creditSummary.get(i);
        creditSummaryListViewHolder.creditSummaryCheckDate.setText(creditSummary.get(i).getCheckingDate());
        creditSummaryListViewHolder.creditOrganizationName.setText(creditSummary.get(i).getOrganizationName());

        creditSummaryListViewHolder.memberCreditSummaryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://166.62.38.28/reportView/equifaxReport/"+creditSummary.get(i).getId()));
                request.setDescription("Credit Data Info");
                request.setTitle("Credit Data Report");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "CreditReport_" + creditSummary.get(i).getId() + ".pdf");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                DownloadManager manager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);


                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("ENFIN Admin");
                builder.setMessage("Downloading Credit Data Report");
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
        });
    }

    @Override
    public int getItemCount() {
        return  creditSummary.size();
    }

    @Override
    public void onApiRequestStart() throws IOException {

    }

    @Override
    public void onApiRequestComplete(String key, String result) throws IOException {

    }

    public class CreditSummaryListViewHolder extends RecyclerView.ViewHolder
    {
        public final View creditSummaryListView;
        public final TextView creditSummaryCheckDate;
        public final TextView creditOrganizationName;
        public CreditSummary creditSummaryItem;
        public final ImageButton memberCreditSummaryButton;

        public CreditSummaryListViewHolder(@NonNull View view) {
            super(view);
            creditSummaryListView = view;
            creditSummaryCheckDate = (TextView) view.findViewById(R.id.creditCheckDate);
            creditOrganizationName = (TextView) view.findViewById(R.id.creditOrganizationName);
            memberCreditSummaryButton =(ImageButton) view.findViewById(R.id.buttonDownloadMemberCreditDetail);
        }
        @Override
        public String toString()
        {
            return super.toString();
        }
    }
}
