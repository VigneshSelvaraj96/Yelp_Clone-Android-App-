package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.lang.reflect.Type;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class busdetailsfragment extends Fragment {

    String busname;
    String address;
    String pricerange;
    String phonenumber;
    ArrayList<String> urlofphotos = new ArrayList<String>();
    String busurl;
    String isopennow;
    String category;
    String busid;
    double lat;
    double lng;

    //initializing text views
    TextView busaddr;
    TextView price;
    TextView pnum;
    TextView cat;
    TextView buslink;
    TextView status;


    //adapter for carousell
    MyCarouselAdapter myCarouselAdapter;
    RecyclerView myrecycler;

    //make reservation button
    Button reservebutton;

    //reservation modal
    EditText dateicked;
    EditText time;
    EditText emailid;

    // Reservation arraylist
    ArrayList<ReservationClass> reservationClasses;

    final Calendar myCalendar= Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_busdetailsfragment, container, false);

        busaddr = view.findViewById(R.id.busaddress);
        price = view.findViewById(R.id.pricerange);
        pnum = view.findViewById(R.id.phonenumber);
        cat = view.findViewById(R.id.categorytext);
        buslink=view.findViewById(R.id.buslink);
        status = view.findViewById(R.id.status);

        myrecycler = view.findViewById(R.id.carouselrecycler);

        reservebutton = view.findViewById(R.id.reservebutton);



        busid = getActivity().getIntent().getStringExtra("busid");
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        setdata(busid,queue);

        myCarouselAdapter = new MyCarouselAdapter(urlofphotos);
        myrecycler.setAdapter(myCarouselAdapter);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        myrecycler.setLayoutManager(horizontalLayoutManager);


        reservebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showcustomdialog();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void updateDate() {
        String myFormat = "MM/dd/yyyy"; //put your date format in which you need to display
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        dateicked.setText(sdf.format(myCalendar.getTime()));
    }

    private void showcustomdialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reservation_modal);

        //initialize views of dialog
        TextView title = dialog.findViewById(R.id.busnamereserve);
        emailid = dialog.findViewById(R.id.emailidreserve);
        dateicked = dialog.findViewById(R.id.datereserve);
        time = dialog.findViewById(R.id.timereserve);
        Button submitbutton = dialog.findViewById(R.id.submitreserve);


        title.setText(busname);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };


        dateicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog d = new DatePickerDialog(dialog.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                d.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                d.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(dialog.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                mTimePicker.show();

            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                //implement validation logic
                if(!isValidEmail(emailid.getText().toString()))
                {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "InValid Email Address.",
                            Toast.LENGTH_LONG).show();
                }
                else if (!isvalidtime(time.getText().toString()))
                {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Time should be between 10AM AND 5PM",
                            Toast.LENGTH_LONG).show();
                }

                else
                {

                    Context context = getContext();
                    //logic for shared preferences go here
                    SharedPreferences sharedPref = context.getSharedPreferences("listofreserve", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    Gson gson = new Gson();
                    String json = sharedPref.getString("reservations", null);
                    Type type = new TypeToken<ArrayList<ReservationClass>>() {}.getType();
                    reservationClasses = gson.fromJson(json, type);
                    // checking below if the array list is empty or not
                    if (reservationClasses == null) {
                        // if the array list is empty
                        // creating a new array list.
                        reservationClasses = new ArrayList<>();
                        System.out.println("reservation class is initially empty");
                    }
                    ReservationClass reservation = new ReservationClass(busname,emailid.getText().toString(),dateicked.getText().toString(),time.getText().toString());
                    reservationClasses.add(reservation);
                    String json2 = gson.toJson(reservationClasses);
                    editor.putString("reservations", json2);
                    editor.apply();
                    System.out.println("reservation length is: ");
                    System.out.println(reservationClasses.size());



                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Reservation Booked",
                            Toast.LENGTH_LONG).show();
                }
            }
        });



        dialog.show();

    }

    private boolean isvalidtime(String toString) {
        String hour = toString.split(":")[0];
        String min = toString.split(":")[1];
        String tgther = hour+min;
        int time = Integer.parseInt(tgther);
        if ( time > 1700 || time < 1000) return false;
        else return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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
                            for (int i=0;i<urlarray.length();i++){
                                urlofphotos.add(urlarray.getString(i));
                                myCarouselAdapter.notifyDataSetChanged();
                            }
                            pricerange = response.optString ("price");
                            System.out.println("price range is: ");
                            System.out.println(pricerange);
                            //dynamically setting all the textviews
                            if (address != null ) {
                                busaddr.setText(address);
                            } else {
                                busaddr.setText("N/A");
                            }
                            if (pricerange != "") {
                                price.setText(pricerange);
                            } else {
                                price.setText("N/A");
                            }
                            if (pnum != null) {
                                pnum.setText(phonenumber);
                            } else {
                                pnum.setText("N/A");
                            }
                            if (cat != null) {
                                cat.setText(category);
                            } else {
                                cat.setText("N/A");
                            }
                            //buslink
                            //status.setText(isopennow);
                            buslink.setPaintFlags(buslink.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                            buslink.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String url = busurl;
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