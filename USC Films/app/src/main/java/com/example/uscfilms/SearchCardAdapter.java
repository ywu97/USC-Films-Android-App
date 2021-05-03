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

import org.w3c.dom.Text;

import java.util.List;

public class SearchCardAdapter extends RecyclerView.Adapter<SearchCardAdapter.MyViewHolder> {
    private Context mContext;
    private List<SearchCard> list;
    View view;

    public SearchCardAdapter(Context mContext, List<SearchCard> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.searchcard, parent, false);
        return new SearchCardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCardAdapter.MyViewHolder holder, int position) {
        if(getItemCount() != 0){
            holder.name.setText(list.get(position).getSearchTitleName());
            holder.category.setText(list.get(position).getSearchCategory());
            if(list.get(position).getSearchStarsRate().equals("100.0")){
                holder.star.setVisibility(View.INVISIBLE);
                holder.starIcon.setVisibility(View.INVISIBLE);
            }else{
                holder.star.setText(list.get(position).getSearchStarsRate());
                holder.star.setVisibility(View.VISIBLE);
                holder.starIcon.setVisibility(View.VISIBLE);
            }
            if(list.get(position).getSearchYear().startsWith("@")){
                holder.year.setVisibility(View.GONE);
            }else{
                holder.year.setText(list.get(position).getSearchYear());
                holder.year.setVisibility(View.VISIBLE);
            }
            Picasso.get().load(list.get(position).getSearchImg()).into(holder.imageView);
            //        holder.imageView.setClipToOutline(true);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("id", list.get(position).getSearchid());
                    intent.putExtra("category", list.get(position).getSearchCategory());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, year, category, star, starIcon;
        public MyViewHolder (@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.searchImg);
            name = itemView.findViewById(R.id.SearchTAndN);
            category = itemView.findViewById(R.id.searchCategory);
            year = itemView.findViewById(R.id.searchYear);
            star = itemView.findViewById(R.id.searchStar);
            starIcon = itemView.findViewById(R.id.SearchstarIcon);

        }
    }

}
