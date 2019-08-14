package com.qbent.enfinsapp.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.TableLayout;
import android.widget.TextView;

import com.qbent.enfinsapp.CollectionPointDetailActivity;
import com.qbent.enfinsapp.R;
import com.qbent.enfinsapp.model.CollectionPoint;

import java.util.List;
import java.util.Locale;

public class CollectionPointListRecyclerViewAdapter extends
        RecyclerView.Adapter<CollectionPointListRecyclerViewAdapter.CollectionPointListViewHolder> {

    private final List<CollectionPoint> collectionPoints;

    public CollectionPointListRecyclerViewAdapter(List<CollectionPoint> collectionPoints) {
        this.collectionPoints = collectionPoints;
    }

    @NonNull
    @Override
    public CollectionPointListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_collection_point, viewGroup, false);
        return new CollectionPointListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionPointListViewHolder collectionPointListViewHolder,final int i) {
        collectionPointListViewHolder.mItem = collectionPoints.get(i);
        collectionPointListViewHolder.mNameView.setText(collectionPoints.get(i).getName());
        collectionPointListViewHolder.mAddressView.setText(collectionPoints.get(i).getAddress());
        collectionPointListViewHolder.mCollectionDayView.setText(collectionPoints.get(i).getCollectionDay());

        if(collectionPoints.get(i).getLat().equals("null"))
        {
            collectionPointListViewHolder.mLocationView.setVisibility(View.GONE);
        }
        else
        {
            collectionPointListViewHolder.mLocationView.setVisibility(View.VISIBLE);
        }

        collectionPointListViewHolder.mLocationView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String geoUri = "http://maps.google.com/maps?q=loc:" + collectionPoints.get(i).getLat() + "," + collectionPoints.get(i).getLon() + " (" + collectionPoints.get(i).getAddress() + ")";
//                String uri = "http://maps.google.com/maps?saddr=" + collectionPoints.get(i).getLat() + "," + collectionPoints.get(i).getLon() + "&daddr=" + collectionPoints.get(i).getLat() + "," + collectionPoints.get(i).getLon();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                view.getContext().startActivity(intent);

//                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", collectionPoints.get(i).getLat(), collectionPoints.get(i).getLon());
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                view.getContext().startActivity(intent);

            }
        });



        collectionPointListViewHolder.gridLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Toast.makeText(view.getContext(),"You Clicked "+members.get(i).getFullName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), CollectionPointDetailActivity.class);
                intent.putExtra("collection_id",collectionPoints.get(i).getId());
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return collectionPoints.size();
    }

    public class CollectionPointListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mAddressView;
        public final TextView mCollectionDayView;
        public final Button mLocationView;
        public CollectionPoint mItem;
        public GridLayout gridLayout;

        public CollectionPointListViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.textName);
            mAddressView = (TextView) view.findViewById(R.id.textAddress);
            mLocationView = (Button) view.findViewById(R.id.buttonLocationCP);
            mCollectionDayView = (TextView) view.findViewById(R.id.textCollectionDay);
            gridLayout = (GridLayout) view.findViewById(R.id.collectionPointGridLayout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAddressView.getText() + "'";
        }
    }
}
