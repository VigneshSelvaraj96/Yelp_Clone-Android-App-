package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.myviewholder> {

    Context context;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> rating = new ArrayList<>();
    ArrayList<String> text = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();

    public MyReviewAdapter(ArrayList<String> name, ArrayList<String> rating, ArrayList<String> text, ArrayList<String> date) {
        this.name = name;
        this.rating = rating;
        this.text = text;
        this.date = date;
    }


    public class myviewholder extends RecyclerView.ViewHolder{

        TextView nameid;
        TextView textid;
        TextView ratingid;
        TextView dateid;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            nameid = itemView.findViewById(R.id.nameid);
            textid = itemView.findViewById(R.id.textid);
            ratingid = itemView.findViewById(R.id.ratingid);
            dateid = itemView.findViewById(R.id.dateid);
        }
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_review,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewAdapter.myviewholder holder, int position) {

        holder.nameid.setText(name.get(position));
        holder.ratingid.setText(rating.get(position));
        holder.textid.setText(text.get(position));
        holder.dateid.setText(date.get(position));

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
