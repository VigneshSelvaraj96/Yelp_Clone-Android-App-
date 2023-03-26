package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterRes extends RecyclerView.Adapter<MyAdapterRes.myviewholder> {

    ArrayList<ReservationClass> listofreservations;
    Context context;

    public MyAdapterRes(ArrayList<ReservationClass> listofreservations) {
        this.listofreservations = listofreservations;
    }


    @NonNull
    @Override
    public MyAdapterRes.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_reservations,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterRes.myviewholder holder, int position) {
        holder.serinum.setText(Integer.toString(position+1));
        holder.busnameres.setText(listofreservations.get(position).getName());
        holder.dateres.setText(listofreservations.get(position).getDate());
        holder.timeres.setText(listofreservations.get(position).getTime());
        holder.emailres.setText(listofreservations.get(position).getEmailid());

    }

    @Override
    public int getItemCount() {
        return listofreservations.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        TextView serinum;
        TextView busnameres;
        TextView dateres;
        TextView timeres;
        TextView emailres;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            serinum = itemView.findViewById(R.id.serinum);
            busnameres = itemView.findViewById(R.id.busnameres);
            dateres = itemView.findViewById(R.id.dateres);
            timeres = itemView.findViewById(R.id.timeres);
            emailres = itemView.findViewById(R.id.emailres);
        }
    }
}
