package com.qbent.enfinsapp.adapter;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qbent.enfinsapp.DemandCollectionPosting;
import com.qbent.enfinsapp.HomeActivity;
import com.qbent.enfinsapp.LoanApplicationActivity;
import com.qbent.enfinsapp.LoanHouseVisitActivity;
import com.qbent.enfinsapp.MemberEditActivity;
import com.qbent.enfinsapp.PartyLedgerActivity;
import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.global.AlertDialogue;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Demand;
import com.qbent.enfinsapp.model.DemandCollection;
import com.qbent.enfinsapp.model.DemandDate;
import com.qbent.enfinsapp.restapi.ApiCallback;
import com.qbent.enfinsapp.restapi.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class DemandCollectionParentAdapter extends RecyclerView.Adapter<DemandCollectionParentAdapter.MyViewHolder>
{
    private DemandCollection demandCollection;
    private Context context;
    RecyclerView.RecycledViewPool viewPool;
    public LinearLayout linearLayout;
    public Integer sum = 0;
    public Integer amount;
    private AlertDialogue alertDialogue;
    private ProgressDialog dialog;


    public DemandCollectionParentAdapter(DemandCollection demandCollection, Context context)
    {
        this.demandCollection = demandCollection;
        this.context = context;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_demand_collection_posting, viewGroup, false);
        alertDialogue = new AlertDialogue(context);

        return new MyViewHolder(itemView);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i)
    {




        DemandDate demandDate = demandCollection.getDemandDatesList().get(i);
        myViewHolder.mItem = demandCollection.getDemandDatesList().get(i);
        myViewHolder.demand_collection_point_names.setText(demandDate.getCollectionPointName());

        myViewHolder.expandableDemandLayout.setVisibility(View.GONE);


        myViewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DemandCollectionChildAdapter demandCollectionChildAdapter = new DemandCollectionChildAdapter(demandCollection.getDemandDatesList().get(i).getDemandsArrayList());

                final List<Demand> demands = demandCollectionChildAdapter.retrieveData();

                amount = myViewHolder.totalAmount.getText().toString().isEmpty()?0:Integer.parseInt(myViewHolder.totalAmount.getText().toString());

                for(int k=0; k<demands.size();k++)
                {
                    if(demands.get(k).getCollected() || demands.get(k).getPreClose() || demands.get(k).getAdvendeCollection())
                    {
                        demands.remove(k);
                    }
                }
                sum = 0;
                if(demands!=null)
                {
                    for(int j=0;j<demands.size();j++)
                    {
                        sum = sum + demands.get(j).getTotalPaidAmount();
                    }
                }

                if(!amount.equals(sum))
                {
                    alertDialogue.showAlertMessage("Collected Amount does not match with demands");
                }
                else if(!sum.equals(0) && !amount.equals(0))
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("ENFIN Admin");
                    builder.setMessage("Are you sure to collect demand?");
                    builder.setCancelable(true);
                    builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int k)
                        {
                            ApiRequest apiRequest = new ApiRequest("collectCashAndUpdate");
                            JSONObject obj = null;
                            JSONArray collections = new JSONArray();


                            for (int i = 0; i < demands.size(); i++) {
                                obj = new JSONObject();
                                try {
                                    obj.put("loanId", demands.get(i).getLoanId());
                                    obj.put("totalPaidAmount", demands.get(i).getTotalPaidAmount());
                                    obj.put("remarks", demands.get(i).getRemarks());
                                    obj.put("isMemberPresent", demands.get(i).getMemberPresent());
                                    obj.put("lat", demands.get(i).getLat());
                                    obj.put("lon", demands.get(i).getLon());

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                collections.put(obj);
                            }
                            JSONObject finalObject = new JSONObject();
                            try {
                                finalObject.put("collections", collections);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            apiRequest.set_t(finalObject);
                            new ApiHandler.PostAsync(context).execute(apiRequest);
                        }
                    });
                    builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }


            }
        });

        myViewHolder.demand_collection_point_names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openProgressBar();

                if(demandCollection.getDemandDatesList().get(i).getFlag() == false)
                {

                    if(demandCollection.getDemandDatesList().get(i).getClicked() == false)
                    {
                        LinearLayoutManager hs_linearLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        myViewHolder.recyclerView_v2.setLayoutManager(hs_linearLayout);
                        myViewHolder.recyclerView_v2.setHasFixedSize(true);

                        DemandCollectionChildAdapter eventListChildAdapter = new DemandCollectionChildAdapter(demandCollection.getDemandDatesList().get(i).getDemandsArrayList());
                        myViewHolder.recyclerView_v2.setAdapter(eventListChildAdapter);

                        demandCollection.getDemandDatesList().get(i).setClicked(true);
                    }

                    demandCollection.getDemandDatesList().get(i).setFlag(true);
                    myViewHolder.demand_collection_point_names.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_remove_circle_outline_white_36dp, 0);

                    slideUp(myViewHolder.expandableDemandLayout);
                }
                else
                {
                    demandCollection.getDemandDatesList().get(i).setFlag(false);
                    myViewHolder.demand_collection_point_names.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.outline_add_circle_outline_white_36dp, 0);
                    slideDown(myViewHolder.expandableDemandLayout);

                }

            }
        });


    }

    public void openProgressBar()
    {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait");
        dialog.show();
    }

    public void closeProgressBar()
    {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void slideUp(View view){

        // Prepare the View for the animation
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);

// Start the animation
        view.animate()
                .translationY(0)
                .alpha(2.0f)
                .setListener(null);

        closeProgressBar();
    }

    // slide the view from its current position to below itself
    public void slideDown(final View view){
        view.animate()
                .translationY(0)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });

        closeProgressBar();
    }

    @Override
    public int getItemCount()
    {
        return demandCollection.getDemandDatesList().size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView demand_collection_point_names;
        public RecyclerView recyclerView_v2;
        public DemandDate mItem;
        public LinearLayout expandableDemandLayout;
        public Button save,cancel;
        public EditText totalAmount;



        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            demand_collection_point_names = (TextView) itemView.findViewById(R.id.demand_collection_point_names_id);
            recyclerView_v2 = (RecyclerView) itemView.findViewById(R.id.secondRecyclerView);
            expandableDemandLayout = (LinearLayout) itemView.findViewById(R.id.expandable_demand_collection);
            save = (Button) itemView.findViewById(R.id.demand_Collection_save_update);
            cancel = (Button) itemView.findViewById(R.id.demand_Collection_back);
            totalAmount = (EditText) itemView.findViewById(R.id.demand_Collected_amt);
        }
    }
}
