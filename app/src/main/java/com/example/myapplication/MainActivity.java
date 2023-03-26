package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button submitbtn;
    Button clearbtn;
    EditText keyword;
    Spinner spinner;
    EditText distance;
    EditText location;
    boolean allfieldschecked=false;
    CheckBox autodetect;

    String keywordquery;
    int distancequery;
    double lat;
    double lng;
    String categoryquery;
    RecyclerView recyclerView;

    String backendurlproxy = "https://yelpcloneangular.wl.r.appspot.com";

    // data model for the search results
    ArrayList<String>  busids=new ArrayList<String>();
    ArrayList<String> serialnumber = new ArrayList<String>();
    ArrayList<String>  busimageurl = new ArrayList<String>();
    ArrayList<String>  busname=new ArrayList<String>();
    ArrayList<String>  busrating=new ArrayList<String>();
    ArrayList<String>  busdistance=new ArrayList<String>();
    // Adapter for searchresulttable
    Myadapter myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyApplication);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerview);
        RequestQueue queue = Volley.newRequestQueue(this);
        Spinner spinner = (Spinner) findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //settiing onclick for reservations
        ImageView reservebutton = (ImageView) findViewById(R.id.reservations_icon);
        reservebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });


        //setting autocomplete
        String[] suggestions = {};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, suggestions);
        Filter filter = adapter2.getFilter();
        filter = null;
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.keyword);
        actv.setAdapter(adapter2);
        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String backendurl = "https://yelpcloneangular.wl.r.appspot.com/autocomplete?key=";
                backendurl+=editable;
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        backendurl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                adapter2.clear();
                                adapter2.notifyDataSetChanged();
                                Log.i("response triggered", response.toString());
                                try {
                                    JSONArray arr = response.getJSONArray("terms");
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject jsonobject = arr.getJSONObject(i);
                                        adapter2.add(jsonobject.getString("text"));
                                        adapter2.notifyDataSetChanged();
                                    }
                                    JSONArray arr2 = response.getJSONArray("categories");
                                    for (int i = 0; i < arr2.length(); i++) {
                                        JSONObject jsonobject = arr2.getJSONObject(i);
                                        adapter2.add(jsonobject.getString("title"));
                                        adapter2.notifyDataSetChanged();
                                    }
                                    //actv.setAdapter(adapter2);

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
        });
        actv.setThreshold(0);//will start working from first character
        //actv.setAdapter(adapter2);//setting the adapter data into the AutoCompleteTextView
        submitbtn = (Button) findViewById(R.id.button);
        clearbtn = (Button) findViewById(R.id.button2);
        keyword =  (EditText) findViewById(R.id.keyword);
        distance = (EditText) findViewById(R.id.distance);
        location = (EditText) findViewById(R.id.location);
        autodetect = (CheckBox) findViewById(R.id.autodetect);


        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allfieldschecked = CheckAllFields();
                if(allfieldschecked){
                    System.out.println("all form fields validated");
                    keywordquery  = keyword.getText().toString();
                    try {
                        keywordquery= URLEncoder.encode(keywordquery, StandardCharsets.UTF_8.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if(distance.length() == 0 ) distancequery = (int)(10*1609.34);
                    else {
                        distancequery = Integer.parseInt(distance.getText().toString());
                        distancequery = (int)(distancequery*1609.34);
                    }
                    categoryquery = spinner.getSelectedItem().toString();
                    if(autodetect.isChecked())
                    {
                        String backendurl = "https://ipinfo.io/?token=340e355e87a33d";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, backendurl, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                           lat = Double.parseDouble(response.getString("loc").split("[,]")[0]);
                                           lng = Double.parseDouble(response.getString("loc").split("[,]")[1]);
                                            System.out.println(lat);
                                            System.out.println(lng);
                                            // searching for businesses using proxy
                                            String backendurl = String.format("https://yelpcloneangular.wl.r.appspot.com/searchall?term=%s&latitude=%f&longitude=%s&categories?=%s&radius=%d",keywordquery,lat,lng
                                                    ,categoryquery,distancequery);
                                            System.out.println(backendurl);
                                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                                    (Request.Method.GET, backendurl, null, new Response.Listener<JSONObject>() {

                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            System.out.println("Response: " + response.toString());
                                                            try {
                                                                JSONArray busarr = response.getJSONArray("businesses");
                                                                for(int i=0;i<busarr.length();i++){
                                                                    JSONObject obj = busarr.getJSONObject(i);
                                                                    serialnumber.add(String.valueOf(i+1));
                                                                    busimageurl.add(obj.getString("image_url"));
                                                                    double dist2 = Double.parseDouble(obj.getString("distance"));
                                                                    dist2 = dist2*0.000621371;
                                                                    DecimalFormat value = new DecimalFormat("#.#");
                                                                    busdistance.add(value.format(dist2));
                                                                    busrating.add(obj.getString("rating"));
                                                                    busname.add(obj.getString("name"));
                                                                    busids.add(obj.getString("id"));
                                                                    if (i==9) break;
                                                                }
                                                                myadapter = new Myadapter(busids,serialnumber,busimageurl,busname,busrating,busdistance);
                                                                recyclerView.setAdapter(myadapter);
                                                                Context c = MainActivity.this;
                                                                recyclerView.setLayoutManager(new LinearLayoutManager(c));
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
                                                            // TODO: Handle error

                                                        }
                                                    });
                                            queue.add(jsonObjectRequest);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error

                                    }
                                });
                        queue.add(jsonObjectRequest);

                    }
                    else if (!autodetect.isChecked())
                    {
                        String addr = location.getText().toString();
                        try {
                            addr= URLEncoder.encode(addr, StandardCharsets.UTF_8.toString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        String backendurl = String .format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=AIzaSyAFc1nfVRj69PHJgilPz2LNWBybwBCX9jQ",addr);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, backendurl, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray arr = response.getJSONArray("results");
                                            JSONObject res = arr.getJSONObject(0);
                                            JSONObject geometry = res.getJSONObject("geometry");
                                            JSONObject location = geometry.getJSONObject("location");
                                            lat = Double.parseDouble(location.getString("lat"));
                                            lng = Double.parseDouble(location.getString("lng"));
                                            System.out.println(lat);
                                            System.out.println(lng);
                                            // searching for businesses using proxy
                                            String backendurl = String.format("https://yelpcloneangular.wl.r.appspot.com/searchall?term=%s&latitude=%f&longitude=%s&categories?=%s&radius=%d",keywordquery,lat,lng
                                                    ,categoryquery,distancequery);
                                            System.out.println(backendurl);
                                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                                    (Request.Method.GET, backendurl, null, new Response.Listener<JSONObject>() {

                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            System.out.println("Response: " + response.toString());
                                                            try {
                                                                JSONArray busarr = response.getJSONArray("businesses");
                                                                for(int i=0;i<busarr.length();i++){
                                                                    JSONObject obj = busarr.getJSONObject(i);
                                                                    serialnumber.add(String.valueOf(i+1));
                                                                    busimageurl.add(obj.getString("image_url"));
                                                                    double dist2 = Double.parseDouble(obj.getString("distance"));
                                                                    dist2 = dist2*0.000621371;
                                                                    DecimalFormat value = new DecimalFormat("#.#");
                                                                    busdistance.add(value.format(dist2));
                                                                    busrating.add(obj.getString("rating"));
                                                                    busname.add(obj.getString("name"));
                                                                    busids.add(obj.getString("id"));
                                                                    if (i==9) break;
                                                                }
                                                                myadapter = new Myadapter(busids,serialnumber,busimageurl,busname,busrating,busdistance);
                                                                recyclerView.setAdapter(myadapter);
                                                                Context c = MainActivity.this;
                                                                recyclerView.setLayoutManager(new LinearLayoutManager(c));
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
                                                            // TODO: Handle error

                                                        }
                                                    });
                                            queue.add(jsonObjectRequest);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("error on url encoding");

                                    }
                                });
                        queue.add(jsonObjectRequest);

                    }

                }
            }
        });
        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword.getText().clear();
                distance.getText().clear();
                location.getText().clear();
                location.setError(null);
                spinner.setAdapter(adapter);
                if (autodetect.isChecked()) {
                    autodetect.setChecked(false);
                    location.setVisibility(View.VISIBLE);
                }
                myadapter.clear();
                myadapter.notifyDataSetChanged();
            }
        });

    }

    public void openNewActivity(){
        Intent intent = new Intent(this, reservationsactivity.class);
        startActivity(intent);
    }

    private boolean CheckAllFields() {
        if(keyword.length() ==0)
        {
            keyword.setError("This field is required");
            return false;
        }
        if(location.getVisibility() == View.VISIBLE && location.getText().toString().length() ==0)
        {
            location.setError("This field is required");
            return false;
        }
        return true;
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.autodetect:
                if (checked){
                    location.setVisibility(View.INVISIBLE);
                }
                else
                    location.setVisibility(View.VISIBLE);
        }
    }

}