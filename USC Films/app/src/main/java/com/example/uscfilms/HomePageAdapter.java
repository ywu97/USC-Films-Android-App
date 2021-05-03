package com.example.uscfilms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.MyViewHolder>{

    private Context mContext;
    private List<Poster> list;
    View view;
    private static final String SHARED_PREFS = "sharedPrefs";
    SharedPreferences sharedPreferences;
    private static final String KEY_ORDER = "orderWL";
//    private static final String POSTER = "poster";
//    Model temp;
    private static final String TAG = "SP";

    public HomePageAdapter(Context mContext, List<Poster> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.homepage_poster, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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



        holder.menuPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());



                StringBuilder sb = new StringBuilder();
                sb.append(list.get(position).getCategory().substring(0,1));
                sb.append(list.get(position).getId());
                sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                if(sharedPreferences.contains(sb.toString())){
                    popupMenu.getMenu().findItem(R.id.AddWL).setTitle("Remove from Watchlist");
                }else{
                    popupMenu.getMenu().findItem(R.id.AddWL).setTitle("Add to Watchlist");
                }

                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.TMDB:
                                String url = "https://www.themoviedb.org/"+list.get(position).getCategory()+"/" + list.get(position).getId();
                                Intent openTMDB = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                openTMDB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(openTMDB);
                                break;
                            case R.id.ShareFB:
                                String urlFB = "https://www.facebook.com/sharer/sharer.php?u=www.themoviedb.org/"+list.get(position).getCategory()+"/" + list.get(position).getId();
                                Intent ShareToFB = new Intent(Intent.ACTION_VIEW, Uri.parse(urlFB));
                                ShareToFB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(ShareToFB);
                                break;
                            case R.id.ShareTwitter:
                                String urlTT = "https://twitter.com/intent/tweet?text=Check%20this%20Out!%0Ahttps://www.themoviedb.org/"+list.get(position).getCategory()+"/" + list.get(position).getId();
                                Intent ShareToTT = new Intent(Intent.ACTION_VIEW, Uri.parse(urlTT));
                                ShareToTT.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(ShareToTT);
                                break;
                            case R.id.AddWL:
//                                Toast.makeText(mContext, "WatchList", Toast.LENGTH_SHORT).show();

//                                deleteItem(position, v);
                                updateWL (position);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void updateWL(int position){
        sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder key = new StringBuilder();
        if(list.get(position).getCategory().equals("movie")) {
            key.append("m");

        }else if(list.get(position).getCategory().equals("tv")){
            key.append("t");
        }
        key.append(list.get(position).getId());

        StringBuilder value = new StringBuilder();
        value.append(list.get(position).getName());
        value.append("@");
        value.append(list.get(position).getImage());

        if(sharedPreferences.contains(key.toString())){
            editor.remove(key.toString());
            Toast.makeText(mContext, list.get(position).getName()+" was removed from Watchlist", Toast.LENGTH_SHORT).show();
            String[] order = sharedPreferences.getString(KEY_ORDER,null).split("@");

            for(int i = 0; i < order.length; i++){
                if(order[i].equals(key.toString())){
                    order[i] = "";
                }
            }
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < order.length; i++){
                if(order[i].length() == 0) continue;
                sb.append(order[i]);
                sb.append("@");
            }
            editor.putString(KEY_ORDER, sb.toString());
            editor.commit();

        }else{
//            Log.i(TAG,"SPgetKEY_ORDER: "+ sharedPreferences.getString(KEY_ORDER,null));

            editor.putString(key.toString(), value.toString());
            if(sharedPreferences.contains(KEY_ORDER)){
                String order = sharedPreferences.getString(KEY_ORDER,null);
                if(order == null || order.length() == 0){
                    order = key.toString() +"@";
                    editor.putString(KEY_ORDER, order);
                    editor.commit();
                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append(order);
                    sb.append(key.toString());
                    sb.append("@");
                    editor.putString(KEY_ORDER, sb.toString());
                    editor.commit();
                }
            }else{
                Log.i(TAG, "no Key_order contains");
                String order = key.toString() + "@";
                editor.putString(KEY_ORDER,order);
                editor.commit();
            }

            Toast.makeText(mContext, list.get(position).getName()+" was added to Watchlist", Toast.LENGTH_SHORT).show();
        }

//        editor.commit();
    }

    @Override
    public int getItemCount() {
//        System.out.println("list size: " + list.size());
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView menuPopUp;
//        name, id, category,
        public MyViewHolder (@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
//            name = itemView.findViewById(R.id.nametextView);
//            category = itemView.findViewById(R.id.categorytextView);
//            id = itemView.findViewById(R.id.idtextView);
            menuPopUp = itemView.findViewById(R.id.popuptextView3);
        }
    }
}


