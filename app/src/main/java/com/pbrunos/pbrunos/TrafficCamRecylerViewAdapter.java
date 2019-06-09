package com.pbrunos.pbrunos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theophrast.ui.widget.SquareImageView;

import java.util.ArrayList;

public class TrafficCamRecylerViewAdapter extends RecyclerView.Adapter<TrafficCamRecylerViewAdapter.ViewHolder>{

    private static final String TAG = "TrafficCamRecylerViewAd";

    private ArrayList<TrafficCamera> cameraData;
    private Context mContext;

    public TrafficCamRecylerViewAdapter(ArrayList<TrafficCamera> cameraData, Context mContext) {
        this.cameraData = cameraData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.traffic_cam_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called" );

        TrafficCamera c = cameraData.get(position);

        Glide.with(mContext)
                .load(c.image)
                .into(holder.squareImageView);
        holder.lable.setText(c.label);
        holder.owner.setText(c.owner);

        
//        holder.parent_tc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, v.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return cameraData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        SquareImageView squareImageView;
        TextView lable;
        TextView owner;
        //Double corrdinates;
        RelativeLayout parent_tc;


        public ViewHolder(View itemView) {
            super(itemView);
            squareImageView = itemView.findViewById(R.id.tc_image);
            lable = itemView.findViewById(R.id.tc_lable);
            owner = itemView.findViewById(R.id.tc_owner);
            //corrdinates = itemView.findViewById(R.id.tc_corrdinats);
            parent_tc = itemView.findViewById(R.id.parent_tc);

        }
    }
}
