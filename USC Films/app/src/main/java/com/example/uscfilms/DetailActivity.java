package com.example.uscfilms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY_ORDER = "orderWL";
    private static final String DetailTAG = "DetailPG_SP";
    private static final String localTestURL = "https://hw9backendapi.azurewebsites.net/";

    FrameLayout DetailLoadingBG;
    private ProgressBar progressBar;
    private TextView loadingTextView;

    ImageView backdropImg;
    YouTubePlayerView youTubePlayerView;
    String getVideoURL = new String();
    String getDetails = new String();
    String getCast = new String();
    String getReviews = new String();
    String getRecommended = new String();

    TextView WLBtn;
    TextView WLBtnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String category = intent.getStringExtra("category");

        progressBar = findViewById(R.id.DetailprogressBar);
        loadingTextView = findViewById(R.id.DetailloadingTextView);
        DetailLoadingBG = findViewById(R.id.DetailLoadingBG);
        DetailLoadingBG.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loadingTextView.setVisibility(View.VISIBLE);

        getVideoURL= localTestURL + "Video?id=" + id + "&category=" + category;
        getDetails = localTestURL + "Details?id=" + id + "&category=" + category;
        getCast = localTestURL + "Cast?id=" + id + "&category=" + category;
        getReviews = localTestURL + "Reviews?id=" + id + "&category=" + category;
        getRecommended = localTestURL + "Recommended?id=" + id + "&category=" + category;


        TextView FBBtn = findViewById(R.id.FBBtn);
        FBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlFB = "https://www.facebook.com/sharer/sharer.php?u=www.themoviedb.org/"+category+"/" + id;
                Intent ShareToFB = new Intent(Intent.ACTION_VIEW, Uri.parse(urlFB));
                ShareToFB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ShareToFB);
            }
        });

        TextView TwittBtn = findViewById(R.id.TwittBtn);
        TwittBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlTT = "https://twitter.com/intent/tweet?text=Check%20this%20Out!%0Ahttps://www.themoviedb.org/"+category+"/" + id;
                Intent ShareToTT = new Intent(Intent.ACTION_VIEW, Uri.parse(urlTT));
                ShareToTT.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ShareToTT);
            }
        });

        WLBtn = findViewById(R.id.WLBtn);
        WLBtnRemove = findViewById(R.id.WLBtnRemove);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        StringBuilder sb = new StringBuilder();
        sb.append(category.substring(0,1));
        sb.append(id);
        if (sharedPreferences.contains(sb.toString())){
            WLBtn.setVisibility(View.INVISIBLE);
            WLBtnRemove.setVisibility(View.VISIBLE);
        }else{
            WLBtn.setVisibility(View.VISIBLE);
            WLBtnRemove.setVisibility(View.INVISIBLE);
        }
        getVideo();
        getDetails(sb, category);
        getRecommended(category);
    }


    private void getVideo(){
        backdropImg = findViewById(R.id.backdropImg);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        JsonObjectRequest jsonObjectRequestVideo = new JsonObjectRequest
                (Request.Method.GET, getVideoURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String[] VideoKey = new String[1];
                            JSONArray res = response.getJSONArray("results");
                            for(int i = 0; i < res.length(); i++){
                                JSONObject o = res.getJSONObject(i);
                                if(o.getString("type").equals("Trailer")) {
                                    VideoKey[0] = o.getString("key");
                                    break;
                                }
                            }
                            if(VideoKey[0] != null && VideoKey[0].length() > 0){
                                backdropImg.setVisibility(View.INVISIBLE);
                                youTubePlayerView.setVisibility(View.VISIBLE);
//                                getLifecycle().addObserver(youTubePlayerView);
                                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                        String videoId = VideoKey[0];
                                        youTubePlayer.cueVideo(videoId, 0);
                                    }
                                });
                            } else {
                                backdropImg.setVisibility(View.VISIBLE);
                                youTubePlayerView.setVisibility(View.INVISIBLE);
                            }
                            Log.d("YT", "YTKEY: " + VideoKey[0]);
                        }catch(JSONException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error!");
                    }
                });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequestVideo);
    }


    private void getDetails(StringBuilder sb, String category){
        JsonObjectRequest jsonObjectRequestDetails = new JsonObjectRequest
                (Request.Method.GET, getDetails, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
//                            JSONArray res = response.getJSONArray("results");
                            if(!response.toString().equals("{}")){
//                                JSONObject o = response.getJSONObject(0);
                                String url;
                                if(response.optString("backdrop_path")!= "null" && response.getString("backdrop_path").length() > 0) {
                                    url = "https://image.tmdb.org/t/p/w780" + response.getString("backdrop_path");
                                }else{
                                    url = "https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/movie-placeholder.jpg";
                                }
                                Picasso.get().load(url).into(backdropImg);


                                TextView TitleOrName = findViewById(R.id.TitleOrName);
                                if(category.equals("movie")){
                                    TitleOrName.setText(response.getString("title"));
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    WLBtnRemove.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getApplicationContext(), response.optString("title") + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                                            editor.remove(sb.toString());
                                            String[] order = sharedPreferences.getString(KEY_ORDER,null).split("@");
                                            StringBuilder newKeyorder = new StringBuilder();
                                            for(int i = 0; i < order.length;i++){
                                                if(order[i].equals(sb.toString())) continue;
                                                newKeyorder.append(order[i]);
                                                newKeyorder.append("@");
                                            }
                                            editor.putString(KEY_ORDER, newKeyorder.toString());
                                            editor.commit();

                                            WLBtn.setVisibility(View.VISIBLE);
                                            WLBtnRemove.setVisibility(View.INVISIBLE);
//                                        editor.clear();
//                                        editor.commit();
//                                            Log.i(DetailTAG, "Removing_SP_KEY_ORDER: " + sharedPreferences.getString(KEY_ORDER,null));

                                        }
                                    });
                                    WLBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                        editor.clear();
//                                        editor.commit();
                                            Toast.makeText(getApplicationContext(), response.optString("title") + " was added to Watchlist", Toast.LENGTH_SHORT).show();
                                            if(response.optString("poster_path") == "null"){

                                                StringBuilder value = new StringBuilder();
                                                value.append(response.optString("title"));
                                                value.append("@");
                                                value.append("https://cinemaone.net/images/movie_placeholder.png");
                                                editor.putString(sb.toString(), value.toString());
                                                editor.commit();
                                            }else{
                                                StringBuilder value = new StringBuilder();
                                                value.append(response.optString("title"));
                                                value.append("@");
                                                value.append("https://image.tmdb.org/t/p/w500" + response.optString("poster_path"));
                                                editor.putString(sb.toString(), value.toString());
                                                editor.commit();
                                            }
                                            if(sharedPreferences.contains(KEY_ORDER)){
                                                String order = sharedPreferences.getString(KEY_ORDER,null);
                                                if(order == null || order.length() == 0){
                                                    order = sb.toString() + "@";
                                                    editor.putString(KEY_ORDER, order);
                                                    editor.commit();
                                                }else{
                                                    StringBuilder newKey = new StringBuilder();
                                                    newKey.append(order);
                                                    newKey.append(sb.toString());
                                                    newKey.append("@");
                                                    editor.putString(KEY_ORDER, newKey.toString());
                                                    editor.commit();
                                                }
                                            }else{
                                                String order = sb.toString()+"@";
                                                editor.putString(KEY_ORDER, order);
                                                editor.commit();
                                            }
//                                            Log.i(DetailTAG, "Adding_SP_KEY_ORDER: " + sharedPreferences.getString(KEY_ORDER,null));
                                            WLBtn.setVisibility(View.INVISIBLE);
                                            WLBtnRemove.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }else if(category.equals("tv")){
                                    TitleOrName.setText(response.getString("name"));
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    WLBtnRemove.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getApplicationContext(), response.optString("name") + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                                            editor.remove(sb.toString());

                                            String[] order = sharedPreferences.getString(KEY_ORDER,null).split("@");
                                            StringBuilder newKeyorder = new StringBuilder();
                                            for(int i = 0; i < order.length;i++){
                                                if(order[i].equals(sb.toString())) continue;
                                                newKeyorder.append(order[i]);
                                                newKeyorder.append("@");
                                            }
                                            editor.putString(KEY_ORDER, newKeyorder.toString());
                                            editor.commit();

                                            WLBtn.setVisibility(View.VISIBLE);
                                            WLBtnRemove.setVisibility(View.INVISIBLE);
//                                        editor.clear();
//                                        editor.commit();
                                        }
                                    });
                                    WLBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                        editor.clear();
//                                        editor.commit();
                                            Toast.makeText(getApplicationContext(), response.optString("name") + " was added to Watchlist", Toast.LENGTH_SHORT).show();
                                            if(response.optString("poster_path") == "null"){
                                                StringBuilder value = new StringBuilder();
                                                value.append(response.optString("name"));
                                                value.append("@");
                                                value.append("https://cinemaone.net/images/movie_placeholder.png");
                                                editor.putString(sb.toString(), value.toString());
                                                editor.commit();
                                            }else{
                                                StringBuilder value = new StringBuilder();
                                                value.append(response.optString("name"));
                                                value.append("@");
                                                value.append("https://image.tmdb.org/t/p/w500" + response.optString("poster_path"));
                                                editor.putString(sb.toString(), value.toString());
                                                editor.commit();
                                            }
                                            if(sharedPreferences.contains(KEY_ORDER)){
                                                String order = sharedPreferences.getString(KEY_ORDER,null);
                                                if(order == null || order.length() == 0){
                                                    order = sb.toString() + "@";
                                                    editor.putString(KEY_ORDER, order);
                                                    editor.commit();
                                                }else{
                                                    StringBuilder newKey = new StringBuilder();
                                                    newKey.append(order);
                                                    newKey.append(sb.toString());
                                                    newKey.append("@");
                                                    editor.putString(KEY_ORDER, newKey.toString());
                                                    editor.commit();
                                                }
                                            }else{
                                                String order = sb.toString()+"@";
                                                editor.putString(KEY_ORDER, order);
                                                editor.commit();
                                            }
                                            WLBtn.setVisibility(View.INVISIBLE);
                                            WLBtnRemove.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                                TextView overview = findViewById(R.id.overview);
                                TextView overviewText = findViewById(R.id.overviewText);
                                if(response.optString("overview") != "null" && response.getString("overview").length() > 0){
                                    overviewText.setText(response.getString(("overview")));
                                }else{
                                    overview.setVisibility(View.GONE);
                                    overviewText.setVisibility(View.GONE);
                                }

                                TextView genresText = findViewById(R.id.genresText);
                                TextView genres = findViewById(R.id.genres);
                                if(response.optJSONArray("genres") != null && response.optJSONArray("genres").length() > 0){
                                    StringBuilder sb = new StringBuilder();
                                    for(int i = 0; i < response.getJSONArray("genres").length(); i++ ){
                                        if(i > 0){sb.append(", ");}
                                        JSONObject o = response.getJSONArray("genres").getJSONObject(i);
                                        sb.append(o.optString("name"));
                                    }
                                    genresText.setText(sb.toString());
                                }else{
                                    genresText.setVisibility(View.GONE);
                                    genres.setVisibility(View.GONE);
                                }

                                TextView year = findViewById(R.id.year);
                                TextView yearText = findViewById(R.id.yearText);
                                if(category.equals("movie")){
                                    if(response.optString("release_date") != "null" && response.getString("release_date").length() > 0){
                                        yearText.setText(response.getString("release_date").substring(0,4));
                                    }else{
                                        year.setVisibility(View.GONE);
                                        yearText.setVisibility(View.GONE);
                                    }
                                }
                                else if(category.equals("tv")){
                                    if(response.optString("first_air_date") == "null" && response.getString("first_air_date").length() > 0){
                                        year.setVisibility(View.GONE);
                                        yearText.setVisibility(View.GONE);
                                    }else{
                                        yearText.setText(response.getString("first_air_date").substring(0,4));
                                    }
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

                        System.out.println("Error!");
                    }
                });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequestDetails);
        getCast();
    }

    private void getCast(){
        RecyclerView castRecyler = findViewById(R.id.castRecyler);
        ArrayList<CastData> castList = new ArrayList<>();
        castAdapter castAdapter = new castAdapter(this, castList);
        castRecyler.setAdapter(castAdapter);
        castRecyler.setItemAnimator(new DefaultItemAnimator());
        castRecyler.setLayoutManager(new GridLayoutManager(this, 3));

        JsonObjectRequest requestCast = new JsonObjectRequest
                (Request.Method.GET, getCast, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray res = response.getJSONArray("cast");
                            if(!response.toString().equals("{}") && res.length() > 0){
                                int castLen = Math.min(6, res.length());
                                String url;
                                for(int i = 0; i < castLen; i++){
                                    JSONObject o = res.getJSONObject(i);
                                    if(o.optString("profile_path") != "null"){
                                        url ="https://image.tmdb.org/t/p/w500" + o.getString("profile_path");
                                    }else{
                                        url = "https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/person-placeholder.png";
                                    }
                                    //System.out.println(url);
                                    castList.add( new CastData(url,o.getString("name")));
                                    castAdapter.notifyDataSetChanged();
                                }
                            }else{
                                TextView cast = findViewById(R.id.cast);
                                cast.setVisibility(View.GONE);
                                castRecyler.setVisibility(View.GONE);
                            }
                        }catch(JSONException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error!");

                    }
                });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(requestCast);
        getReview();
    }

    private void getReview(){
        RecyclerView reviewRecyclerView = findViewById(R.id.reviewRecycler);
        List<ReviewCards> reviewList = new ArrayList<>();
        ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));

        JsonObjectRequest jsonObjectRequestReview = new JsonObjectRequest
                (Request.Method.GET, getReviews , null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        loadingTextView.setVisibility(View.GONE);
                        DetailLoadingBG.setVisibility(View.GONE);
                        try{
                            JSONArray res = response.getJSONArray("results");
                            if(!response.toString().equals("{}") && res.length() > 0){
                                int reviewLen = Math.min(3, res.length());
                                for(int i = 0; i < reviewLen; i++){
                                    JSONObject o = res.getJSONObject(i);
                                    StringBuilder sb = new StringBuilder();
                                    JSONObject authorDetails = o.getJSONObject("author_details");
                                    sb.append("by ");
                                    sb.append(authorDetails.optString("username"));
                                    sb.append(" on ");
                                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(o.getString("created_at").substring(0, 10));
                                    String dateStr = new SimpleDateFormat("E, MMM dd yyyy").format(date);
                                    sb.append(dateStr);

                                    StringBuilder rat = new StringBuilder();
                                    int rating = 0;
                                    if(authorDetails.optString("rating") != "null"){ rating = authorDetails.getInt("rating") / 2;}
                                    rat.append(Integer.valueOf(rating));
                                    rat.append("/5");

                                    reviewList.add( new ReviewCards(sb.toString(), rat.toString(), o.getString("content")));
                                    reviewAdapter.notifyDataSetChanged();
                                }
                            }

                            else{
                                TextView review = findViewById(R.id.reviews);
                                review.setVisibility(View.GONE);
                                reviewRecyclerView.setVisibility(View.GONE);
                            }
                        }catch(JSONException | ParseException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        loadingTextView.setVisibility(View.GONE);
                        DetailLoadingBG.setVisibility(View.GONE);
                        System.out.println("Error!");

                    }
                });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequestReview);
    }

    private void getRecommended( String category){
        RecyclerView RecommendedRecycler = findViewById(R.id.RecommendedRecycler);
        List<Poster> recomendList = new ArrayList<>();
        RecommendPosterAdapter recommendPosterAdapter = new RecommendPosterAdapter(this, recomendList);
        RecommendedRecycler.setAdapter(recommendPosterAdapter);
        RecommendedRecycler.setItemAnimator(new DefaultItemAnimator());
        RecommendedRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        JsonObjectRequest reqRecomend = new JsonObjectRequest
                (Request.Method.GET, getRecommended, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray res = response.getJSONArray("results");
                            String url;
                            if(!response.toString().equals("{}") && res.length() > 0){
                                int recLen = Math.min(10, res.length());
                                for(int i = 0; i < recLen; i++){
                                    JSONObject o = res.getJSONObject(i);
                                    if(o.optString("poster_path") != "null" && o.getString("poster_path").length() > 0) {
                                        url = "https://image.tmdb.org/t/p/w500" + o.getString("poster_path");

                                    }else{
                                        url = "https://cinemaone.net/images/movie_placeholder.png";
                                    }
                                    if(category.equals("movie")){
                                        recomendList.add(new Poster(o.getString("id"), o.getString("title"), url, "movie"));
                                        recommendPosterAdapter.notifyDataSetChanged();
                                    }else if(category.equals("tv")) {
                                        recomendList.add(new Poster(o.getString("id"), o.getString("name"), url, "tv"));
                                        recommendPosterAdapter.notifyDataSetChanged();
                                    }
                                }
                            }else{
                                TextView reco = findViewById(R.id.recommended);
                                reco.setVisibility(View.GONE);
                                RecommendedRecycler.setVisibility(View.GONE);
                            }
                        }catch(JSONException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error!");
                    }
                });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(reqRecomend);
    }
}
