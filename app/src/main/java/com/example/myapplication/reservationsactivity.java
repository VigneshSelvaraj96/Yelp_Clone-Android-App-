package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class reservationsactivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ReservationClass> thislist;
    TextView nodata;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservationsactivity);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        nodata = findViewById(R.id.nodata);

        //get shared preference and set to internal list
        SharedPreferences sharedPref = this.getSharedPreferences("listofreserve", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = sharedPref.getString("reservations", null);
        Type type = new TypeToken<ArrayList<ReservationClass>>() {}.getType();
        thislist = gson.fromJson(json, type);
        // checking below if the array list is empty or not
        if (thislist == null) {
            // if the array list is empty
            // creating a new array list.
            thislist = new ArrayList<>();
            System.out.println("reservation class is initially empty");
        }



        recyclerView = findViewById(R.id.recuclerviewreservations);
        MyAdapterRes myAdapterResAdapter = new MyAdapterRes(thislist);
        recyclerView.setAdapter(myAdapterResAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (thislist.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        }
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Take action for the swiped item
                int posremoved = viewHolder.getAbsoluteAdapterPosition();
                thislist.remove(posremoved);
                myAdapterResAdapter.notifyItemRemoved(posremoved);
                SharedPreferences sharedPreferences = getSharedPreferences("listofreserve", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(thislist);
                editor.putString("reservations", json);
                editor.apply();
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content).getRootView(),"Removing Existing Reservation",Snackbar.LENGTH_SHORT);
                snackbar.show();
                if (thislist.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    nodata.setVisibility(View.GONE);
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red))
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}