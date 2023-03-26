package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.myviewholder> {
    Context context;
    ArrayList<String>  busids=new ArrayList<String>();
    ArrayList<String> serialnumber = new ArrayList<String>();
    ArrayList<String>  busimageurl = new ArrayList<String>();
    ArrayList<String>  busname=new ArrayList<String>();
    ArrayList<String>  busrating=new ArrayList<String>();
    ArrayList<String>  busdistance=new ArrayList<String>();

    public Myadapter(ArrayList<String> busids, ArrayList<String> serialnumber, ArrayList<String> busimageurl, ArrayList<String> busname, ArrayList<String> busrating, ArrayList<String> busdistance) {
        this.busids = busids;
        this.serialnumber = serialnumber;
        this.busimageurl = busimageurl;
        this.busname = busname;
        this.busrating = busrating;
        this.busdistance = busdistance;
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.serialno.setText(serialnumber.get(position));
        holder.busname.setText(busname.get(position));
        holder.busrating.setText(busrating.get(position));
        holder.busdist.setText(busdistance.get(position));
        Picasso.get().load(busimageurl.get(position)).into(holder.busimg);

        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MainActivity2.class);
                int pos = holder.getAdapterPosition();
                intent.putExtra("busid",busids.get(pos));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return busids.size();
    }

    public void clear() {
        busids.clear();
        serialnumber.clear();
        busimageurl.clear();
        busname.clear();
        busrating.clear();
        busdistance.clear();
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        TextView serialno;
        ImageView busimg;
        TextView busname;
        TextView busrating;
        TextView busdist;
        ConstraintLayout mainlayout;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            serialno = itemView.findViewById(R.id.serialnum);
            busimg = itemView.findViewById(R.id.busimg);
            busname = itemView.findViewById(R.id.busname);
            busrating = itemView.findViewById(R.id.busrating);
            busdist = itemView.findViewById(R.id.busdistance);
            mainlayout= itemView.findViewById(R.id.main_row_layout);
        }
    }
}
