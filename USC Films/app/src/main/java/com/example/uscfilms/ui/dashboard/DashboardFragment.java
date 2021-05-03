package com.example.uscfilms.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.uscfilms.R;
import com.example.uscfilms.SearchCard;
import com.example.uscfilms.SearchCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {

    private static final String localTestURL = "https://hw9backendapi.azurewebsites.net/";


    RecyclerView resultMedia;
    List<SearchCard> SearchResultList;
    SearchCardAdapter searchResultAdapter;
    TextView noRes;
    String multiSearch;
    private RequestQueue requestQueue;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchView searchInput;

        View root = inflater.inflate(R.layout.fragment_searchpage, container, false);

        Toolbar Searchtoolbar = root.findViewById(R.id.Searchtoolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(Searchtoolbar);

        searchInput = (SearchView) root.findViewById(R.id.searchView);
        searchInput.setIconifiedByDefault(true);
        searchInput.setFocusable(true);
        searchInput.setIconified(false);
        searchInput.requestFocusFromTouch();
        searchInput.setQueryHint("Search movies and TV");

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        noRes = root.findViewById(R.id.noSearchResult);
        resultMedia = (RecyclerView) root.findViewById(R.id.resultMedia);
        SearchResultList = new ArrayList<>();
        searchResultAdapter = new SearchCardAdapter(getActivity().getApplicationContext(), SearchResultList);
        resultMedia.setAdapter(searchResultAdapter);
        resultMedia.setItemAnimator(new DefaultItemAnimator());
        resultMedia.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false));

        ImageView clearButton = searchInput.findViewById(androidx.appcompat.R.id.search_close_btn);
        clearButton.setOnClickListener(v -> {
            if(searchInput.getQuery().length() == 0) {
                searchInput.setIconified(true);
            } else {
                searchInput.setQuery("", false);
                SearchResultList.clear();
                searchResultAdapter.notifyDataSetChanged();
            }
        });

        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.trim() == null){
                    query = "%20";
                    return false;
                }else{
                    multiSearch = localTestURL + "getSearchResult?queryInput=" + query;
                    doSearch();
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    noRes.setVisibility(View.INVISIBLE);
                    resultMedia.setVisibility(View.VISIBLE);
                    if(newText.trim() != null && newText.trim().length() > 0) {
                        multiSearch = localTestURL + "getSearchResult?queryInput=" + newText;
                        doSearch();
                        return false;
                    }else{
                        return false;
                    }
            }
        });
        return root;
    }
    private void doSearch(){
        JsonObjectRequest searchReq = new JsonObjectRequest
                (Request.Method.GET, multiSearch, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SearchResultList.clear();
                        noRes.setVisibility(View.INVISIBLE);
                        resultMedia.setVisibility(View.VISIBLE);
                        try{
                            JSONArray res = response.getJSONArray("results");
                            String url;
                            int count = 0;
                            if(!response.toString().equals("{}") && res.length() > 0){
                                for(int i = 0; i < res.length(); i++){
                                    if(count == 20) break;
                                    JSONObject o = res.getJSONObject(i);
                                    if(o.getString("media_type").equals("person") || o.optString("backdrop_path") == "null"){
                                        continue;
                                    } else {
                                        count++;
                                        url = "https://image.tmdb.org/t/p/w780" + o.getString("backdrop_path");
                                        String starStr = "";
                                        if(o.optString("vote_average") == "null"){
                                            Double starS = 100.0;
                                            starStr = starS.toString();
                                        }else{
                                            Double starS = o.optDouble("vote_average");
                                            starStr = starS.toString(starS/2.0);
                                        }


                                        if (o.getString("media_type").equals("movie")) {
                                            StringBuilder sb = new StringBuilder();
                                            if(o.optString("release_date") == "null" || o.optString("release_date").length() == 0){
                                                sb.append("@");
                                            }else{
                                                sb.append("(");
                                                sb.append(o.optString("release_date").substring(0,4));
                                                sb.append(")");
                                            }


                                            SearchResultList.add(new SearchCard("movie", sb.toString() , o.optString("title"), starStr, url, o.getString("id")));
                                            searchResultAdapter.notifyDataSetChanged();
                                        }
                                        if (o.getString("media_type").equals("tv")) {
                                            StringBuilder sb = new StringBuilder();
                                            if(o.optString("first_air_date") == "null" || o.optString("first_air_date").length() == 0){
                                                sb.append("@");
                                            }else {
                                                sb.append("(");
                                                sb.append(o.optString("first_air_date").substring(0,4));
                                                sb.append(")");
                                            }

                                            SearchResultList.add(new SearchCard("tv",sb.toString() , o.getString("name"), starStr, url, o.getString("id")));
                                            searchResultAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                            if(SearchResultList.size() == 0){
                                noRes.setVisibility(View.VISIBLE);
                                resultMedia.setVisibility(View.INVISIBLE);
                            }

                        }catch(JSONException e){
                            System.out.println("Problem: "+ e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("NoInput!");

                    }
                });
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(searchReq);
    }
}