package com.example.uscfilms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class castAdapter extends RecyclerView.Adapter<castAdapter.castAdapterViewHolder>  {
    public List<CastData> castArrayList;
    public Context myContext;

    public castAdapter(Context myContext, ArrayList<CastData> castArrayList) {
        this.castArrayList = castArrayList;
        this.myContext = myContext;
    }
    @NonNull
    @Override
    public castAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast, null);
        return new castAdapter.castAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull castAdapterViewHolder holder, int position) {
        final CastData castItem = castArrayList.get(position);
        holder.name.setText(castItem.getName());
        Picasso.get().load(castArrayList.get(position).getImgUrl()).transform(new CropCircleTransformation()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return castArrayList.size();
    }

    public class castAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        public castAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.castImage);
            name = itemView.findViewById(R.id.castName);
        }
    }
}
