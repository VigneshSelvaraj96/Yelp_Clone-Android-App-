package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyCarouselAdapter extends RecyclerView.Adapter<MyCarouselAdapter.myviewholder> {

    private ArrayList<String> urlimages = new ArrayList<String>();
    Context context;

    public MyCarouselAdapter(ArrayList<String>url){
        this.urlimages=url;
    }


    @NonNull
    @Override
    public MyCarouselAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.carousellimg,parent,false);
        return new MyCarouselAdapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCarouselAdapter.myviewholder holder, int position) {
        Picasso.get().load(urlimages.get(position)).into(holder.busimg);

    }

    @Override
    public int getItemCount() {
        return urlimages.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        ImageView busimg;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            busimg = itemView.findViewById(R.id.imgcaro);
        }
    }
}
