package com.example.uscfilms.ui.home;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.transition.CircularPropagation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uscfilms.HomePageAdapter;
import com.example.uscfilms.Poster;
import com.example.uscfilms.R;
import com.example.uscfilms.SliderAdapter;
import com.example.uscfilms.SliderData;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    // ============================================================================================
    RecyclerView topRatedMovRecyclerView;
    List<Poster> topRatedMovList;
    HomePageAdapter topRatedMovAdapter;

    RecyclerView popularMovRecyclerView;
    List<Poster> popularMovList;
    HomePageAdapter popularMovAdapter;

    RecyclerView topRatedTVRecyclerView;
    List<Poster> topRatedTVList;
    HomePageAdapter topRatedTVAdapter;

    RecyclerView popularTVRecyclerView;
    List<Poster> popularTVList;
    HomePageAdapter popularTVAdapter;

    SliderView sliderView;
    private ArrayList<SliderData> sliderDataArrayList;
    SliderAdapter adapter;
    private RequestQueue requestQueue;

    private ArrayList<SliderData> sliderTV;
    private RequestQueue requestTvSlide;
    SliderView sliderViewTV;
    SliderAdapter adapterTV;
    // ============================================================================================
    private ProgressBar progressBar;
    private TextView loadingTextView;
    private FrameLayout loadingBG;


    private static final String localTestURL = "https://hw9backendapi.azurewebsites.net/";

    private static final String MnowPlayingURL = localTestURL + "currentMovies";
    private static final String MTopRatedURL = localTestURL + "topRatedMovies";
    private static final String MPopularURL = localTestURL + "popularMovies";
    private static final String TVTrendURL = localTestURL + "trendingTvShows";
    private static final String TVTopRatedURL = localTestURL + "topRatedTvShows";
    private static final String TVPopularURL = localTestURL + "popularTvShows";
    View root;
//    Handler handler = new Handler(Looper.getMainLooper());
//    Runnable runnable;
//    private int reqInterval = 200000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = root.findViewById(R.id.progressBar);
        loadingTextView = root.findViewById(R.id.loadingTextView);
        loadingBG = root.findViewById(R.id.loadingBG);
        progressBar.setVisibility(View.VISIBLE);
        loadingTextView.setVisibility(View.VISIBLE);
        loadingBG.setVisibility(View.VISIBLE);

        TextView homeBtn = root.findViewById(R.id.TitleHome);
        TextView moviePgBtn = root.findViewById(R.id.moviePage);
        TextView tvPgBtn = root.findViewById(R.id.tvPage);

        ScrollView moviePage = root.findViewById(R.id.scrollView2);
        ScrollView tvPage = root.findViewById(R.id.scrollViewTV);

        moviePage.setVisibility(View.VISIBLE);
        tvPage.setVisibility(View.INVISIBLE);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviePgBtn.setTextColor(getResources().getColor(R.color.white, null));
                tvPgBtn.setTextColor(getResources().getColor(R.color.blue, null));
                moviePage.setVisibility(View.VISIBLE);
                tvPage.setVisibility(View.INVISIBLE);
            }
        });

        moviePgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviePgBtn.setTextColor(getResources().getColor(R.color.white, null));
                tvPgBtn.setTextColor(getResources().getColor(R.color.blue, null));
                moviePage.setVisibility(View.VISIBLE);
                tvPage.setVisibility(View.INVISIBLE);
            }
        });

        tvPgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPgBtn.setTextColor(getResources().getColor(R.color.white, null));
                moviePgBtn.setTextColor(getResources().getColor(R.color.blue, null));
                moviePage.setVisibility(View.INVISIBLE);
                tvPage.setVisibility(View.VISIBLE);
            }
        });


        getCurrentMovie(root);
        ///footer
        TextView linkTextView = root.findViewById(R.id.footer);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView.setLinkTextColor(getResources().getColor(R.color.blue, null));

        String TMDB = "https://www.themoviedb.org/";
        Spannable s = new SpannableString(linkTextView.getText());
        s.setSpan(new URLSpanNoUnderline(TMDB), 0 , linkTextView.length(),0);
        linkTextView.setText(s);

        TextView linkTextView2 = root.findViewById(R.id.footer2);
        linkTextView2.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView2.setLinkTextColor(getResources().getColor(R.color.blue, null));

        linkTextView2.setText(s);

        return root;

    }

    private void getCurrentMovie(View root){
        sliderDataArrayList = new ArrayList<>();
        sliderView = root.findViewById(R.id.slider1);
        adapter = new SliderAdapter(getActivity().getApplicationContext(), sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, MnowPlayingURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray res = response.getJSONArray("results");
                            if(!response.toString().equals("{}")){
                                int count = 0;
                                for(int i = 0; i < res.length(); i++){
                                    if(count == 6) break;
                                    JSONObject o = res.getJSONObject(i);
                                    if(o.optString("poster_path") == "null"){
                                        continue;
                                    }
                                    count++;
                                    String url ="https://image.tmdb.org/t/p/w500" + o.getString("poster_path");
                                    sliderDataArrayList.add( new SliderData(url, o.getString("id"), "movie"));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }catch(JSONException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("MPlaying_Error!");

                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonObjectRequest);
        getTopRatedMovie(root);
    }



    private void getTopRatedMovie( View root){
        topRatedMovRecyclerView = root.findViewById(R.id.TopRatedMovRecycler);
        topRatedMovList = new ArrayList<>();
        topRatedMovAdapter = new HomePageAdapter(getActivity().getApplicationContext(), topRatedMovList);
        topRatedMovRecyclerView.setAdapter(topRatedMovAdapter);
        topRatedMovRecyclerView.setItemAnimator(new DefaultItemAnimator());
        topRatedMovRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));

        JsonObjectRequest reqTopRatedMovie = new JsonObjectRequest
                (Request.Method.GET, MTopRatedURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray res = response.getJSONArray("results");
                            if(!response.toString().equals("{}")){
                                int count = 0;
                                for(int i = 0; i < res.length(); i++){
                                    if(count == 10) break;
                                    JSONObject o = res.getJSONObject(i);
                                    if(o.optString("poster_path") == "null"){
                                        continue;
                                    }
                                    count++;
                                    String url ="https://image.tmdb.org/t/p/w500" + o.getString("poster_path");
                                    topRatedMovList.add(new Poster(o.getString("id"), o.getString("title"), url, "movie"));
                                    topRatedMovAdapter.notifyDataSetChanged();

                                }
                            }
                        }catch(JSONException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("MTopRatedURL_Error!");

                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(reqTopRatedMovie);
        getPopularMovie(root);
    }
    private void getPopularMovie (View root){
        popularMovRecyclerView = root.findViewById(R.id.popMovRecycler);
        popularMovList = new ArrayList<>();
        popularMovAdapter = new HomePageAdapter(getActivity().getApplicationContext(), popularMovList);
        popularMovRecyclerView.setAdapter(popularMovAdapter);
        popularMovRecyclerView.setItemAnimator(new DefaultItemAnimator());
        popularMovRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));

        JsonObjectRequest reqPopMovie = new JsonObjectRequest
                (Request.Method.GET, MPopularURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray res = response.getJSONArray("results");
                            if(!response.toString().equals("{}")){
                                int count = 0;
                                for(int i = 0; i < res.length(); i++){
                                    if(count == 10) break;
                                    JSONObject o = res.getJSONObject(i);
                                    if(o.optString("poster_path") == "null") continue;
                                    count++;
                                    String url ="https://image.tmdb.org/t/p/w500" + o.getString("poster_path");
                                    popularMovList.add(new Poster(o.getString("id"), o.getString("title"), url, "movie"));
                                    popularMovAdapter.notifyDataSetChanged();

                                }
                            }
                        }catch(JSONException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("MPopularURL_Error!");

                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(reqPopMovie);
        getTrendingTv(root);
    }

    private void getTrendingTv (View root){
        sliderTV = new ArrayList<>();
        sliderViewTV = root.findViewById(R.id.slider2);
        adapterTV = new SliderAdapter(getActivity().getApplicationContext(), sliderTV);
        sliderViewTV.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderViewTV.setSliderAdapter(adapterTV);
        sliderViewTV.setScrollTimeInSec(3);
        sliderViewTV.setAutoCycle(true);
        sliderViewTV.startAutoCycle();
        JsonObjectRequest jsonObjectRequestTVSlider = new JsonObjectRequest
                (Request.Method.GET, TVTrendURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray res = response.getJSONArray("results");
                            if(!response.toString().equals("{}")){
                                int count = 0;
                                for(int i = 0; i < res.length(); i++){
                                    if(count == 6) break;
                                    JSONObject o = res.getJSONObject(i);
                                    if(o.optString("poster_path") == "null") continue;
                                    count++;
                                    String url ="https://image.tmdb.org/t/p/w500" + o.getString("poster_path");
                                    //System.out.println(url);
                                    sliderTV.add( new SliderData(url,o.getString("id"), "tv"));
                                    adapterTV.notifyDataSetChanged();
                                }
                            }
                        }catch(JSONException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("TVTrendURL_Error!");

                    }
                });
        requestTvSlide = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestTvSlide.add(jsonObjectRequestTVSlider);
        getTopRatedTv(root);
    }


    private void getTopRatedTv (View root){
        topRatedTVRecyclerView = root.findViewById(R.id.TopRatedTVRecycler);
        topRatedTVList = new ArrayList<>();
        topRatedTVAdapter = new HomePageAdapter(getActivity().getApplicationContext(), topRatedTVList);
        topRatedTVRecyclerView.setAdapter(topRatedTVAdapter);
        topRatedTVRecyclerView.setItemAnimator(new DefaultItemAnimator());
        topRatedTVRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));

        JsonObjectRequest reqTopRatedTV = new JsonObjectRequest
                (Request.Method.GET, TVTopRatedURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray res = response.getJSONArray("results");
                            if(!response.toString().equals("{}")){
                                int count = 0;
                                for(int i = 0; i < res.length(); i++){
                                    if(count == 10)break;
                                    JSONObject o = res.getJSONObject(i);
                                    if(o.optString("poster_path") == "null")continue;
                                    count++;
                                    String url ="https://image.tmdb.org/t/p/w500" + o.getString("poster_path");
                                    topRatedTVList.add(new Poster(o.getString("id"), o.getString("name"), url, "tv"));
                                    topRatedTVAdapter.notifyDataSetChanged();

                                }
                            }
                        }catch(JSONException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("TVTopRatedURL_Error!");

                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(reqTopRatedTV);
        getPopularTv(root);
    }
    private void getPopularTv(View root){
        popularTVRecyclerView = root.findViewById(R.id.popTVRecycler);
        popularTVList = new ArrayList<>();
        popularTVAdapter = new HomePageAdapter(getActivity().getApplicationContext(), popularTVList);
        popularTVRecyclerView.setAdapter(popularTVAdapter);
        popularTVRecyclerView.setItemAnimator(new DefaultItemAnimator());
        popularTVRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.HORIZONTAL, false));

        JsonObjectRequest reqPopTV = new JsonObjectRequest
                (Request.Method.GET, TVPopularURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        loadingTextView.setVisibility(View.GONE);
                        loadingBG.setVisibility(View.GONE);
                        try{
                            JSONArray res = response.getJSONArray("results");
                            if(!response.toString().equals("{}")){
                                int count = 0;
                                for(int i = 0; i < res.length(); i++){
                                    if(count == 10)break;
                                    JSONObject o = res.getJSONObject(i);
                                    if(o.optString("poster_path") == "null")continue;
                                    count++;
                                    String url ="https://image.tmdb.org/t/p/w500" + o.getString("poster_path");
                                    popularTVList.add(new Poster(o.getString("id"), o.getString("name"), url, "tv"));
                                    popularTVAdapter.notifyDataSetChanged();

                                }
                            }
                        }catch(JSONException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        loadingTextView.setVisibility(View.GONE);
                        loadingBG.setVisibility(View.GONE);
                        System.out.println("TVPopularURL_Error!");

                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(reqPopTV);
    }
}

class URLSpanNoUnderline extends URLSpan {
    public URLSpanNoUnderline(String url) {
        super(url);
    }
    @Override public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
    public static void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }
}
