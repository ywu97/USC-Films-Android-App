package com.example.uscfilms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;  // Import the DateTimeFormatter class
import java.time.LocalDateTime;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private Context mContext;
    private List<ReviewCards> list;
    View view;


    public ReviewAdapter(Context mContext, List<ReviewCards> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.reviewscard, parent, false);
        return new ReviewAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
        holder.userNameAndDate.setText(list.get(position).getUserNameAndDate());
        holder.rating.setText(list.get(position).getRating());
        holder.reviewText.setText(list.get(position).getReviewText());


        holder.reviewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReviewMoreActivity.class);
                intent.putExtra("userNameAndDate", list.get(position).getUserNameAndDate());
                intent.putExtra("rating", list.get(position).getRating());
                intent.putExtra("reviewText", list.get(position).getReviewText());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
//        System.out.println("list size: " + list.size());
        return list.size();
    }
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView userNameAndDate, rating, reviewText;
        public ReviewAdapterViewHolder (@NonNull View itemView) {
            super(itemView);
            userNameAndDate = itemView.findViewById(R.id.authorAnddate);
            rating = itemView.findViewById(R.id.rating);
            reviewText = itemView.findViewById(R.id.reviewText);

        }
    }
}
