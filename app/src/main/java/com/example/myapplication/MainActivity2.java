package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {


    // busdetails variables
    String busname;
    String address;
    String pricerange;
    String phonenumber;
    double lat;
    double lng;
    ArrayList<String> urlofphotos = new ArrayList<String>();
    String busurl;
    String isopennow;
    String category;


    ImageView facebookimg;
    ImageView twitterimg;
    TextView title;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyApplication);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        RequestQueue queue = Volley.newRequestQueue(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);
        // busid search
        String busid;
        busid=getdata();
        setdata(busid,queue);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        // finding all the views
        facebookimg = (ImageView) findViewById(R.id.fbicon);
        twitterimg = (ImageView) findViewById(R.id.twittericon);
        title = findViewById(R.id.header);


    }


    private String getdata() {
        return getIntent().getStringExtra("busid");
    }

    private void setdata(String busid, RequestQueue queue) {
        String backendurl = "https://yelpcloneangular.wl.r.appspot.com/searchbusiness?id=";
        backendurl+=busid;
        System.out.println(backendurl);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                backendurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            busname=response.getString("name");
                            title.setText(busname);
                            busurl=response.getString("url");
                            phonenumber = response.getString("display_phone");
                            JSONObject addobj = response.getJSONObject("location");
                            address+=addobj.getString("address1") + " " + addobj.getString("address2")+" "+addobj.getString("city")+ " "+addobj.getString("zip_code");
                            address = address.replace("null","");
                            JSONArray catarr = response.getJSONArray("categories");
                            for (int i=0;i<catarr.length();i++){
                                JSONObject catobj = catarr.getJSONObject(i);
                                category+= catobj.getString("title") + " | ";
                            }
                            category = category.substring(0,category.length()-2);
                            category = category.replace("null","");
                            JSONObject coordobj = response.getJSONObject("coordinates");
                            lat = Double.parseDouble(coordobj.getString("latitude"));
                            lng = Double.parseDouble(coordobj.getString("longitude"));
                            JSONArray arr2 = response.getJSONArray("hours");
                            JSONObject obj2 = arr2.getJSONObject(0);
                            isopennow = obj2.getString("is_open_now");
                            JSONArray urlarray = response.getJSONArray("photos");
                            System.out.println(urlarray);
                            for (int i=0;i<urlarray.length();i++){
                                urlofphotos.add(urlarray.getString(i));
                            }


                            // setting the click links for fb and twitter
                            facebookimg.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    String url = String.format("https://www.facebook.com/sharer/sharer.php?u=%s",busurl);
                                    System.out.println(url);
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(browserIntent);
                                }
                            });

                            twitterimg.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    String url = String.format("https://twitter.com/intent/tweet?text=Check %s on Yelp.&url=%s",busname,busurl);
                                    System.out.println(url);
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(browserIntent);
                                }
                            });

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
    }


}