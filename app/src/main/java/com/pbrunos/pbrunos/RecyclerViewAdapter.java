package com.pbrunos.pbrunos;

import android.content.Context;
import android.content.Intent;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mmovieImage;
    private ArrayList<String> mmovieText1;
    private ArrayList<String> mmovieText2;
    private ArrayList<String> mmovieText3;
    private ArrayList<String> mmovieText4;
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mmovieImage, ArrayList<String> mmovieText1, ArrayList<String> mmovieText2, ArrayList<String> mmovieText3,
                               ArrayList<String> mmovieText4, Context mContext) {
        this.mmovieImage = mmovieImage;
        this.mmovieText1 = mmovieText1;
        this.mmovieText2 = mmovieText2;
        this.mmovieText3 = mmovieText3;
        this.mmovieText4 = mmovieText4;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zombie_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext)
                .asBitmap()
                .load(mmovieImage.get(position))
                .into(holder.squareImageView);

        holder.movieText1.setText(mmovieText1.get(position));
        holder.movieText2.setText(mmovieText2.get(position));
        holder.movieText3.setText(mmovieText3.get(position));



        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on " + mmovieText1.get(position));
                Toast.makeText(mContext, mmovieText4.get(position), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(v.getContext(), DisplayMovieInfo.class);
//                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mmovieText1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SquareImageView squareImageView;
        TextView movieText1;
        TextView movieText2;
        TextView movieText3;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            squareImageView = itemView.findViewById(R.id.movie_image);
            movieText1 = itemView.findViewById(R.id.movie_text1);
            movieText2 = itemView.findViewById(R.id.movie_text2);
            movieText3 = itemView.findViewById(R.id.movie_text3);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
