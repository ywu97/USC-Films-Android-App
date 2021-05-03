package com.example.uscfilms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.uscfilms.ui.notifications.ItemMoveCallback;
import com.example.uscfilms.DragAndDrop.ItemTouchHelperAdapter;
import com.example.uscfilms.DragAndDrop.OnStartDragListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

// implements ItemMoveCallback.ItemTouchHelperContract

public class WLPosterAdapter extends RecyclerView.Adapter<WLPosterAdapter.MyViewHolder>
implements ItemTouchHelperAdapter {
    private Context mContext;
    private List<Poster> list;
    View view;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    OnStartDragListener listener;
    private static final String KEY_ORDER = "orderWL";

    public WLPosterAdapter(Context mContext, List<Poster> list, OnStartDragListener listener) {
        this.mContext = mContext;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WLPosterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.wl_poster, parent, false);
        return new WLPosterAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WLPosterAdapter.MyViewHolder holder, int position) {
        holder.category.setText(list.get(position).getCategory());
        Picasso.get().load(list.get(position).getImage()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                if (list.get(position).getCategory().equals("Movie")) {
                    intent.putExtra(("category"), "movie");
                } else if (list.get(position).getCategory().equals("TV")) {
                    intent.putExtra("category", "tv");
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
//        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int action = event.getAction();
//                if(action == MotionEvent.ACTION_DOWN)
//                    listener.onStartDrag(holder);
//                return false;
//            }
//        });
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onStartDrag(holder);
                return false;
            }
        });
        holder.WLremoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.commit();

                StringBuilder sb = new StringBuilder();
                if (list.get(position).getCategory().equals("Movie")) {
                    sb.append("m");
                } else if (list.get(position).getCategory().equals("TV")) {
                    sb.append("t");
                }
                sb.append(list.get(position).getId());
                if (sharedPreferences.contains(sb.toString())) {
                    editor.remove(sb.toString());
                }
                String[] order = sharedPreferences.getString(KEY_ORDER, null).split("@");
                StringBuilder newOrder = new StringBuilder();
                for (int i = 0; i < order.length; i++) {
                    if (order[i].equals(sb.toString())) continue;
                    newOrder.append(order[i]);
                    newOrder.append("@");
                }
                editor.putString(KEY_ORDER, newOrder.toString());
                editor.commit();
                holder.WLCardView.setVisibility(View.GONE);
                Toast.makeText(mContext, "'" + list.get(position).getName() + "'" + " was removed from favourites", Toast.LENGTH_SHORT).show();
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        StringBuilder from = new StringBuilder();
        if (list.get(fromPosition).getCategory().equals("Movie")) {
            from.append("m");
        } else if (list.get(fromPosition).getCategory().equals("TV")) {
            from.append("t");
        }
        from.append(list.get(fromPosition).getId());
        Log.i("from key", "From: " + from.toString());
        StringBuilder to = new StringBuilder();
        if (list.get(toPosition).getCategory().equals("Movie")) {
            to.append("m");
        } else if (list.get(toPosition).getCategory().equals("TV")) {
            to.append("t");
        }
        Log.i("to key", "To: " + to.toString());
        to.append(list.get(toPosition).getId());
        sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String[] order = sharedPreferences.getString(KEY_ORDER, null).split("@");
        StringBuilder newKey = new StringBuilder();
        for (int i = 0; i < order.length; i++) {
            if (order[i].equals(from.toString())) {
                order[i] = to.toString();
            } else if (order[i].equals(to.toString())) {
                order[i] = from.toString();
            }
            newKey.append(order[i]);
            newKey.append("@");
        }
        editor.putString(KEY_ORDER, newKey.toString());
        editor.commit();

        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemChanged(fromPosition);
        notifyItemChanged(toPosition);
        notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView category, WLremoveBtn;
        CardView WLCardView;
        Unbinder unbinder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            WLCardView = itemView.findViewById(R.id.WLCardView);
            WLremoveBtn = itemView.findViewById(R.id.WLremoveBtn);
            imageView = itemView.findViewById(R.id.imageViewWL);
            category = itemView.findViewById(R.id.categorytextViewWL);
        }
    }
}


