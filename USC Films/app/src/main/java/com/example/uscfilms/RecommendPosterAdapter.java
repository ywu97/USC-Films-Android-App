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

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecommendPosterAdapter  extends RecyclerView.Adapter<RecommendPosterAdapter.MyViewHolder> {
    private Context mContext;
    private List<Poster> list;
    View view;

    public RecommendPosterAdapter(Context mContext, List<Poster> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecommendPosterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.detailpage_poster, parent, false);
        return new RecommendPosterAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendPosterAdapter.MyViewHolder holder, int position) {
//        holder.name.setText(list.get(position).getName());
//        holder.id.setText(list.get(position).getId());
//        holder.category.setText(list.get(position).getCategory());
        Picasso.get().load(list.get(position).getImage()).into(holder.imageView);
        holder.imageView.setClipToOutline(true);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("category", list.get(position).getCategory());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
//        TextView name, id, category;
        public MyViewHolder (@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewD);
//            name = itemView.findViewById(R.id.nametextViewD);
//            category = itemView.findViewById(R.id.categorytextViewD);
//            id = itemView.findViewById(R.id.idtextViewD);
        }
    }
}
