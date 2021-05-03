package com.example.uscfilms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.example.uscfilms.ui.home.HomeFragment;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    public List<SliderData> mSliderItems;
    public Context myContext;

    // Constructor
//    public SliderAdapter(HomeFragment context, ArrayList<SliderData> sliderDataArrayList) {
//        this.mSliderItems = sliderDataArrayList;
//
//    }
    public SliderAdapter(Context myContext, ArrayList<SliderData> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.myContext = myContext;
    }

    // We are inflating the slider_layout 
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final SliderData sliderItem = mSliderItems.get(position);
//
//        // Glide is use to load image
//        // from url in your imageview.
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImgUrl())
                .transform(new BlurTransformation(myContext))
                .into(viewHolder.imageViewBackground);

//        Picasso.get().load(mSliderItems.get(position).getImgUrl()).into(viewHolder.imageViewBackground);
        Picasso.get().load(mSliderItems.get(position).getImgUrl()).into(viewHolder.MyImageViewPoster);

        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, DetailActivity.class);
                intent.putExtra("id", sliderItem.getId());
                intent.putExtra("category", sliderItem.getCategory());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myContext.startActivity(intent);
            }
        });

        viewHolder.MyImageViewPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, DetailActivity.class);
                intent.putExtra("id", sliderItem.getId());
                intent.putExtra("category", sliderItem.getCategory());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myContext.startActivity(intent);
            }
        });

    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing 
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground, MyImageViewPoster;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            MyImageViewPoster = itemView.findViewById(R.id.myimagePoster);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }
}

class BlurTransformation extends BitmapTransformation {

    private RenderScript rs;

    public BlurTransformation(Context context) {
        super();

        rs = RenderScript.create(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);

        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(rs, input.getType());

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);

        // Set the blur radius
        script.setRadius(20);

        // Start the ScriptIntrinisicBlur
        script.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(blurredBitmap);

        return blurredBitmap;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update("blur transformation".getBytes());
    }
}