package com.example.myapplication;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ReviewsFragment extends Fragment {

    String busid;


    //hold data for reviews
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> rating = new ArrayList<>();
    ArrayList<String> text = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();

    RecyclerView recyclerView;


    public ReviewsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        recyclerView = view.findViewById(R.id.recyclerviewratings);


        busid = getActivity().getIntent().getStringExtra("busid");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String backendurl = "https://yelpcloneangular.wl.r.appspot.com/reviews?id=";
        backendurl+=busid;
        System.out.println(backendurl);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                backendurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray reviewarr = response.getJSONArray("reviews");
                            for (int i=0;i<reviewarr.length();i++){
                                JSONObject review = reviewarr.getJSONObject(i);
                                String ratingnow = (review.getString("rating"));
                                String ratingtoadd = String.format("Rating :%s/5",ratingnow);
                                rating.add(ratingtoadd);
                                text.add(review.getString("text"));
                                String time = review.getString("time_created");
                                String arr[] = time.split(" ", 2);
                                String first = arr[0];   //the
                                date.add(first);
                                JSONObject user = review.getJSONObject("user");
                                name.add(user.getString("name"));

                            }
                            MyReviewAdapter myReviewAdapter = new MyReviewAdapter(name,rating,text,date);
                            recyclerView.setAdapter(myReviewAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                            GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xff161616, 0xff161616});
                            drawable.setSize(7,7);
                            itemDecoration.setDrawable(drawable);
                            recyclerView.addItemDecoration(itemDecoration);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjReq);

        return view;
    }
}